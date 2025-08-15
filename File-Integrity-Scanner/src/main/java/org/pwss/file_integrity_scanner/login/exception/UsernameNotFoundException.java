package org.pwss.file_integrity_scanner.login.exception;

/**
 * Exception thrown when a username is not found in the system.
 */
public final class UsernameNotFoundException extends Exception {

    /**
     * Default constructor for UsernameNotFoundException. No message or cause
     * specified.
     */
    public UsernameNotFoundException() {
        super();
    }

    /**
     * Constructs a new UsernameNotFoundException with the specified detail message.
     *
     * @param message The detail message (which is saved for later retrieval by the
     *                getMessage method).
     */
    public UsernameNotFoundException(String message) {
        super(message);
        super(message + " \nPWSS @Exception");
    }

    /**
     * Constructs a new UsernameNotFoundException with the specified cause and a
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
    public UsernameNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new UsernameNotFoundException with the specified detail message
     * and cause.
     *
     * @param message The detail message (which is saved for later retrieval by the
     *                getMessage method).
     * @param cause   The cause (which is saved for later retrieval by the getCause
     *                method). A null value is
     *                permitted,
     *                and indicates that the cause is nonexistent or unknown.
     */
    public UsernameNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
