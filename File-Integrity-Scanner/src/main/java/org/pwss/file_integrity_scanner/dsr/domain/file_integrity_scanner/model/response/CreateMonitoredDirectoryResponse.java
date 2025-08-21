package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.response;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.monitored_directory.MonitoredDirectory;

/**
 * A record representing a response containing the created monitored directory.
 */
public record CreateMonitoredDirectoryResponse(
        /**
         * The instance of MonitoredDirectory that was created as a result of the
         * request.
         */
        MonitoredDirectory mDirectory) {
}