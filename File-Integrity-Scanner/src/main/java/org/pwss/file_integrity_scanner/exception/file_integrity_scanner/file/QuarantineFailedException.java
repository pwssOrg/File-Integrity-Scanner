package org.pwss.file_integrity_scanner.exception.file_integrity_scanner.file;


import java.io.Serial;

/**
 * Exception thrown when file quarantine operation fails.
 */
public final class QuarantineFailedException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@code QuarantineFailedException} with no detail message.
     */
    public QuarantineFailedException() {
        super();
    }

    /**
     * Constructs a new {@code QuarantineFailedException} with the specified detail message.
     *
     * @param message The detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public QuarantineFailedException(String message) {
        super(message + " \nPWSS @Exception");
    }

    /**
     * Constructs a new {@code QuarantineFailedException} with the specified detail message and file path.
     *
     * @param message The detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param path    The path where the error occurred, which will be included in the exception message.
     */
    public QuarantineFailedException(String message, String path) {
        super(message + " \nPWSS @Exception\nPath with error: " + path);
    }

    /**
     * Constructs a new {@code QuarantineFailedException} with the specified cause.
     *
     * @param cause The cause (which is saved for later retrieval by the {@link #getCause()} method).
     */
    public QuarantineFailedException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new {@code QuarantineFailedException} with the specified detail message and cause.
     *
     * @param message The detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause   The cause (which is saved for later retrieval by the {@link #getCause()} method).
     */
    public QuarantineFailedException(String message, Throwable cause) {
        super(message + " \nPWSS @Exception", cause);
    }
}
