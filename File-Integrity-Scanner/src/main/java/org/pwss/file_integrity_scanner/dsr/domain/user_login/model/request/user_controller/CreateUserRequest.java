package org.pwss.file_integrity_scanner.dsr.domain.user_login.model.request.user_controller;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A data class representing a request to create a new user.
 */
@Schema(description = "Represents a request to create a new user.")
public final class CreateUserRequest {

    /**
     * The username for the new user.
     */
    @Schema(description = "The username of the new user", example = "john_doe")
    public String username;

    /**
     * The password for the new user.
     */
    @Schema(description = "The password of the new user", example = "securePassword123")
    public String password;

    /**
     * Default constructor for CreateUserRequest.
     */
    public CreateUserRequest() {
    }

    /**
     * Returns the password for the new user.
     *
     * @return The password
     */
    public final String getPassword() {
        return password;
    }

    /**
     * Returns the username for the new user.
     *
     * @return The username
     */
    public final String getUsername() {
        return username;
    }

    /**
     * Sets the username for the new user.
     *
     * @param username The username to set
     */
    public final void setUsername(String username) {
        this.username = username;
    }
}