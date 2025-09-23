package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.history_controller;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A request object used to retrieve summaries associated with a specific file.
 *
 * This record holds the ID of the file whose summaries are being requested.
 */
@Schema(description = "Represents a request to retrieve summaries for a specific file.")
public record GetSummaryForFileRequest(
        /**
         * The unique identifier of the file whose summaries are being requested.
         */
        @Schema(description = "The unique ID of the file", example = "12345") Long fileId) {
}