package org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.file.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link File} entities.
 *
 * This interface extends {@link JpaRepository}, providing CRUD operations and
 * additional custom query methods
 * to interact with the database table that stores file information.
 */
public interface FileRepository extends JpaRepository<File, Long> {

    /**
     * Finds a file entity by its path.
     *
     * @param path the file path to search for. Must not be null or empty.
     * @return an {@link Optional} containing the file entity with the given path,
     *         or an empty {@link Optional} if no matching file is found
     */
    Optional<File> findByPath(String path);

    /**
     * Checks if a file entity exists with the given path.
     *
     * @param path the file path to check. Must not be null or empty.
     * @return true if a file with the given path exists, false otherwise
     */
    boolean existsByPath(String path);
}
