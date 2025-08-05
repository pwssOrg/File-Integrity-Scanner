package org.pwss.file_integrity_scanner.msr.service.scan;

import jakarta.transaction.Transactional;
import lib.pwss.hash.model.HashForFilesOutput;
import org.pwss.file_integrity_scanner.component.DirectoryTraverser;
import org.pwss.file_integrity_scanner.component.FileHashComputer;
import org.pwss.file_integrity_scanner.msr.domain.model.entities.checksum.Checksum;
import org.pwss.file_integrity_scanner.msr.domain.model.entities.monitored_directory.MonitoredDirectory;
import org.pwss.file_integrity_scanner.msr.domain.model.entities.scan.Scan;
import org.pwss.file_integrity_scanner.msr.domain.model.entities.scan.ScanStatus;
import org.pwss.file_integrity_scanner.msr.domain.model.entities.scan_summary.ScanSummary;
import org.pwss.file_integrity_scanner.msr.repository.ScanRepository;
import org.pwss.file_integrity_scanner.msr.service.BaseService;
import org.pwss.file_integrity_scanner.msr.service.checksum.ChecksumService;
import org.pwss.file_integrity_scanner.msr.service.file.FileService;
import org.pwss.file_integrity_scanner.msr.service.monitored_directory.MonitoredDirectoryService;
import org.pwss.file_integrity_scanner.msr.service.scan.internal.ScanTaskState;
import org.pwss.file_integrity_scanner.msr.service.scan_summary.ScanSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class ScanServiceImpl extends BaseService<ScanRepository> implements ScanService {

    @Autowired
    private final MonitoredDirectoryService monitoredDirectoryService;

    @Autowired
    private final FileService fileService;

    @Autowired
    private final ScanSummaryService scanSummaryService;

    @Autowired
    private final ChecksumService checksumService;

    @Autowired
    private final DirectoryTraverser directoryTraverser;

    @Autowired
    private final FileHashComputer fileHashComputer;

    private final org.slf4j.Logger log;
    private final DateTimeFormatter timeAndDateStringForLogFormat;

    // Map to hold active scan tasks, keyed by directory path
    private final ConcurrentMap<String, ScanTaskState> activeScanTasks;

    // Flag to indicate if an ongoing scan should be stopped
    private boolean stopRequested = false;

    // Delay in milliseconds for monitoring scan tasks
    private final int SCAN_TASK_MONITOR_DELAY = 5000;

    @Autowired
    public ScanServiceImpl(ScanRepository repository,
                           MonitoredDirectoryService monitoredDirectoryService,
                           FileService fileService,
                           ScanSummaryService scanSummaryService,
                           ChecksumService checksumService,
                           DirectoryTraverser directoryTraverser,
                           FileHashComputer fileHashComputer) {
        super(repository);
        this.monitoredDirectoryService = monitoredDirectoryService;
        this.fileService = fileService;
        this.scanSummaryService = scanSummaryService;
        this.checksumService = checksumService;
        this.directoryTraverser = directoryTraverser;
        this.fileHashComputer = fileHashComputer;
        this.log = org.slf4j.LoggerFactory.getLogger(ScanServiceImpl.class);
        this.timeAndDateStringForLogFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.activeScanTasks = new ConcurrentHashMap<>();
    }

    @Override
    public void scanAllDirectories() {
        log.info("Starting scan of all monitored directories at {}", OffsetDateTime.now().format(timeAndDateStringForLogFormat));
        stopRequested = false; // Reset stop request at the start of a new scan.

        try {
            List<MonitoredDirectory> activeDirs = monitoredDirectoryService.findByIsActive(true);

            if (activeDirs.isEmpty()) {
                log.warn("No active monitored directories found.");
                return;
            }

            // Iterate over each monitored directory in database
            for (MonitoredDirectory dir : activeDirs) {
                if (stopRequested) {
                    log.info("Scan stopped  by user request.");
                    break;
                }

                Scan scan = new Scan();
                scan.setMonitoredDirectory(dir);
                scan.setScanTime(OffsetDateTime.now());
                scan.setStatus(ScanStatus.IN_PROGRESS.toString());

                repository.save(scan);

                Future<List<File>> futureFiles = scanDirectoryAsync(dir.getPath(), dir.getIncludeSubdirectories());
                activeScanTasks.put(dir.getPath(), new ScanTaskState(futureFiles, scan));
            }
        } catch (Exception e) {
            log.error("Error while scanning all monitored directories {},", e.getMessage());
        }
    }

    @Override
    public void scanSingleDirectory(MonitoredDirectory dir) {
        if (!dir.getIsActive()) {
            log.warn("Monitored directory {} is not active. Skipping scan.", dir.getPath());
            return;
        }

        Scan scan = new Scan();
        scan.setMonitoredDirectory(dir);
        scan.setScanTime(OffsetDateTime.now());
        scan.setStatus(ScanStatus.IN_PROGRESS.toString());

        repository.save(scan);

        Future<List<File>> futureFiles = scanDirectoryAsync(dir.getPath(), dir.getIncludeSubdirectories());
        activeScanTasks.put(dir.getPath(), new ScanTaskState(futureFiles, scan));
    }

    /**
     * Asynchronously scans a directory and its subdirectories to retrieve a list of files.
     *
     * @param directoryPath the path of the directory to scan
     * @return a Future containing the list of files found in the directory
     */
    private Future<List<File>> scanDirectoryAsync(String directoryPath, boolean includeSubdirectories) {
        if (includeSubdirectories) {
            return directoryTraverser.collectFilesInDirectory(directoryPath);
        } else {
            return directoryTraverser.collectTopLevelFiles(new File(directoryPath));
        }
    }

    /**
     * Monitors active scan tasks and processes completed scans.
     * <p>
     * This method is scheduled to run at a fixed delay of 5000 milliseconds.
     * It iterates through the active scan tasks, checks their status, and processes
     * completed scans by invoking the `finishScanTask` method.
     * <p>
     * If a scan is still in progress, it logs the status.
     */
    @Scheduled(fixedDelay = SCAN_TASK_MONITOR_DELAY)
    public void monitorOngoingScanTasks() {
        if (activeScanTasks.isEmpty()) {
            log.debug("No active scan tasks to process.");
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
                } catch (ExecutionException e) {
                    log.error("Execution exception while completing scan for directory {}: {}", dirPath, e.getMessage());
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
     * This method processes each file in the provided list, updates the scan status,
     * and establishes a baseline for the monitored directory if necessary. If a stop
     * request is detected, the scan is marked as cancelled. In case of errors during
     * processing, the scan is marked as failed. Regardless of the outcome, the task
     * is removed from the active scan tasks map.
     *
     * @param scanInstance the scan instance associated with the task
     * @param files        the list of files to process
     * @return true if the scan was successfully completed, false otherwise
     */
    private boolean finalizeScanTask(Scan scanInstance, List<File> files) {
        String dirPath = scanInstance.getMonitoredDirectory().getPath();

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
                    scanInstance.getScanTime().format(timeAndDateStringForLogFormat),
                    OffsetDateTime.now().format(timeAndDateStringForLogFormat));

            scanInstance.setStatus(ScanStatus.FAILED.toString());
            repository.save(scanInstance);
            return false; // Scan Failed
        } finally {
            // Remove the task from active tasks regardless of success or failure
            if (activeScanTasks.containsKey(dirPath)) {
                activeScanTasks.remove(dirPath);
                log.info("Removed scan task for directory: {}", dirPath);
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
        if (!file.isFile()) {
            return;
        }

        org.pwss.file_integrity_scanner.msr.domain.model.entities.file.File fileEntity;
        boolean fileInDatabase = fileService.existsByPath(file.getPath());

        try {
            HashForFilesOutput computedHashes = fileHashComputer.computeHashes(file);

            MonitoredDirectory mDirectory = scanInstance.getMonitoredDirectory();

            if (fileInDatabase) {
                // Fetch existing entity and update fields
                fileEntity = fileService.findByPath(file.getPath());
                fileEntity.setSize(file.length());
                OffsetDateTime lastModified = Instant.ofEpochMilli(file.lastModified())
                        .atOffset(ZoneOffset.UTC);
                fileEntity.setMtime(lastModified);
                log.debug("Updating existing file in DB: {}", fileEntity.getPath());
            } else {
                // Create new entity
                fileEntity = new org.pwss.file_integrity_scanner.msr.domain.model.entities.file.File();
                fileEntity.setPath(file.getPath());
                fileEntity.setBasename(file.getName());
                fileEntity.setDirectory(file.getParent());
                fileEntity.setSize(file.length());
                OffsetDateTime lastModified = Instant.ofEpochMilli(file.lastModified())
                        .atOffset(ZoneOffset.UTC);
                fileEntity.setMtime(lastModified);
                log.debug("Adding new file to DB: {}", fileEntity.getPath());
            }

            fileService.save(fileEntity);

            Checksum checksum = new Checksum();
            checksum.setChecksumSha256(computedHashes.sha256());
            checksum.setChecksumSha3(computedHashes.sha3());
            checksum.setChecksumBlake2b(computedHashes.blake2());
            checksum.setFile(fileEntity);
            checksumService.save(checksum);

            // If the baseline is established, check if the file has changed
            if (monitoredDirectoryService.isBaseLineEstablished(mDirectory)) {
                List<Checksum> dbChecksums = checksumService.findByFile(fileEntity);
                if (!dbChecksums.isEmpty()) {
                    Checksum oldChecksum = dbChecksums.getFirst();
                    if (fileHashComputer.compareHashes(oldChecksum, checksum)) {
                        log.info("File {} has not changed since last scan ✅", fileEntity.getPath());
                    } else {
                        log.warn("File {} has changed since last scan ⚠️", fileEntity.getPath());
                        // TODO: Figure out what to do with changed files, add some notes to scan summary?
                    }
                } else {
                    log.info("No existing checksum found for file from prior scans {}", fileEntity.getPath());
                }
            }

            ScanSummary scanSummary = new ScanSummary();
            scanSummary.setFile(fileEntity);
            scanSummary.setScan(scanInstance);
            scanSummary.setChecksum(checksum);
            scanSummaryService.save(scanSummary);
        } catch (OutOfMemoryError memoryError) {
            log.warn("Out of memory error while processing file: {}. Skipping file.", file.getPath());
        }
    }

    @Override
    public void stopScan() {
        stopRequested = true;
        log.info("Scan stop requested. Will stop after current file processing.");
    }
}
