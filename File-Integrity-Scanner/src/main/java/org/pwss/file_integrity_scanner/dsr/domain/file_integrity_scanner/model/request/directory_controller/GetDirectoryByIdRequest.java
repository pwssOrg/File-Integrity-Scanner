package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.directory_controller;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A request object to retrieve details of a specific directory by its ID.
 *
 * This record holds the parameter necessary to identify which directory's
 * information should be retrieved, using its unique identifier.
 */
@Schema(description = "Represents a request to retrieve details of a specific directory by its ID.")
public record GetDirectoryByIdRequest(
                /**
                 * The unique identifier for the directory whose details are being requested.
                 */
                @Schema(description = "The unique identifier for the directory", example = "1") Integer directoryId) {
}