package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.directory_controller;

/**
 * A request object to update information about a monitored directory.
 *
 * This record holds the parameters necessary to update details of a monitored
 * directory, such as its ID, activity status, notes, and whether it includes subdirectories.
 */
public record UpdateMonitoredDirectoryRequest(
    /**
     * The unique identifier for the monitored directory that needs to be updated.
     */
    Integer id,

    /**
     * A flag indicating whether the monitored directory is active or not.
     * True if active, false otherwise.
     */
    Boolean isActive,

    /**
     * Optional notes or comments related to the update of this monitored directory.
     */
    String notes,

    /**
     * A flag indicating whether subdirectories should be included in monitoring.
     * True if subdirectories are to be included, false otherwise.
     */
    Boolean includeSubDirs) {
}
