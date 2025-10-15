package org.pwss.file_integrity_scanner.exception.file_integrity_scanner.file;

import java.io.Serial;

import org.pwss.file_integrity_scanner.exception.PWSSbaseException;

/**
 * Exception thrown when file unquarantine operation fails.
 */
public final class UnquarantineFailedException extends PWSSbaseException {

    @Serial
    private static final long serialVersionUID = 2L;

    /**
     * Constructs a new {@code UnquarantineFailedException} with the specified
     * detail message and file path.
     *
     * @param message The detail message (which is saved for later retrieval by the
     *                {@link #getMessage()} method).
     * @param path    The keyPath String where the error occurred, which will be
     *                included in the exception message.
     */
    public UnquarantineFailedException(String message, String keyPath) {
        super(message + "\nPath with error: " + keyPath);
    }

    /**
     * Constructs a new {@code UnquarantineFailedException} by copying an existing
     * exception.
     *
     * @param cause The exception to copy, which will be used
     *              as the cause for the new
     *              exception.
     */
    public UnquarantineFailedException(UnquarantineFailedException cause) {
        super(cause);
    }
}
