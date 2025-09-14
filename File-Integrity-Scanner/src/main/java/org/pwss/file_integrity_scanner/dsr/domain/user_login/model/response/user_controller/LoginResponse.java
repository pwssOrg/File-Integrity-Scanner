package org.pwss.file_integrity_scanner.dsr.domain.user_login.model.response.user_controller;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A data class representing a response to a login request.
 */
@Schema(description = "Represents the result of a user login attempt.")
public record LoginResponse(
        /**
         * Indicates whether the login was successful.
         */
        @Schema(description = "Indicates if the login was successful", example = "true") Boolean successful) {
}