package org.pwss.file_integrity_scanner.exception.file_integrity_scanner;



/**
 * Exception thrown when there are no active monitored directories.
 */
public final class NoActiveMonitoredDirectoriesException extends Exception {
    /**
     * Default constructor for NoActiveMonitoredDirectoriesException. No message or
     * cause
     * specified.
     */
    public NoActiveMonitoredDirectoriesException() {
        super();
    }

    /**
     * Constructs a new NoActiveMonitoredDirectoriesException with the specified
     * detail message.
     *
     * @param message The detail message (which is saved for later retrieval by the
     *                getMessage method).
     */
    public NoActiveMonitoredDirectoriesException(String message) {
        super(message + " \nPWSS @Exception");
    }

    /**
     * Constructs a new NoActiveMonitoredDirectoriesException with the specified
     * detail message
     * and directory ID.
     *
     * @param message     The detail message (which is saved for later retrieval by
     *                    the getMessage method).
     * @param directoryId The ID of the monitored directory (for additional
     *                    context).
     */
    public NoActiveMonitoredDirectoriesException(String message, Integer directoryId) {
        super(message + " \nPWSS @Exception\nId for monitored directory: " + directoryId);
    }

    
    /**
     * Constructs a new NoActiveMonitoredDirectoriesException with the specified
     * cause and a
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
    public NoActiveMonitoredDirectoriesException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new NoActiveMonitoredDirectoriesException with the specified
     * detail message
     * and cause.
     *
     * @param message The detail message (which is saved for later retrieval by the
     *                getMessage method).
     * @param cause   The cause (which is saved for later retrieval by the getCause
     *                method). A null value is
     *                permitted,
     *                and indicates that the cause is nonexistent or unknown.
     */
    public NoActiveMonitoredDirectoriesException(String message, Throwable cause) {
        super(message, cause);
    }

}
