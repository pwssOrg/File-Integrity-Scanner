package org.pwss.file_integrity_scanner.exception.license;

import org.pwss.file_integrity_scanner.exception.PWSSbaseException;

/**
 * Exception thrown when license validation fails.
 *
 * This exception is used to indicate that a license key provided during
 * authentication has
 * failed validation,
 * preventing further access or operations within the application.
 */
public final class LicenseValidationFailedException extends PWSSbaseException {

    /**
     * Constructs a new {@code LicenseValidationFailedException} with the specified
     * detail
     * message.
     *
     * @param message The detailed message explaining why license validation failed.
     */
    public LicenseValidationFailedException(String message) {
        super(message);
    }

}
