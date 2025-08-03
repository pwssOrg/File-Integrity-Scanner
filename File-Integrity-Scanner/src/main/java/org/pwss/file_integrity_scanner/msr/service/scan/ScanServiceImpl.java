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
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
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

    private ConcurrentMap<String, ScanTaskState> activeScanTasks;

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

    // Flag to indicate if an ongoing scan should be stopped
    private boolean stopRequested = false;

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
                    log.info("Scan stopped by user request.");
                    break;
                }

                Scan scan = new Scan();
                scan.setMonitoredDirectory(dir);
                scan.setScanTime(OffsetDateTime.now());
                scan.setStatus(ScanStatus.IN_PROGRESS.toString());

                repository.save(scan);

                // Start scanning the directory and its subdirectories
                if (activeScanTasks == null) {
                    log.info("Active scan tasks map is null");
                } else {
                    log.info("Active scan tasks map is initialized with size: {}", activeScanTasks.size());
                }

                Future<List<File>> futureFiles = scanDirectoryAsync(dir.getPath(), true);
                activeScanTasks.put(dir.getPath(), new ScanTaskState(futureFiles, scan));
            }
        } catch (Exception e) {
            log.error("Error while scanning all monitored directories {},", e.getMessage());
        }
    }

    /**
     * Asynchronously scans a directory and its subdirectories( if specified)to retrieve a list of files.
     *
     * @param directoryPath     the path of the directory to scan
     * @param includeSubFolders a flag indicating whether to include subdirectories in the scan
     * @return a Future containing the list of files found in the directory
     */
    @Async
    private Future<List<File>> scanDirectoryAsync(String directoryPath, boolean includeSubFolders) throws ExecutionException, InterruptedException {
        return directoryTraverser.traverseDirectory(directoryPath, includeSubFolders);
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
    @Scheduled(fixedDelay = 5000, initialDelay = 2500)
    public final void monitorActiveScans() {
        if (activeScanTasks.isEmpty()) {
            log.info("No active scan tasks to process.");
            return;
        }

        for (String dirPath : activeScanTasks.keySet()) {
            ScanTaskState task = activeScanTasks.get(dirPath);
            Future<List<File>> future = task.future();

            if (future.isDone()) {
                log.info("Traversing completed for directory: {}", dirPath);
                completeScan(task);
                activeScanTasks.remove(dirPath);
            } else {
                log.info("Traversing for directory {} is still in progress.", dirPath);
            }
        }
    }

    /**
     * Completes the scan process for a given scan task state.
     * <p>
     * This method retrieves the list of files from the completed scan task and processes each file.
     * If a stop request is detected, the scan is marked as cancelled. Otherwise, the scan is marked
     * as completed, and the baseline is established for the monitored directory if it has not been set.
     * <p>
     * Any errors during processing are logged, and the scan is marked as failed. The updated scan
     * status is saved to the repository at the end of the method. The task is removed from the active
     * scan tasks map regardless of success or failure.
     *
     * @param scanTaskState the state of the scan task to process, containing the scan and its future result
     * @return true if the scan was successfully completed, false otherwise
     */
    @Async
    private boolean completeScan(ScanTaskState scanTaskState) {
        String dirPath = scanTaskState.scan().getMonitoredDirectory().getPath();
        Scan scan = scanTaskState.scan();

        try {
            // Retrieve the list of files from the completed scan
            List<File> files = scanTaskState.future().get(); // Non-blocking since the future is done.

            for (File file : files) {
                if (stopRequested) {
                    break;// Exit if stop is requested
                }
                // Process each file found in the directory and its subdirectories
                processFile(file, scanTaskState.scan());
            }

            if (stopRequested) {
                // Mark the scan as cancelled if a stop was requested
                scan.setStatus(ScanStatus.CANCELLED.toString());
                repository.save(scan);
                return false; // Scan Stopped / Not complete (no baseline)
            } else {
                MonitoredDirectory dir = scan.getMonitoredDirectory();
                scan.setStatus(ScanStatus.COMPLETED.toString());

                // If the Baseline has not yet been established and the Scan was Successful
                if (!dir.getBaselineEstablished()) {
                    log.info("Establishing baseline for directory: {}", dirPath);
                    dir.setBaselineEstablished(true);
                    monitoredDirectoryService.save(dir);
                } else {
                    log.info("Baseline already established for directory: {}", dirPath);
                }
                log.info("Completed scan for directory {}", dirPath);
                repository.save(scan);

                return true; // Successful Scan
            }

        } catch (Exception e) {
            // Handle errors during scan processing
            log.error("Scan Failed {} - Start Time {} - End Time {}", e.getMessage(),
                    scan.getScanTime().format(timeAndDateStringForLogFormat),
                    OffsetDateTime.now().format(timeAndDateStringForLogFormat));

            scan.setStatus(ScanStatus.FAILED.toString());
            repository.save(scan);
        } finally {
            // Remove the task from active tasks regardless of success or failure
            activeScanTasks.remove(dirPath);
            log.info("Removed scan task for directory: {}", dirPath);
        }
        return false; // Shouldn't reach here under normal execution
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

        HashForFilesOutput computedHashes = fileHashComputer.computeHashes(file);

        MonitoredDirectory mDirectory = scanInstance.getMonitoredDirectory();

        if (monitoredDirectoryService.isBaseLineEstablished(mDirectory)) {

            // Compare current hash to previous hash (Ticket #46)

        } else {
            // Scan without comparing hashes for this Monitored Directory
        }

        if (fileInDatabase) {
            // Fetch existing entity and update fields
            fileEntity = fileService.findByPath(file.getPath());
            fileEntity.setSize(file.length());
            OffsetDateTime lastModified = Instant.ofEpochMilli(file.lastModified())
                    .atOffset(ZoneOffset.UTC);
            fileEntity.setMtime(lastModified);
            log.info("Updating existing file in DB: {}", fileEntity.getPath());
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
            log.info("Adding new file to DB: {}", fileEntity.getPath());
        }

        fileService.save(fileEntity);

        Checksum checksums = new Checksum();
        checksums.setChecksumSha256(computedHashes.sha256());
        checksums.setChecksumSha3(computedHashes.sha3());
        checksums.setChecksumBlake2b(computedHashes.blake2());
        checksums.setFile(fileEntity);
        checksumService.save(checksums);

        ScanSummary scanSummary = new ScanSummary();
        scanSummary.setFile(fileEntity);
        scanSummary.setScan(scanInstance);
        scanSummary.setChecksum(checksums);
        scanSummaryService.save(scanSummary);
    }

    @Override
    public void stopScan() {
        stopRequested = true;
        log.info("Scan stop requested. Will stop after current file processing.");
    }

    @Override
    public Boolean scanMonitoredDirectory(Scan scanInstance) {
        return scanMonitoredDirectory(scanInstance, true);
    }

    @Override
    public Boolean scanMonitoredDirectory(Scan scanInstance, boolean includeSubFolders) {
//
        //final MonitoredDirectory mDirectory;
//
        //mDirectory = scanInstance.getMonitoredDirectory();
        //if (includeSubFolders) {
//
        //    // Regular scan
//
        //    if (completeScan(scanInstance)) {
//
        //        log.info("Scan Successful for Monitored Directory {}", mDirectory.getPath());
        //        scanInstance.setStatus(ScanStatus.COMPLETED.toString());
//
        //        // If the Baseline has not yet been established and the Scan was Successful
        //        if (!mDirectory.getBaselineEstablished()) {
        //            mDirectory.setBaselineEstablished(true);
        //            monitoredDirectoryService.save(mDirectory);
        //        }
//
        //        // Save scanInstance in the persistence layer
        //        this.repository.save(scanInstance);
        //        return true; // OK Scan :)
//
        //    } else {
//
        //        log.error("Scan failed for Monitored Directory {}", mDirectory.getPath());
//
        //        // An added Note that is saved to the persistence layer
        //        mDirectory.setNotes(ScanStatus.FAILED.toString());
        //        monitoredDirectoryService.save(mDirectory);
        //        this.repository.save(scanInstance);
        //        return false;
        //    }
//
        //}
//
        //// No Subfolders
        //else {
//
        //    final File file;
//
        //    try {
//
        //        file = new File(mDirectory.getPath());
        //        log.debug("Created new entity - scanMonitoredDirectory -  In No Subfolders If Block - Path {}",
        //                file.getAbsolutePath());
        //        Optional<List<File>> topLevelFiles = directoryTraverser.collectTopLevelFiles(file);
//
        //        if (topLevelFiles.isPresent()) {
//
        //            topLevelFiles.get().stream().forEach(tp -> processFile(tp, scanInstance));
//
        //            scanInstance.setStatus(ScanStatus.COMPLETED.toString());
        //            // Save scanInstance in the persistence layer
        //            this.repository.save(scanInstance);
        //            return true; // OK Scan :)
        //        } else {
        //            scanInstance.setStatus(ScanStatus.FAILED.toString());
        //            // Save scanInstance in the persistence layer
        //            this.repository.save(scanInstance);
        //            return false; // No Top Files Present - Scan Failed
        //        }
        //    } catch (ExecutionException executingException) {
//
        //        log.error("ExecutionException in scanMonitoredDirectory - {}", executingException);
        //    } catch (InterruptedException interruptedException) {
//
        //        log.error("InterruptedException in scanMonitoredDirectory", interruptedException.getMessage());
        //    } catch (Exception exception) {
//
        //        log.error("Generic Exception in scanMonitoredDirectory", exception.getMessage());
        //    }
//
        //}
        //// Fallback return - this should not be reached under normal execution
        return false;
    }
}
