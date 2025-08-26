package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.file;


import java.util.Optional;

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

    /**
     * Finds a file by its ID in the database.
     *
     * @param id the unique identifier of the file to find
     * @return an {@code Optional} containing the {@link File} with the specified ID
     *         if found,
     *         or empty if no such file exists
     */
    Optional<File> findById(Long id);

}
