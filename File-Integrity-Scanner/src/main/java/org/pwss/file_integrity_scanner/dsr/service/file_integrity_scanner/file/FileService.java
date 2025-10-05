package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.file;

import java.util.List;
import java.util.Optional;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.file.File;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_controller.QuarantineFileRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_controller.UnQurantineFileRequest;
import org.pwss.file_integrity_scanner.exception.file_integrity_scanner.file.QuarantineFailedException;
import org.pwss.file_integrity_scanner.exception.file_integrity_scanner.file.UnquarantineFailedException;
import org.pwss.quarantineManager_aes.dto.MetaDataResult;

/**
 * Service interface for managing {@link File} entities.
 *
 * This service provides methods to interact with file information in a
 * database,
 * including finding files by their path and checking if they exist.
 */
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

    /**
     * Finds up to a specified number of files whose basename matches the given
     * search string using a case-insensitive
     * LIKE query.
     *
     * @param searchString the search string to match against file basenames. Can
     *                     contain wildcards like % and _.
     * @param limit        the maximum number of results to return
     * @param sortField    the field by which to sort results (e.g., "basename",
     *                     "createdDate")
     * @param ascending    true for ascending order, false for descending order
     * @return a {@link List} of up to the specified number of file entities whose
     *         basename matches the search string,
     *         or an empty list if no files are found
     */
    List<File> findFilesByBasenameLikeIgnoreCase(String searchString, int limit, String sortField,
            boolean ascending);

    /**
     * Quarantines a file based on the given request.
     *
     * @param request The {@link QuarantineFileRequest} containing information about
     *                the file to be quarantined.
     * @return A {@link MetaDataResult} containing information about the quarantine
     *         operation.
     * @throws SecurityException         if validation of the request fails.
     * @throws QuarantineFailedException if the quarantine operation fails for any
     *                                   reason.
     */
    MetaDataResult quranantine(QuarantineFileRequest request) throws SecurityException, QuarantineFailedException;

    /**
     * Unquarantines a file based on the given request.
     *
     * @param request The {@link UnQurantineFileRequest} containing information
     *                about
     *                the key path of the file to be unquarantined.
     * @return A {@link MetaDataResult} containing information about the
     *         unquarantine operation.
     * @throws SecurityException           if validation of the request fails.
     * @throws UnquarantineFailedException if the unquarantine operation fails for
     *                                     any reason.
     */
    MetaDataResult unQuarantine(UnQurantineFileRequest request) throws SecurityException, UnquarantineFailedException;

}
