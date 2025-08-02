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
import org.pwss.file_integrity_scanner.msr.service.scan_summary.ScanSummaryService;
import org.pwss.util.PWSSDirectoryNavUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

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
    }

    // Flag to indicate if an ongoing scan should be stopped
    private boolean stopRequested = false;

    @Override
    public void scanAllDirectories() {
        stopRequested = false; // Reset stop request at the start of a new scan.

        try {
            List<MonitoredDirectory> directories = monitoredDirectoryService.findByIsActive(true);

            if (directories.isEmpty()) {
                log.warn("No active monitored directories found.");
                return;
            }

            // Iterate over each monitored directory in database
            for (MonitoredDirectory dir : directories) {

                if (stopRequested) {
                    log.info("Scan stopped by user request.");
                    break;
                }

                Scan scan = new Scan();
                scan.setMonitoredDirectory(dir);
                scan.setScanTime(OffsetDateTime.now());
                scan.setStatus(ScanStatus.IN_PROGRESS.toString());

                repository.save(scan);

                // If Scan was Successful (And also invocation of method scanDirectory)
                if (scanDirectory(scan)) {

                    log.info("Successful Scan Result for All Monitored Directories {}", dir.getPath());

                    // If the Baseline has not yet been established and the Scan was Successful
                    if (!dir.getBaselineEstablished()) {
                        dir.setBaselineEstablished(true);
                        monitoredDirectoryService.save(dir);
                    }

                    scan.setStatus(ScanStatus.COMPLETED.toString());
                }

                // Else - the scan has failed!
                else {
                    // No changes to the baseLineEstablished Boolean for the Monitored Directory

                    log.error("Scan failed for Monitored Directory {}", dir.getPath());

                    // An added Note that is saved to the persistence layer
                    dir.setNotes(ScanStatus.FAILED.toString());
                    monitoredDirectoryService.save(dir);

                    // After scanning, update the scan status to completed
                    scan.setStatus(ScanStatus.FAILED.toString());
                }

                repository.save(scan);
            }
        } catch (Exception e) {
            log.error("Error while scanning all monitored directories {},", e.getMessage());
        }
    }

    /**
     * Scans a monitored directory using the provided scan instance.
     * @param scanInstance       the scan instance used for scanning the directory
     * @return true if the monitored directory scan is successful, false otherwise
     */
    private final Boolean scanDirectory(Scan scanInstance) {

        MonitoredDirectory monitoredDirectory;

        try {
            monitoredDirectory = scanInstance.getMonitoredDirectory();
            List<File> files = directoryTraverser.collectFilesInDirectory(monitoredDirectory.getPath());

            for (File file : files) {
                if (stopRequested) {
                    break; // Exit if stop is requested
                }
                // Process each file found in the directory and its subdirectories
                processFile(file, scanInstance);
            }

            if (stopRequested) {
                scanInstance.setStatus(ScanStatus.CANCELLED.toString());
                repository.save(scanInstance);
                return false; // Scan Stopped / Not complete (no baseline)
            } else {
                log.debug("Scan Successful for monitored directory {}", monitoredDirectory.getPath());
                return true; // Successful Scan
            }

        } catch (Exception e) {

            log.error("Scan Failed {} - Start Time {} - End Time {}", e.getMessage(),
                    scanInstance.getScanTime().format(timeAndDateStringForLogFormat),
                    OffsetDateTime.now().format(timeAndDateStringForLogFormat));

            scanInstance.setStatus(ScanStatus.FAILED.toString());
            repository.save(scanInstance);
            return false; // Scan Failed

        }
    }

    @Transactional
    @Override
    public void processFile(File file, Scan scanInstance) {
        if (!file.isFile()) {
            return;
        }

        org.pwss.file_integrity_scanner.msr.domain.model.entities.file.File fileEntity;
        boolean fileInDatabase = fileService.existsByPath(file.getPath());

        HashForFilesOutput computedHashes = fileHashComputer.computeHashes(file);

       MonitoredDirectory mDirectory = scanInstance.getMonitoredDirectory();

    
       if(monitoredDirectoryService.isBaseLineEstablished(mDirectory)){
            
        // Compare current hash to previous hash (Ticket #46)

       }

       else{
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

        // TODO: Try / Catch and

        final MonitoredDirectory mDirectory;

        mDirectory = scanInstance.getMonitoredDirectory();

        if (includeSubFolders) {

            // Regular scan

            if (scanDirectory(scanInstance)) {

                log.info("Scan Successful for Monitored Directory {}", mDirectory.getPath());
                scanInstance.setStatus(ScanStatus.COMPLETED.toString());

                // If the Baseline has not yet been established and the Scan was Successful
                if (!mDirectory.getBaselineEstablished()) {
                    mDirectory.setBaselineEstablished(true);
                    monitoredDirectoryService.save(mDirectory);
                }

                  scanInstance.setStatus(ScanStatus.COMPLETED.toString());
                    // Save scanInstance in the persistence layer
                    this.repository.save(scanInstance);
                    return true; // OK Scan :)

            }

            else {

                log.error("Scan failed for Monitored Directory {}", mDirectory.getPath());

                // An added Note that is saved to the persistence layer
                mDirectory.setNotes("FAILED");
                monitoredDirectoryService.save(mDirectory);
                this.repository.save(scanInstance);
                return false;
            }

        }

        // No Subfolders
        else {

            final File file;

            try {

                file = new File(mDirectory.getPath());
                log.debug("Created new entity - scanMonitoredDirectory -  In No Subfolders If Block - Path {}",
                        file.getAbsolutePath());
                Optional<List<File>> topLevelFiles = directoryTraverser.collectTopLevelFiles(file);

                if (topLevelFiles.isPresent()) {

                    topLevelFiles.get().stream().forEach(tp -> processFile(tp, scanInstance));

                    scanInstance.setStatus(ScanStatus.COMPLETED.toString());
                    // Save scanInstance in the persistence layer
                    this.repository.save(scanInstance);
                    return true; // OK Scan :)
                } else {
                    scanInstance.setStatus(ScanStatus.FAILED.toString());
                    // Save scanInstance in the persistence layer
                    this.repository.save(scanInstance);
                    return false; // No Top Files Present - Scan Failed
                }
            }

            catch (ExecutionException executingException) {

                log.error("ExecutionException in scanMonitoredDirectory - {}", executingException);
            }

            catch (InterruptedException interruptedException) {

                log.error("InterruptedException in scanMonitoredDirectory", interruptedException.getMessage());
            }

            catch (Exception exception) {

                log.error("Generic Exception in scanMonitoredDirectory", exception.getMessage());
            }

        }
        // Should not happen :P 
         return false;
    }
   
}
