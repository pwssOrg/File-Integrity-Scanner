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
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class ScanServiceImpl extends BaseService<ScanRepository> implements ScanService {

    private boolean isScanning = false; // This needs to be removed. If you use this threading strategy in another project you need
    // To use the volatile keyword. That keyword removes the caching from that boolean which is essintial when a boolean is used as a thread mutex.

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

    // Thread to handle the scan process
    private Thread scanThread;
    // Using a fixed thread pool to handle parallel scans (maybe we dont want to do this but let's try)
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

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
    }

    @Override
    public void scanAllDirectories() {
        if (isScanning) {
            System.out.println("Scan already in progress :D");
            return;
        }

        isScanning = true;

        scanThread = new Thread(() -> {
            try {
                List<MonitoredDirectory> directories = monitoredDirectoryService.findByIsActive(true);

                if (directories.isEmpty()) {
                    System.out.println("No active directories to scan :(");
                    isScanning = false;
                    return;
                }

                // Create a list to hold futures for each directory scan
                List<Future<?>> futures = new ArrayList<>();

                // Iterate over each monitored directory in database
                for (MonitoredDirectory dir : directories) {
                    // Run each directory scan in parallel
                    futures.add(executor.submit(() -> {
                        Scan scan = new Scan();
                        scan.setMonitoredDirectory(dir);
                        scan.setScanTime(OffsetDateTime.now());
                        scan.setStatus(ScanStatus.IN_PROGRESS.toString());
                        repository.save(scan);

                        scanDirectory(dir, scan);

                        scan.setStatus(ScanStatus.COMPLETED.toString());
                        repository.save(scan);
                    }));
                }

                // Wait for all scans to complete
                for (Future<?> future : futures) {
                    try {
                        future.get();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        // maybe we want to handle this somehow for each individual scan
                        System.out.println("Scan interrupted");
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }

            } finally {
                isScanning = false;
            }
        });

        // Start the scan thread
        scanThread.start();
    }

    @Override
    public void scanDirectory(MonitoredDirectory monitoredDirectory, Scan scanInstance) {
        // TODO: Fully migrate to use java.io.File instead, got some Maven issues I need help with
        try {
            List<Path> paths = directoryTraverser.scanDirectory(monitoredDirectory.getPath());

            for (Path path : paths) {
                if (!isScanning) {
                    System.out.println("Scan stopped prematurely :o");
                    break;
                }
                processFile(path.toFile(), scanInstance);
            }

        } catch (Exception e) {
            e.printStackTrace();
            scanInstance.setStatus(ScanStatus.FAILED.toString());
            repository.save(scanInstance);
        }
    }

    @Override
    @Transactional
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
            System.out.println("Updating existing file in DB: " + fileEntity.getPath());
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
        if (isScanning) {
            isScanning = false;
            if (scanThread != null && scanThread.isAlive()) {
                scanThread.interrupt();
            }
            executor.shutdownNow();
        }
    }
}
