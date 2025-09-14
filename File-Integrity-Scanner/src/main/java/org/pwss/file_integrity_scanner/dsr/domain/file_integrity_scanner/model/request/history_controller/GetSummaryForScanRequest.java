package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.history_controller;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A request object used to retrieve summaries associated with a specific scan.
 *
 * This record holds the ID of the scan whose summaries are being requested.
 */
@Schema(description = "Represents a request to retrieve summaries for a specific scan.")
public record GetSummaryForScanRequest(
        /**
         * The unique identifier of the scan whose summaries are being requested.
         */
        @Schema(description = "The unique ID of the scan", example = "12345") Integer scanId) {
}
