package org.pwss.file_integrity_scanner.dsr.service.license;

/**
 * Service interface for managing license operations.
 * This interface defines methods for validating license keys, which will be
 * implemented by a
 * service class.
 */
 interface LicenseService {

    /**
     * Validates the provided license key against stored values in the repository.
     *
     * @param licenseKey The license key to be validated.
     * @return {@code true} if the validation is successful, otherwise
     *         {@code false}.
     */
    boolean validateLicenseKey(final String licenseKey);

}
