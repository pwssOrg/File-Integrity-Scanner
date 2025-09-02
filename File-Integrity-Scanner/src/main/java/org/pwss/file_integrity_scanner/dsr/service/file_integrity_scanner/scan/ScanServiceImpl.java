package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.scan;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

import org.pwss.file_integrity_scanner.component.DirectoryTraverser;
import org.pwss.file_integrity_scanner.component.FileHashComputer;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.checksum.Checksum;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.diff.Diff;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.monitored_directory.MonitoredDirectory;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.note.Note;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan.Scan;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan_summary.ScanSummary;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.scan.ScanTaskState;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.scan.enumeration.ScanStatus;
import org.pwss.file_integrity_scanner.dsr.domain.mixed.time.Time;
import org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner.scan.ScanRepository;
import org.pwss.file_integrity_scanner.dsr.service.PWSSbaseService;
import org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.checksum.ChecksumService;
import org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.diff.IntegrityService;
import org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.file.FileService;
import org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.monitored_directory.MonitoredDirectoryService;
import org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.note.NoteService;
import org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.scan_summary.ScanSummaryService;
import org.pwss.file_integrity_scanner.dsr.service.mixed.time.TimeService;
import org.pwss.file_integrity_scanner.exception.file_integrity_scanner.NoActiveMonitoredDirectoriesException;
import org.pwss.file_integrity_scanner.exception.file_integrity_scanner.ScanAlreadyRunningException;
import org.pwss.io_file.FileTraverser;
import org.pwss.io_file.FileTraverserImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lib.pwss.hash.model.HashForFilesOutput;

@Service
public class ScanServiceImpl extends PWSSbaseService<ScanRepository, Scan, Integer> implements ScanService {

    private final MonitoredDirectoryService monitoredDirectoryService;

    private final FileService fileService;

    private final ScanSummaryService scanSummaryService;

    private final ChecksumService checksumService;

    private final TimeService timeService;

    private final NoteService noteService;

    private final IntegrityService integrityService;

    private final DirectoryTraverser directoryTraverser;

    private final FileHashComputer fileHashComputer;

    private final org.slf4j.Logger log;
    private final DateTimeFormatter timeAndDateStringForLogFormat;

    @Qualifier("threadPoolTaskScheduler")

    private final TaskScheduler taskScheduler;
    private ScheduledFuture<?> monitorTaskFuture;

    /**
     * Map to hold active scan tasks, keyed by directory path
     */
    private final ConcurrentMap<String, ScanTaskState> activeScanTasks;

    /**
     * Flag to indicate if an ongoing scan should be stopped
     */
    private volatile boolean stopRequested;

    // TODO: Add Java Docs
    private volatile boolean isScanRunning;

    /**
     * Schedule rate in milliseconds for monitoring scan tasks.
     */
    private final long SCAN_TASK_MONITOR_DELAY = 5000L;

    /**
     * FileTraverser from PWSS Directory Nav
     */
    private FileTraverser fileTraverser;

    @Autowired
    public ScanServiceImpl(ScanRepository repository,
            MonitoredDirectoryService monitoredDirectoryService,
            FileService fileService,
            ScanSummaryService scanSummaryService,
            ChecksumService checksumService,
            TimeService timeService,
            NoteService noteService,
            IntegrityService integrityService,
            DirectoryTraverser directoryTraverser,
            FileHashComputer fileHashComputer,
            TaskScheduler taskScheduler) {
        super(repository);
        this.monitoredDirectoryService = monitoredDirectoryService;
        this.fileService = fileService;
        this.scanSummaryService = scanSummaryService;
        this.checksumService = checksumService;
        this.timeService = timeService;
        this.noteService = noteService;
        this.integrityService = integrityService;
        this.directoryTraverser = directoryTraverser;
        this.fileHashComputer = fileHashComputer;
        this.log = org.slf4j.LoggerFactory.getLogger(ScanServiceImpl.class);
        this.timeAndDateStringForLogFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.activeScanTasks = new ConcurrentHashMap<>();
        this.taskScheduler = taskScheduler;

        // Initialize volatile state booleans to false
        this.stopRequested = false;
        this.isScanRunning = false;
    }

    @Transactional
    @Override
    public void scanAllDirectories() throws ScanAlreadyRunningException, NoActiveMonitoredDirectoriesException {

        if (isScanRunning) {
            throw new ScanAlreadyRunningException();
        }

        this.isScanRunning = true;

        stopRequested = false; // Reset stop request at the start of a new scan.

        List<MonitoredDirectory> activeDirs = monitoredDirectoryService.findByIsActive(true);

        if (activeDirs.isEmpty()) {
            log.warn("No active monitored directories found");
            isScanRunning = false;
        }

        if (isScanRunning == false) {
            throw new NoActiveMonitoredDirectoriesException(
                    "No active monitored directories found when scanning all directories");
        }
        try {
            log.info("Starting scan of all monitored directories at {}",
                    OffsetDateTime.now().format(timeAndDateStringForLogFormat));
            log.debug("Scan is running - {}", isScanRunning);
            // Iterate over each monitored directory in database
            for (MonitoredDirectory dir : activeDirs) {
                if (stopRequested) {
                    log.info("Scan stopped  by user request.");
                    break;
                }

                final Time time = new Time(OffsetDateTime.now(), OffsetDateTime.now());
                timeService.save(time);

                final Note note = new Note("No notes", time);
                noteService.save(note);

                final Boolean isBaseLineScan = !dir.getBaselineEstablished();
                Scan scan = new Scan(time, ScanStatus.IN_PROGRESS.toString(), dir, note, isBaseLineScan);

                repository.save(scan);

                fileTraverser = new FileTraverserImpl();
                Future<List<File>> futureFiles;

                this.startMonitoring();
                if (dir.getIncludeSubdirectories()) {
                    futureFiles = scanDirectoryAsync(dir.getPath(), fileTraverser);
                } else {
                    futureFiles = scanDirectoryAsync(dir.getPath());
                }

                activeScanTasks.put(dir.getPath(), new ScanTaskState(futureFiles, scan));
            }
        } catch (Exception e) {
            log.error("Error while scanning all monitored directories {},", e.getMessage());
        }

    }

    @Transactional
    @Override
    public void scanSingleDirectory(MonitoredDirectory dir)
            throws ScanAlreadyRunningException, NoActiveMonitoredDirectoriesException {

        if (isScanRunning) {
            throw new ScanAlreadyRunningException();
        }

        if (dir == null) {
            throw new NullPointerException("Monitored directory cannot be null");
        }

        if (!dir.getIsActive()) {
            log.warn("No active monitored directory found");
            throw new NoActiveMonitoredDirectoriesException("monitored directory id: ", dir.getId());
        }

        log.info("Starting scan of monitored directory - {} at time - {}",
                dir.getPath(), OffsetDateTime.now().format(timeAndDateStringForLogFormat));

        this.isScanRunning = true;
        log.debug("Scan is running - {}", isScanRunning);

        fileTraverser = new FileTraverserImpl();

        final Time time = new Time(OffsetDateTime.now(), OffsetDateTime.now());
        timeService.save(time);

        final Note note = new Note("No notes", time);
        noteService.save(note);

        final Boolean isBaseLineScan = !dir.getBaselineEstablished();

        Scan scan = new Scan(time, ScanStatus.IN_PROGRESS.toString(),
                dir, note, isBaseLineScan);

        repository.save(scan);

        Future<List<File>> futureFiles;

        this.startMonitoring();
        if (dir.getIncludeSubdirectories()) {
            futureFiles = scanDirectoryAsync(dir.getPath(), fileTraverser);
        } else {

            futureFiles = scanDirectoryAsync(dir.getPath());
        }

        activeScanTasks.put(dir.getPath(), new ScanTaskState(futureFiles, scan));
    }

    /**
     * Asynchronously scans a directory to retrieve a list of top-level files.
     *
     * This is an overloaded version that calls the main implementation with a null
     * fileTraverser, which results in only collecting top-level files from the
     * specified directory.
     *
     * @param directoryPath the path of the directory to scan for top-level files
     * @return a {@link Future} containing the list of top-level files found in the
     *         directory
     */
    private Future<List<File>> scanDirectoryAsync(String directoryPath) {

        return scanDirectoryAsync(directoryPath, null);
    }

    /**
     * Asynchronously scans a directory and its subdirectories to retrieve a list of
     * files.
     *
     * This method uses either a provided {@link FileTraverser} instance to traverse
     * the
     * directory,
     * or it collects only top-level files in the directory if no traversal strategy
     * is specified.
     *
     * @param directoryPath the path of the directory to scan
     * @param fileTraverser an optional FileTraverser implementation for directory
     *                      traversal;
     *                      if null, only top-level files are collected
     * @return a {@link Future} containing the list of files found in the directory
     */
    private Future<List<File>> scanDirectoryAsync(String directoryPath, FileTraverser fileTraverser) {
        if (fileTraverser != null) {
            return directoryTraverser.collectFilesInDirectory(directoryPath, fileTraverser);
        } else {
            return directoryTraverser.collectTopLevelFiles(new File(directoryPath));
        }
    }

    /**
     * Starts monitoring of ongoing scan tasks.
     * <p>
     * This method schedules a task to monitor active scan tasks at a fixed rate,
     * determined by the SCAN_TASK_MONITOR_DELAY constant. If a monitoring task is
     * not already running,
     * it initializes and starts one using the task scheduler.
     */
    private void startMonitoring() {
        if (monitorTaskFuture == null || monitorTaskFuture.isCancelled()) {
            final Duration duration = Duration.ofMillis(SCAN_TASK_MONITOR_DELAY);
            monitorTaskFuture = taskScheduler.scheduleAtFixedRate(this::monitorOngoingScanTasks, duration);
            log.info("Scan task monitoring started (delay: {} ms).", SCAN_TASK_MONITOR_DELAY);
        } else {
            log.debug("Monitoring is already running.");
        }
    }

    /**
     * Stops the monitoring of ongoing scan tasks.
     * <p>
     * This method cancels the scheduled monitoring task if it is currently running.
     */
    private void stopMonitoring() {
        if (monitorTaskFuture != null && !monitorTaskFuture.isCancelled()) {
            monitorTaskFuture.cancel(true);
            log.info("Scan task monitoring stopped.");
        } else {
            log.debug("Monitoring is not running.");
        }
    }

    /**
     * Monitors active scan tasks and processes completed scans.
     * <p>
     * It iterates through the active scan tasks, checks their status, and processes
     * completed scans by invoking the `finishScanTask` method.
     * <p>
     * If a scan is still in progress, it logs the status.
     */
    private void monitorOngoingScanTasks() {
        log.debug("monitorOngoingScanTasks: Invoked to check active scan tasks.");
        if (activeScanTasks.isEmpty()) {
            return;
        }

        for (String dirPath : activeScanTasks.keySet()) {
            ScanTaskState task = activeScanTasks.get(dirPath);
            Future<List<File>> future = task.future();

            if (future.isDone()) {
                log.info("Traversing completed for directory: {}", dirPath);
                try {
                    List<File> files = future.get(); // Non-blocking since we check if the future is done
                    if (finalizeScanTask(task.scan(), files)) {
                        log.info("Scan recursive completed successfully for directory: {}", dirPath);
                    } else {
                        log.warn("Scan recursive was not completed successfully for directory: {}", dirPath);
                    }
                } catch (InterruptedException e) {
                    log.error("Scan interrupted for directory {}: {}", dirPath, e.getMessage());
                    activeScanTasks.remove(dirPath);
                } catch (ExecutionException e) {
                    log.error("Execution exception while completing scan for directory {}: {}", dirPath,
                            e.getMessage());
                    activeScanTasks.remove(dirPath);
                }
                activeScanTasks.remove(dirPath);
            } else {
                log.info("Traversing for directory {} is still in progress.", dirPath);
            }
        }
    }

    /**
     * Finalizes the scan task for a given scan instance and list of files.
     * <p>
     * This method processes each file in the provided list, updates the scan
     * status,
     * and establishes a baseline for the monitored directory if necessary. If a
     * stop
     * request is detected, the scan is marked as cancelled. In case of errors
     * during
     * processing, the scan is marked as failed. Regardless of the outcome, the task
     * is removed from the active scan tasks map.
     *
     * @param scanInstance the scan instance associated with the task
     * @param files        the list of files to process
     * @return true if the scan was successfully completed, false otherwise
     */
    private boolean finalizeScanTask(Scan scanInstance, List<File> files) {
        String dirPath = scanInstance.getMonitoredDirectory().getPath();

        log.debug("Finalizing Scan Task for MonitoredDirectory {}", dirPath);

        try {
            // Retrieve the list of files from the completed scan
            for (File file : files) {
                if (stopRequested) {
                    break;// Exit if stop is requested
                }
                // Process each file found in the directory and its subdirectories
                processFile(file, scanInstance);
            }

            if (stopRequested) {
                // Mark the scan as cancelled if a stop was requested
                scanInstance.setStatus(ScanStatus.CANCELLED.toString());
                repository.save(scanInstance);
                return false; // Scan Stopped / Not complete (no baseline)
            } else {
                MonitoredDirectory dir = scanInstance.getMonitoredDirectory();
                scanInstance.setStatus(ScanStatus.COMPLETED.toString());

                // If the Baseline has not yet been established and the Scan was Successful
                if (!dir.getBaselineEstablished()) {
                    log.info("Establishing baseline for directory: {}", dirPath);
                    dir.setBaselineEstablished(true);
                    monitoredDirectoryService.save(dir);
                } else {
                    log.info("Baseline already established for directory: {}", dirPath);
                }
                log.info("Completed scan for directory {}", dirPath);
                repository.save(scanInstance);

                return true; // Successful Scan
            }

        } catch (Exception e) {
            // Handle errors during scan processing
            log.error("Scan Failed {} - Start Time {} - End Time {}", e.getMessage(),
                    scanInstance.getScanTime().getCreated().format(timeAndDateStringForLogFormat),
                    OffsetDateTime.now().format(timeAndDateStringForLogFormat));

            scanInstance.setStatus(ScanStatus.FAILED.toString());
            repository.save(scanInstance);
            return false; // Scan Failed
        } finally {
            // Remove the task from active tasks regardless of success or failure
            if (activeScanTasks.containsKey(dirPath)) {
                activeScanTasks.remove(dirPath);
                log.info("Removed scan task for directory: {}", dirPath);

                if (activeScanTasks.isEmpty()) {
                    log.debug("Active Scan Tasks Map is empty");
                    log.info("Scan finalized with Monitored Directory: {} being the last element", dirPath);

                    // Stop monitoring if no active scan tasks remain
                    stopMonitoring();

                    // Shutdown the file traverser thread pool
                    fileTraverser.shutdownThreadPool();

                    // Set state boolean to false so this method can be ran again
                    this.isScanRunning = false;
                }

            }
        }
    }

    /**
     * Processes a file by checking its existence in the database, updating or
     * creating
     * the corresponding file entity, and saving associated checksum and scan
     * details.
     * <p>
     * This method is annotated with {@link Transactional}, ensuring all operations
     * within this method are executed within a single transaction.
     *
     * @param file         the file to process
     * @param scanInstance the scan instance associated with the file
     */
    @Transactional
    private void processFile(File file, Scan scanInstance) {
        if (file == null || !file.isFile()) {
            return;
        }

        boolean fileInDatabase = fileService.existsByPath(file.getPath());

        final HashForFilesOutput computedHashes;

        final Optional<HashForFilesOutput> computedHashesOpt = fileHashComputer.computeHashes(file);

        if (computedHashesOpt.isPresent()) {
            computedHashes = computedHashesOpt.get();
        } else {

            log.error(
                    "Could not compute hash for file - {}\nProbably due to a file access issue. Try runnning File Integrity Scanner as an administrator and see if it resolves this issue",
                    file.getName());
            return;
        }

        MonitoredDirectory mDirectory = scanInstance.getMonitoredDirectory();

        boolean isNewfile = false;

        org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.file.File fileEntity;
        if (fileInDatabase) {
            // Fetch existing entity and update fields
            fileEntity = fileService.findByPath(file.getPath());
            fileEntity.setSize(file.length());
            OffsetDateTime lastModified = Instant.ofEpochMilli(file.lastModified())
                    .atOffset(ZoneOffset.UTC);
            fileEntity.setMtime(lastModified);
        } else if (scanInstance.getIsBaselineScan()) {
            // Create new entity if it is a baseline scan
            fileEntity = new org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.file.File(
                    file.getPath(),
                    file.getName(), file.getParent(), file.length(),
                    Instant.ofEpochMilli(file.lastModified()).atOffset(ZoneOffset.UTC));
            isNewfile = true;
        }

        else {
            // If file is not in the repository layer and the scan is not a baseline scan

            final String fileNotIncludedInBaseScanText = "There are files in your monitored directory which is not included in the baseline scan\\n"
                    + //
                    "In order to include those scan in the File Integrity Scan you need to reset your baseline";

            try {

                Note note = scanInstance.getNotes();

                if (!noteService.anyNoteContains(note, fileNotIncludedInBaseScanText)) {
                    noteService.updateNote(note,
                            fileNotIncludedInBaseScanText);

                    scanInstance.setNotes(note);
                    this.repository.save(scanInstance);
                }
            }

            catch (NullPointerException nullPointerException) {
                log.debug(
                        "No notes with value where present in the scaninstance with ID - {}\nWill Create a new Note Instance",
                        scanInstance.getId());

                final Time time = new Time(OffsetDateTime.now(), OffsetDateTime.now());
                timeService.save(time);

                final Note note = new Note(fileNotIncludedInBaseScanText, time);

                noteService.save(note);

                scanInstance.setNotes(note);

                this.repository.save(scanInstance);

            }

            return;
        }

        if (isNewfile)
            log.debug("Adding new file to the repository layer: {}", fileEntity.getPath());
        else
            log.debug("Updating existing file in the repository layer: {}", fileEntity.getPath());
        fileService.save(fileEntity);

        Checksum checksum = new Checksum(fileEntity, computedHashes.sha256(), computedHashes.sha3(),
                computedHashes.blake2());
        checksumService.save(checksum);

        // If the baseline is established, check if the file has changed
        if (monitoredDirectoryService.isBaseLineEstablished(mDirectory)) {

            final Optional<ScanSummary> oBaselineScanSummaryFromRepository = scanSummaryService
                    .findScanSummmarWithHighestIdWhereScanBaselineIsSetToTrue(fileEntity);

            if (oBaselineScanSummaryFromRepository.isPresent()) {
                ScanSummary baselineScanSummaryFromRepository = oBaselineScanSummaryFromRepository.get();
                Checksum oldChecksum = baselineScanSummaryFromRepository.getChecksum();
                if (fileHashComputer.compareHashes(oldChecksum, checksum)) {
                    log.info("File {} has not changed since last scan ✅", fileEntity.getPath());

                    final ScanSummary currentScanSummary = new ScanSummary(scanInstance, fileEntity, checksum);
                    scanSummaryService.save(currentScanSummary);

                } else {
                    log.warn("File {} has changed since last scan ⚠️", fileEntity.getPath());

                    final ScanSummary currentScanSummary = new ScanSummary(scanInstance, fileEntity, checksum);
                    scanSummaryService.save(currentScanSummary);

                    final Time time = new Time(OffsetDateTime.now(), OffsetDateTime.now());
                    timeService.save(time);
                    integrityService.save(new Diff(baselineScanSummaryFromRepository, currentScanSummary, time));

                }
            }
        } else {
            log.info("No existing checksum found for file from prior scans {}", fileEntity.getPath());
            scanSummaryService.save(new ScanSummary(scanInstance, fileEntity, checksum));
        }

    }

    @Override
    public void stopScan() {

        if (activeScanTasks.isEmpty()) {
            log.debug("No ongoing scans\nRequest to stop scan denied");
        } else {
            stopRequested = true;
            log.info("Scan stop requested. Will stop after current file processing.");
        }
    }

    @Override
    public Boolean isScanRunning() {
        return isScanRunning;
    }

    @Override
    public Optional<Scan> getMostRecentScan() {
        return this.repository.findMostRecentScan();
    }

    @Override
    public Optional<Scan> findById(Integer id) {

        if (id != null)
            return repository.findById(id);
        else
            return Optional.empty();
    }
}