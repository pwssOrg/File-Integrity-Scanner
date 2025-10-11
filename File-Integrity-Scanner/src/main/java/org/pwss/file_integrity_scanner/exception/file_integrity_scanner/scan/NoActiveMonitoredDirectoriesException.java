package org.pwss.file_integrity_scanner.exception.file_integrity_scanner.scan;

import java.io.Serial;

import org.pwss.file_integrity_scanner.exception.PWSSbaseException;

/**
 * Exception thrown when there are no active monitored directories.
 */
public final class NoActiveMonitoredDirectoriesException extends PWSSbaseException {

    @Serial
    private static final long serialVersionUID = 3L;

    /**
     * Constructs a new NoActiveMonitoredDirectoriesException with the specified
     * detail message and directory ID.
     *
     * @param message     The detail message (which is saved for later retrieval by
     *                    the getMessage method).
     * @param directoryId The ID of the monitored directory (for additional
     *                    context).
     */
    public NoActiveMonitoredDirectoriesException(String message, Integer directoryId) {
        super(message + "\nId for monitored directory: " + directoryId);
    }

    /**
     * Constructs a new NoActiveMonitoredDirectoriesException with the specified
     * detail message.
     *
     * @param message The detail message (which is saved for later retrieval by
     *                the getMessage method).
     */
    public NoActiveMonitoredDirectoriesException(String message) {
        super(message);
    }
}
