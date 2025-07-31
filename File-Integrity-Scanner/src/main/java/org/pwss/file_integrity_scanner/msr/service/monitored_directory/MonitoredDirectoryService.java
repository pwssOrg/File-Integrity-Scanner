package org.pwss.file_integrity_scanner.msr.service.monitored_directory;

import java.util.List;
import org.pwss.file_integrity_scanner.msr.domain.model.entities.monitored_directory.MonitoredDirectory;

/**
 * Service interface for managing {@link MonitoredDirectory} entities.
 * This interface defines methods for interacting with monitored directories,
 * including finding directories based on their active status.
 */
public interface MonitoredDirectoryService {

     /**
     * Finds all monitored directories that are active or inactive based on the given status.
     *
     * @param isActive true to find active directories, false to find inactive ones
     * @return a list of monitored directories matching the specified active status,
     *         or null if no matches are found or an error occurs
     */
    List<MonitoredDirectory> findByIsActive(boolean isActive);
}
