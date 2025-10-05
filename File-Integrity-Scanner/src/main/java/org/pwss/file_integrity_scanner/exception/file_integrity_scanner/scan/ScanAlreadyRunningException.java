package org.pwss.file_integrity_scanner.exception.file_integrity_scanner.scan;

import java.io.Serial;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan.Scan;

/**
 * Exception thrown when a Scan is already ongoing when starting a new Scan
 */
public final class ScanAlreadyRunningException extends Exception {

    @Serial
    private static final long serialVersionUID = 2L;

    /**
     * Default constructor for ScanAlreadyRunningException. No message or cause
     * specified.
     */
    public ScanAlreadyRunningException() {
        super();
    }

    /**
     * Constructs a new ScanAlreadyRunningException with the specified detail
     * message.
     *
     * @param message The detail message (which is saved for later retrieval by the
     *                getMessage method).
     */
    public ScanAlreadyRunningException(String message) {
        super(message + " \nPWSS @Exception");
    }

    public ScanAlreadyRunningException(String message, Scan scan) {
        super(message + " \nPWSS @Exception\n" + scan.getScanTime());
    }

    /**
     * Constructs a new ScanAlreadyRunningException with the specified cause and a
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
    public ScanAlreadyRunningException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new ScanAlreadyRunningException with the specified detail
     * message
     * and cause.
     *
     * @param message The detail message (which is saved for later retrieval by the
     *                getMessage method).
     * @param cause   The cause (which is saved for later retrieval by the getCause
     *                method). A null value is
     *                permitted,
     *                and indicates that the cause is nonexistent or unknown.
     */
    public ScanAlreadyRunningException(String message, Throwable cause) {
        super(message, cause);
    }

}
