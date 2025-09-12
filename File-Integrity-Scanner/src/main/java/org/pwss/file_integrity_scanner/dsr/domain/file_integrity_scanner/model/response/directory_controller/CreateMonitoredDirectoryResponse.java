package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.response.directory_controller;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.monitored_directory.MonitoredDirectory;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A record representing a response containing the created monitored directory.
 */
@Schema(description = "Represents a response with the created monitored directory.")
public record CreateMonitoredDirectoryResponse(
                /**
                 * The instance of MonitoredDirectory that was created as a result of the
                 * request.
                 */
                @Schema(description = "The created monitored directory", implementation = MonitoredDirectory.class) MonitoredDirectory mDirectory) {
}