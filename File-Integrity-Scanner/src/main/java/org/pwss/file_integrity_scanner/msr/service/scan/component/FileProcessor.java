package org.pwss.file_integrity_scanner.msr.service.scan.component;

import lib.pwss.hash.model.HashForFilesOutput;
import org.pwss.file_integrity_scanner.msr.domain.model.entities.checksum.Checksum;
import org.pwss.file_integrity_scanner.msr.domain.model.entities.scan.Scan;
import org.pwss.file_integrity_scanner.msr.domain.model.entities.scan_summary.ScanSummary;
import org.pwss.file_integrity_scanner.msr.repository.ChecksumRepository;
import org.pwss.file_integrity_scanner.msr.repository.FileRepository;
import org.pwss.file_integrity_scanner.msr.repository.ScanSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
public class FileProcessor {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ChecksumRepository checksumRepository;

    @Autowired
    private ScanSummaryRepository scanDetailsRepository;

    @Autowired
    private FileHashComputer fileHashComputer;

    /**
     * Processes a file by checking its existence in the database, updating or creating
     * the corresponding file entity, and saving associated checksum and scan details.
     *
     * @param path the path of the file to process
     * @param scanInstance the scan instance associated with the file
     */
    public void process(Path path, Scan scanInstance) {
        File resolvedFile = path.toFile();
        if (!resolvedFile.isFile()) {
            return;
        }

        org.pwss.file_integrity_scanner.msr.domain.model.entities.file.File fileEntity;
        boolean fileInDatabase = fileRepository.existsByPath(path.toString());

        HashForFilesOutput computedHashes = fileHashComputer.computeHashes(resolvedFile);

        if (fileInDatabase) {
            // Fetch existing entity and update fields
            fileEntity = fileRepository.findByPath(path.toString());
            fileEntity.setSize(resolvedFile.length());
            OffsetDateTime lastModified = Instant.ofEpochMilli(resolvedFile.lastModified())
                    .atOffset(ZoneOffset.UTC);
            fileEntity.setMtime(lastModified);
            System.out.println("Updating existing file in DB: " + fileEntity.getPath());
        } else {
            // Create new entity
            fileEntity = new org.pwss.file_integrity_scanner.msr.domain.model.entities.file.File();
            fileEntity.setPath(resolvedFile.getPath());
            fileEntity.setBasename(resolvedFile.getName());
            fileEntity.setDirectory(resolvedFile.getParent());
            fileEntity.setSize(resolvedFile.length());
            OffsetDateTime lastModified = Instant.ofEpochMilli(resolvedFile.lastModified())
                    .atOffset(ZoneOffset.UTC);
            fileEntity.setMtime(lastModified);
        }

        fileRepository.save(fileEntity);

        Checksum checksums = new Checksum();
        checksums.setChecksumSha256(computedHashes.sha256());
        checksums.setChecksumSha3(computedHashes.sha3());
        checksums.setChecksumBlake2b(computedHashes.blake2());
        checksums.setFile(fileEntity);
        checksumRepository.save(checksums);

        ScanSummary scanDetails = new ScanSummary();
        scanDetails.setFile(fileEntity);
        scanDetails.setScan(scanInstance);
        scanDetails.setChecksum(checksums);
        scanDetailsRepository.save(scanDetails);
    }
}
