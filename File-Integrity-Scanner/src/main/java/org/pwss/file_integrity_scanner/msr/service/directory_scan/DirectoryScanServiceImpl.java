package org.pwss.file_integrity_scanner.msr.service.directory_scan;

import lib.pwss.hash.FileHashHandler;
import lib.pwss.hash.model.HashForFilesOutput;
import org.pwss.FileNavigatorImpl;
import org.pwss.file_integrity_scanner.msr.domain.model.entities.checksum.Checksum;
import org.pwss.file_integrity_scanner.msr.domain.model.entities.monitored_directory.MonitoredDirectory;
import org.pwss.file_integrity_scanner.msr.domain.model.entities.scan.Scan;
import org.pwss.file_integrity_scanner.msr.domain.model.entities.scan_details.ScanDetails;
import org.pwss.file_integrity_scanner.msr.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.nio.file.Path;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.Future;

//NOTES: This is some initial progress and it is not complete.
// It is kind of working but needs more development.
// we need to manage scan statuses properly, handle existing files in the database, and so on I will catch more issues later.
// Also some improvements to overall code structure & design are needed.
@Service
public class DirectoryScanServiceImpl implements DirectoryScanService {
    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ScanRepository scanRepository;

    @Autowired
    private ScanDetailsRepository scanDetailsRepository;

    @Autowired
    private MonitoredDirectoryRepository monitoredDirectoryRepository;

    @Autowired
    private ChecksumRepository checksumRepository;

    private boolean isScanning = false;
    private Thread scanThread;
    final FileHashHandler fileHashHandler = new FileHashHandler();

    @Override
    public void scanAllDirectories() {
        if (!isScanning) {
            scanThread = new Thread(() -> {
                List<MonitoredDirectory> directories = monitoredDirectoryRepository.findAll();
                if (directories.isEmpty()) {
                    System.out.println("No directories to scan.");
                    return;
                }
                // Start scanning directories
                isScanning = true;
                for (MonitoredDirectory dir : directories) {
                    if (!isScanning) break;
                    // Create a new Scan instance for each monitored directory
                    Scan scan = new Scan();
                    scan.setMonitoredDirectory(dir);
                    scan.setScanTime(OffsetDateTime.now());
                    scan.setStatus("IN_PROGRESS");
                    scanRepository.save(scan);
                    // Start scanning the directory
                    scanDirectory(dir, scan);
                }
                isScanning = false;
            });
            scanThread.start();
        }
    }

    @Override
    public void scanDirectory(MonitoredDirectory monitoredDirectory, Scan scanInstance) {
        try {
            FileNavigatorImpl navigator = new FileNavigatorImpl(monitoredDirectory.getPath());
            List<Future<List<Path>>> futures = navigator.traverseFiles();

            for (Future<List<Path>> future : futures) {
                List<Path> paths = future.get(); // Blocks until the directory's scan is done
                for (Path path : paths) {
                    if (!isScanning) return;
                    File resolvedFile = path.toFile();

                    // Compute hashes
                    HashForFilesOutput computedHashes = computeHash(resolvedFile);

                    // Build entities
                    // TODO: We need to figure out if file already exists in the database instead of always creating a new enitity
                    org.pwss.file_integrity_scanner.msr.domain.model.entities.file.File fileEntity = new org.pwss.file_integrity_scanner.msr.domain.model.entities.file.File();
                    fileEntity.setPath(resolvedFile.getPath());
                    fileEntity.setBasename(resolvedFile.getName());
                    fileEntity.setDirectory(resolvedFile.getParent());
                    fileEntity.setSize(resolvedFile.length());
                    OffsetDateTime lastModified = Instant.ofEpochMilli(resolvedFile.lastModified())
                            .atOffset(ZoneOffset.UTC);
                    fileEntity.setMtime(lastModified);
                    fileRepository.save(fileEntity);

                    Checksum checksums = new Checksum();
                    checksums.setChecksumSha256(computedHashes.sha256());
                    checksums.setChecksumSha3(computedHashes.sha3());
                    checksums.setChecksumBlake2b(computedHashes.blake2());
                    checksums.setFile(fileEntity);
                    checksumRepository.save(checksums);

                    ScanDetails scanDetails = new ScanDetails();
                    scanDetails.setFile(fileEntity);
                    scanDetails.setScan(scanInstance);
                    scanDetails.setChecksum(checksums);
                    scanDetailsRepository.save(scanDetails);
                }
            }

        } catch (Exception e) {
            e.printStackTrace(); // Replace with proper logging in production
        }
    }

    @Override
    public void stopScan() {
        if (isScanning) {
            isScanning = false;
            if (scanThread != null && scanThread.isAlive()) {
                scanThread.interrupt();
            }
        }
    }

    private HashForFilesOutput computeHash(File file) {
        return fileHashHandler.GetAllHashes(file);
    }
}
