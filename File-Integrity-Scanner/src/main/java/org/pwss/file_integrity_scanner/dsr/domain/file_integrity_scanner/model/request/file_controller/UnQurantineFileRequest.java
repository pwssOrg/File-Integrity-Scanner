package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_controller;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents a request to unquarantine a file.
 * This class encapsulates the key path parameter required for unquarantining a
 * specific file.
 */
@Schema(description = "Represents a request to unquarantine a file.")
public record UnQurantineFileRequest(
        /**
         * The key path of the file to be unquarantined.
         *
         * @return the key path as a string
         */
        @Schema(description = "The key path of the file to be unquarantined", example = "C_drive__.myfolder.myfile.txt") String keyPath) {
}
