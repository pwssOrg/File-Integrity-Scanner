package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_integrity_controller;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents a request to retrieve recent scans.
 * This class is used to encapsulate parameters for querying
 * the most recent scans, specifying how many scan results to return.
 */
@Schema(description = "Represents a request to retrieve recent scans.")
public record RetrieveRecentScansRequest(
        /**
         * The number of most recent scans to return.
         */
        @Schema(description = "The number of most recent scans to return", example = "10") int nrOfScans) {
}