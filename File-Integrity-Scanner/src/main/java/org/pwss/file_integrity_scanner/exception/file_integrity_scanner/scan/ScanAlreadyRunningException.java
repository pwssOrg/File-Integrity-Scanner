package org.pwss.file_integrity_scanner.exception.file_integrity_scanner.scan;

import java.io.Serial;


import org.pwss.file_integrity_scanner.exception.PWSSbaseException;

/**
 * Exception thrown when a Scan is already ongoing when starting a new Scan.
 */
public final class ScanAlreadyRunningException extends PWSSbaseException {
    @Serial
    private static final long serialVersionUID = 3L;
}
