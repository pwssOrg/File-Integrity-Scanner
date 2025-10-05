package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_controller;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents a request to quarantine a file.
 * This class encapsulates parameters for quarantining a specific file by its
 * ID.
 */
@Schema(description = "Represents a request to quarantine a file.")
public record QuarantineFileRequest(
        /**
         * The unique identifier of the file to be quarantined.
         */
        @Schema(description = "The unique identifier of the file to be quarantined", example = "12345") Long fileId) {
}