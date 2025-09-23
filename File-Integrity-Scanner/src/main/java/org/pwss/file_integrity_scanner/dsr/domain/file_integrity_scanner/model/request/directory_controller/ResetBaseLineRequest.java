package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.directory_controller;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A record representing a request to reset the baseline for a monitored
 * directory.
 */
@Schema(description = "Represents a request to reset the baseline for a monitored directory.")
public record ResetBaseLineRequest(
        /**
         * The unique identifier of the directory. This ID is used
         * to locate the specific directory for which the baseline needs to be reset.
         */
        @Schema(description = "The unique identifier of the directory", example = "1") Integer directoryId,

        /**
         * The code associated with the endpoint that is making this request.
         * This can be used for tracking or authorization purposes.
         */
        @Schema(description = "The code associated with the endpoint", example = "123456789") Long endpointCode) {
}