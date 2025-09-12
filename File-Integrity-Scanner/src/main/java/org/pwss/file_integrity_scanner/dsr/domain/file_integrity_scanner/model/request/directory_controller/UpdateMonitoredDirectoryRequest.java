package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.directory_controller;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A request object to update information about a monitored directory.
 *
 * This record holds the parameters necessary to update details of a monitored
 * directory, such as its ID, activity status, notes, and whether it includes
 * subdirectories.
 */
@Schema(description = "Represents a request to update information about a monitored directory.")
public record UpdateMonitoredDirectoryRequest(
        /**
         * The unique identifier for the monitored directory that needs to be updated.
         */
        @Schema(description = "The unique identifier of the monitored directory", example = "1") Integer id,

        /**
         * A flag indicating whether the monitored directory is active or not.
         * True if active, false otherwise.
         */
        @Schema(description = "A flag indicating whether the monitored directory is active", example = "true") Boolean isActive,

        /**
         * Notes or comments related to the update of this monitored directory.
         */
        @Schema(description = "Notes or comments related to the update", example = "Updated for performance reasons") String notes,

        /**
         * A flag indicating whether subdirectories should be included in monitoring.
         * True if subdirectories are to be included, false otherwise.
         */
        @Schema(description = "A flag indicating whether subdirectories should be included in monitoring", example = "true") Boolean includeSubDirs) {
}