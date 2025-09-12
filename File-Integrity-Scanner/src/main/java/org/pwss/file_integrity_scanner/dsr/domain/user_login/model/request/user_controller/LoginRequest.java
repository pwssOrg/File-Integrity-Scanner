package org.pwss.file_integrity_scanner.dsr.domain.user_login.model.request.user_controller;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A data class representing a request to log in a user.
 */
@Schema(description = "Represents a request to authenticate a user.")
public record LoginRequest(

        /**
         * The username for the user attempting to log in.
         */
        @Schema(description = "The username of the user", example = "john_doe") String username,

        /**
         * The password for the user attempting to log in.
         */
        @Schema(description = "The password of the user", example = "securePassword123") String password) {

    /**
     * Returns the username for the user attempting to log in.
     *
     * @return The username
     */
    public final String getUsername() {
        return this.username;
    }

    /**
     * Returns the password for the user attempting to log in.
     *
     * @return The password
     */
    public final String getPassword() {
        return this.password;
    }
}