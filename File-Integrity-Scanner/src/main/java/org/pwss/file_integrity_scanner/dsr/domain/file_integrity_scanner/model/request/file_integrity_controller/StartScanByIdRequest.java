package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_integrity_controller;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * Immutable data carrier for starting a scan by directory ID.
 *
 * This record contains an identifier (ID) of the directory to be scanned,
 * making it easy to pass this information between different layers of the
 * application in a thread-safe manner.
 */
@Schema(description = "Represents a request to start a scan by directory ID.")
public record StartScanByIdRequest(
        /**
         * The unique identifier for the monitored directory to be scanned.
         * Cannot be null. If null, an exception may be thrown during validation or
         * processing.
         */
        @Schema(description = "The unique identifier of the directory to be scanned", example = "12345") Integer id) {

    /**
     * Constructs a new instance of {@code StartScanByIdRequest} with the specified
     * directory ID.
     *
     * @param id The unique identifier for the monitored directory to be scanned.
     *           Cannot be null. If null, an exception may be thrown during
     *           validation or processing.
     */
    public StartScanByIdRequest(Integer id) {
        Objects.requireNonNull(id, "Directory ID cannot be null");
        this.id = id;
    }
}