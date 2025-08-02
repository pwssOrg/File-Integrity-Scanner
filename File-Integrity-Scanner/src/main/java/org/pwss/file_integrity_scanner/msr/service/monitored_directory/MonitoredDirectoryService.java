package org.pwss.file_integrity_scanner.msr.service.monitored_directory;

import org.pwss.file_integrity_scanner.msr.domain.model.entities.monitored_directory.MonitoredDirectory;

import java.util.List;

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
     * Checks whether the baseline has been established for the given monitored
     * directory.
     *
     * @param mDirectory the {@link MonitoredDirectory} entity to check
     * @return true if the baseline is established, false otherwise
     */
    Boolean isBaseLineEstablished(MonitoredDirectory mDirectory);

    /**
     * Persists a monitored directory entity in the database.
     *
     * This method saves or updates the provided {@code MonitoredDirectory}
     * entity, ensuring its state is recorded persistently. It handles all
     * necessary operations to make the entity available for future queries.
     *
     * @param mDirectory the {@link MonitoredDirectory} entity to be saved or
     *                   updated in the database
     */
    void save(MonitoredDirectory mDirectory);

}
