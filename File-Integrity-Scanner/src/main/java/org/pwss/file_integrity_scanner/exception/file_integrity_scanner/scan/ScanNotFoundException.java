package org.pwss.file_integrity_scanner.exception.file_integrity_scanner.scan;

import java.io.Serial;

import org.pwss.file_integrity_scanner.exception.PWSSbaseException;

/**
 * This exception is thrown when a requested Scan cannot be found in the system.
 * It extends the PWSSbaseException to provide specific handling for
 * scan-related issues.
 */
public final class ScanNotFoundException extends PWSSbaseException {
    @Serial
    private static final long serialVersionUID = 1L;
}
