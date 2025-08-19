package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.file;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.file.File;

public interface FileService {

    /**
     * Finds a file entity by its path.
     *
     * @param path the path of the file to find
     * @return the file entity if found, or null if not found
     */
    File findByPath(String path);

    /**
     * Checks if a file entity exists in the database by its path.
     *
     * @param path the path of the file to check
     * @return true if the file exists, false otherwise
     */
    boolean existsByPath(String path);

    /**
     * Saves a file entity to the database.
     *
     * @param file the file entity to save
     */
    void save(File file);
}
