package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.monitored_directory;

import java.util.List;
import java.util.Optional;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.monitored_directory.MonitoredDirectory;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.CreateMonitoredDirectoryRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.response.CreateMonitoredDirectoryResponse;

/**
 * Service interface for managing {@link MonitoredDirectory} entities.
 * This interface defines methods for interacting with monitored directories,
 * including finding directories based on their active status.
 */
public interface MonitoredDirectoryService {

    /**
     * Finds all monitored directories that are active or inactive based on the
     * given status.
     *
     * @param isActive true to find active directories, false to find inactive ones
     * @return a list of monitored directories matching the specified active status,
     *         or null if no matches are found or an error occurs
     */
    List<MonitoredDirectory> findByIsActive(boolean isActive);

    /**
     * Finds a monitored directory by its unique identifier.
     *
     * This method attempts to locate and return a {@link MonitoredDirectory} object
     * that corresponds
     * to the specified ID. If no such directory exists, it returns an empty
     * {@link Optional}.
     *
     * @param id The unique identifier of the monitored directory to find.
     *           Must not be null.
     * @return An {@code Optional} containing the found {@code MonitoredDirectory},
     *         if one exists;
     *         otherwise, an empty {@code Optional}.
     */
    Optional<MonitoredDirectory> findById(Integer id);

    /**
     * Checks whether the baseline has been established for the given monitored
     * directory.
     *
     * @param mDirectory the {@link MonitoredDirectory} entity to check
     * @return true if the baseline is established, false otherwise
     */
    Boolean isBaseLineEstablished(MonitoredDirectory mDirectory);

    /**
     * Persists a monitored directory entity in the database.
     * <p>
     * This method saves or updates the provided {@code MonitoredDirectory}
     * entity, ensuring its state is recorded persistently. It handles all
     * necessary operations to make the entity available for future queries.
     *
     * @param mDirectory the {@link MonitoredDirectory} entity to be saved or
     *                   updated in the database
     */
    void save(MonitoredDirectory entity);

    /**
     * Resets a baseline for the specified monitored directory.
     *
     * A "baseline" typically represents a known good state against which subsequent
     * changes
     * in the directory are measured. This method is used to update or establish
     * this baseline,
     * which can then be used for comparison to detect modifications, additions, or
     * deletions
     * of files within the directory.
     *
     * @param mDirectory The monitored directory for which the new baseline should
     *                   be set.
     *                   Must not be null.
     * @return True if the baseline was successfully reset; false otherwise.
     */
    Boolean resetBaseline(MonitoredDirectory mDirectory);

    /**
     * Creates a monitored directory based on the provided request.
     *
     * This method validates the createRequest object. If validation passes, it
     * attempts to save a new
     * {@link MonitoredDirectory} instance with details from the createRequest
     * (path, active
     * status,
     * and subdirectory inclusion). Any exceptions during this process are logged as
     * errors,
     * and the method throws exceptions in such cases. If the request fails
     * validation,
     * the method throws a Security Exception.
     *
     * @param createRequest The request containing information needed to create a
     *                      monitored directory.
     *                      It includes path, active status, and whether to include
     *                      subdirectories.
     * @return {@link CreateMonitoredDirectoryResponse}
     */
    CreateMonitoredDirectoryResponse createMonitoredDirectoryFromRequest(CreateMonitoredDirectoryRequest createRequest)
            throws SecurityException, NullPointerException;

}
