package org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner.file;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.file.File;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
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
     * Finds up to a specified number of files whose basename matches the given
     * search string using a
     * case-insensitive LIKE query.
     *
     * @param searchString the search string to match against file basenames. Can
     *                     contain wildcards like % and _.
     * @param information  pagination information
     * @return a {@link List} of up to the specified number of file entities whose
     *         basename matches the search
     *         string,
     *         or an empty list if no files are found
     */
    List<File> findByBasenameLikeIgnoreCase(String searchString, Pageable information);

    /**
     * Checks if a file entity exists with the given path.
     *
     * @param path the file path to check. Must not be null or empty.
     * @return true if a file with the given path exists, false otherwise
     */
    boolean existsByPath(String path);
}
