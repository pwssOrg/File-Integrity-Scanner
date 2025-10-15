package org.pwss.file_integrity_scanner.exception.user_login;

import org.pwss.file_integrity_scanner.exception.PWSSbaseException;

/**
 * Exception thrown when user creation fails.
 *
 * This exception is used to signal errors that occur during the process of
 * creating a new user.
 * It provides a message describing the failure, which can be used for debugging
 * and error reporting purposes.
 */
public final class CreateUserFailedException extends PWSSbaseException {

    /**
     * Constructs a new {@code CreateUserFailedException} with the specified detail
     * message.
     *
     * @param message The detail message (which is saved for later retrieval by the
     *                {@link #getMessage()} method)
     */
    public CreateUserFailedException(String message) {
        super(message);
    }
}
