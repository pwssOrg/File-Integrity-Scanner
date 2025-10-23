package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_integrity_controller;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Immutable data carrier for requesting to start all scans.
 *
 * This record contains the maximum file size for hash extraction attempts,
 * making it easy to pass this information between different layers of the
 * application in a thread-safe manner.
 */
@Schema(description = "Represents a request to start all scans with specified parameters.")
public record StartAllRequest(
                /**
                 * The maximum file size for hash extraction attempts.
                 * Cannot be null. If null, an exception may be thrown during validation or
                 * processing.
                 */
                @Schema(description = "The maximum file size to consider for hash extraction attempts", example = "12345") Long maxHashExtractionFileSize) {
}
