package org.pwss.file_integrity_scanner.msr.service.checksum;


import org.pwss.file_integrity_scanner.msr.domain.model.entities.checksum.Checksum;
import org.pwss.file_integrity_scanner.msr.domain.model.entities.file.File;

import java.util.List;

public interface ChecksumService {

    /**
     * Retrieves a list of checksums associated with the specified file.
     * <p>
     * This method queries the database to find all checksum entities
     * that are linked to the given file.
     *
     * @param file the file for which to retrieve associated checksums
     * @return a list of checksums associated with the specified file
     */
    List<Checksum> findByFile(File file);

    /**
     * Saves a checksum entity to the database.
     *
     * @param checksum the checksum entity to save
     */
    void save(Checksum checksum);
}
