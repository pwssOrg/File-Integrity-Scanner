package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request;

/**
 * A record representing a request to create a monitored directory.
 */
public record CreateMonitoredDirectoryRequest(
        /**
         * The path of the directory to be monitored.
         */
        String path,

        /**
         * Indicates whether subdirectories should be included in monitoring (true) or not (false).
         */
        boolean includeSubdirectories,

        /**
         * A boolean indicating whether the directory should be active (true) or inactive (false).
         */
        boolean isActive
) {}