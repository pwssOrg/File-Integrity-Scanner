package org.pwss.file_integrity_scanner.dsr.domain.user_login.model.request.user_controller;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * A data class representing a request to create a new user.
 */
@Schema(description = "Represents a request to create a new user.")
public record CreateUserRequest(/**
     * The username for the new user.
     */
    @Schema(description = "The Username for the New User", example = "john_doe")  @NotBlank(message = "Name must not be empty")   String username,
     /**
     * The password for the new user.
     */
    @Schema(description = "The Password for the New User", example = "securePassword123")  @Size(min = 8, message = "Password must be at least 8 characters")
    String password,
    /** licenseKeyString */
    @Schema(description = "The License Key for the File Integrity Scanner", example = "license key string") String licenseKey) {


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
     * Returns the license key associated with the user.
     *
     * This method is used to retrieve the license key that will be validated during
     * login.
     *
     * @return The license key
     */
    public final String licenseKey() {
        return licenseKey;
    }
}