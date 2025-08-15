package org.pwss.file_integrity_scanner.login.exception;

/**
 * <p>
 * Exception thrown to indicate that a provided password is incorrect.
 * </p>
 *
 * <p>
 * This exception is typically used in authentication scenarios where a username
 * (or other identifier)
 * is recognized, but the corresponding password does not match what's stored
 * for that user.
 * </p>
 *
 * @see UsernameNotFoundException
 */

public final class WrongPasswordException extends Exception {

    /**
     * Default constructor for WrongPasswordException. No message or cause
     * specified.
     */
    public WrongPasswordException() {
        super();
    }

    /**
     * Constructs a new WrongPasswordException with the specified detail message.
     *
     * @param message The detail message (which is saved for later retrieval by the
     *                getMessage method).
     */
    public WrongPasswordException(String message) {
        super(message);
    }

    /**
     * Constructs a new WrongPasswordException with the specified cause and a
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
    public WrongPasswordException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new WrongPasswordException with the specified detail message
     * and cause.
     *
     * @param message The detail message (which is saved for later retrieval by the
     *                getMessage method).
     * @param cause   The cause (which is saved for later retrieval by the getCause
     *                method). A null value is
     *                permitted,
     *                and indicates that the cause is nonexistent or unknown.
     */
    public WrongPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}