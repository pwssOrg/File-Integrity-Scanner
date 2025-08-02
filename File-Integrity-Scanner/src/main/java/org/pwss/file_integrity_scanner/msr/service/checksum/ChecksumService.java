package org.pwss.file_integrity_scanner.msr.service.checksum;


import org.pwss.file_integrity_scanner.msr.domain.model.entities.checksum.Checksum;

public interface ChecksumService {
    /**
     * Saves a checksum entity to the database.
     *
     * @param checksum the checksum entity to save
     */
    void save(Checksum checksum);
}
