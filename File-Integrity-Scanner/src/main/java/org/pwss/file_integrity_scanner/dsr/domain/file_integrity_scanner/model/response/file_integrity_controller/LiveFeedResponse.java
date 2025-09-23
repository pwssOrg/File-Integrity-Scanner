package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.response.file_integrity_controller;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A record representing a response containing information about the scan status
 * and current live feed data.
 */
@Schema(description = "Represents a response with scan status and live feed data.")
public record LiveFeedResponse(
        /**
         * Indicates whether a scan is currently running.
         *
         * @return {@code true} if a scan is currently running; {@code false} otherwise.
         */
        @Schema(description = "True if a scan is currently running, false otherwise", example = "true") boolean isScanRunning,

        /**
         * The string representation of the current live feed data.
         *
         * @return the live feed data as a string
         */
        @Schema(description = "The current live feed data", example = "Some live feed data") String livefeed) {
}