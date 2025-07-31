package org.pwss.file_integrity_scanner.msr.service.checksum;


import org.pwss.file_integrity_scanner.msr.domain.model.entities.checksum.Checksum;

public interface ChecksumService {
    void save(Checksum checksum);
}
