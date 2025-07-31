package org.pwss.file_integrity_scanner.msr.service.scan;

import jakarta.transaction.Transactional;
import lib.pwss.hash.model.HashForFilesOutput;
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
import org.pwss.file_integrity_scanner.msr.service.scan.component.DirectoryTraverser;
import org.pwss.file_integrity_scanner.msr.service.scan.component.FileHashComputer;
import org.pwss.file_integrity_scanner.msr.service.scan_summary.ScanSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

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
    }

    // Flag to indicate if an ongoing scan should be stopped
    private volatile boolean stopRequested = false;

    @Async
    @Override
    public void scanAllDirectories() {
        stopRequested = false; // Reset stop request at the start of a new scan.

        try {
            List<MonitoredDirectory> directories = monitoredDirectoryService.findByIsActive(true);

            if (directories.isEmpty()) {
                log.info("No active monitored directories found.");
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

                scanDirectory(dir, scan);

                // After scanning, update the scan status to completed
                scan.setStatus(ScanStatus.COMPLETED.toString());
                repository.save(scan);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    @Async
    @Override
    public void scanDirectory(MonitoredDirectory monitoredDirectory, Scan scanInstance) {
        try {
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
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            scanInstance.setStatus(ScanStatus.FAILED.toString());
            repository.save(scanInstance);
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
}
