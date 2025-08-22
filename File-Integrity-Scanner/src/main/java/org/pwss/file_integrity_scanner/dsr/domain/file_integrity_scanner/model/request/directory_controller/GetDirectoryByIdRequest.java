package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.directory_controller;

/**
 * A request object to retrieve details of a specific directory by its ID.
 *
 * This record holds the parameter necessary to identify which directory's
 * information should be retrieved, using its unique identifier.
 */
public record GetDirectoryByIdRequest(
        /**
         * The unique identifier for the directory whose details are being requested.
         */
        Integer directoryId) {
}