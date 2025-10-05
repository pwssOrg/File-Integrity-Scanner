package org.pwss.file_integrity_scanner.exception.file_integrity_scanner.file;

import java.io.Serial;

/**
 * Exception thrown when file unquarantine operation fails.
 */
public final class UnquarantineFailedException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@code UnquarantineFailedException} with no detail message.
     */
    public UnquarantineFailedException() {
        super();
    }

    /**
     * Constructs a new {@code UnquarantineFailedException} with the specified detail message.
     *
     * @param message The detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public UnquarantineFailedException(String message) {
        super(message + " \nPWSS @Exception");
    }

    /**
     * Constructs a new {@code UnquarantineFailedException} with the specified detail message and file path.
     *
     * @param message The detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param path    The keyPath String where the error occurred, which will be included in the exception message.
     */
    public UnquarantineFailedException(String message, String keyPath) {
        super(message + " \nPWSS @Exception\nPath with error: " + keyPath);
    }

    /**
     * Constructs a new {@code UnquarantineFailedException} with the specified cause.
     *
     * @param cause The cause (which is saved for later retrieval by the {@link #getCause()} method).
     */
    public UnquarantineFailedException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new {@code UnquarantineFailedException} with the specified detail message and cause.
     *
     * @param message The detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause   The cause (which is saved for later retrieval by the {@link #getCause()} method).
     */
    public UnquarantineFailedException(String message, Throwable cause) {
        super(message + " \nPWSS @Exception", cause);
    }
}
