package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.directory_controller;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A record representing a request to create a monitored directory.
 */
@Schema(description = "Represents a request to create a monitored directory.")
public record CreateMonitoredDirectoryRequest(

                /**
                 * The path of the directory to be monitored.
                 */
                @Schema(description = "The path of the directory to be monitored", example = "/path/to/directory") String path,

                /**
                 * Indicates whether subdirectories should be included in monitoring (true) or
                 * not (false).
                 */
                @Schema(description = "Indicates if subdirectories should be included in monitoring", example = "true") boolean includeSubdirectories,

                /**
                 * A boolean indicating whether the directory should be active (true) or
                 * inactive (false).
                 */
                @Schema(description = "Indicates if the directory is active", example = "true") boolean isActive) {
}