package org.pwss.file_integrity_scanner.exception.file_integrity_scanner.file;

import java.io.Serial;

import org.pwss.file_integrity_scanner.exception.PWSSbaseException;

/**
 * Exception thrown when file quarantine operation fails.
 */
public final class QuarantineFailedException extends PWSSbaseException {

    @Serial
    private static final long serialVersionUID = 2L;

    /**
     * Constructs a new {@code QuarantineFailedException} with the specified detail
     * message and file path.
     *
     * @param message The detail message (which is saved for later retrieval by the
     *                {@link #getMessage()} method).
     * @param path    The path where the error occurred, which will be included in
     *                the exception message.
     */
    public QuarantineFailedException(String message, String path) {
        super(message + "\nPath with error: " + path);
    }

    /**
     * Constructs a new {@code QuarantineFailedException} with the specified detail
     * message.
     *
     * @param message The detail message (which is saved for later retrieval by the
     *                {@link #getMessage()} method).
     */
    public QuarantineFailedException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code QuarantineFailedException} with the specified cause.
     *
     * @param cause The cause (which is saved for later retrieval by the getCause
     *              method). A null value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.
     */
    public QuarantineFailedException(QuarantineFailedException cause) {
        super(cause);
    }
}
