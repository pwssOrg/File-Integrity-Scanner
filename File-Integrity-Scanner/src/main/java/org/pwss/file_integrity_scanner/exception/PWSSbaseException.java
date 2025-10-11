package org.pwss.file_integrity_scanner.exception;

/**
 * Base exception class for exceptions in the PWSS system.
 * All custom exceptions should extend this class to ensure consistency.
 * @author PWSS ORG
 */
public abstract class PWSSbaseException extends Exception {

    /**
     * Constructs a new {@code PWSSbaseException} with no detail message.
     */
    public PWSSbaseException() {
        super();
    }

    /**
     * Constructs a new {@code PWSSbaseException} with the specified detail message.
     *
     * @param message The detail message (which is saved for later retrieval by the
     *                {@link #getMessage()} method).
     */
    public PWSSbaseException(String message) {
        super(message + "\nPWSS @Exception");
    }

    /**
     * Constructs a new PWSSbaseException with the specified cause and a
     * detail message of
     * <tt>(cause==null ? null : cause.toString())</tt> (which typically contains
     * the class and detail message
     * of <tt>cause</tt>).
     *
     * @param cause The cause (which is saved for later retrieval by the getCause
     *              method). A null value is
     *              permitted,
     *              and indicates that the cause is nonexistent or unknown.
     */
    public PWSSbaseException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new PWSSbaseException with the specified detail message
     * and cause.
     *
     * @param message The detail message (which is saved for later retrieval by the
     *                getMessage method).
     * @param cause   The cause (which is saved for later retrieval by the getCause
     *                method). A null value is
     *                permitted,
     *                and indicates that the cause is nonexistent or unknown.
     */
    public PWSSbaseException(String message, Throwable cause) {
        super(message, cause);
    }

}
