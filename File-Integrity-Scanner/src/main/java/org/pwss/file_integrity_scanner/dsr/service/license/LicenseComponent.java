package org.pwss.file_integrity_scanner.dsr.service.license;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

/**
 * Component responsible for validating license keys.
 *
 * This component provides functionality to verify whether a given license key
 * matches any of
 * the stored license hashes.
 */
@Component
final class LicenseComponent {

    /**
     * Default constructor for LicenseComponent. Package-private (no modifier) to
     * comply with
     * Spring's requirements.
     */
    LicenseComponent() {
    }

    /**
     * Validates the provided license key against a set of stored license data.
     *
     * This method calculates the SHA-256 hash of the input license key and compares
     * it to each
     * hash in the
     * licenseKeyData array. If any match is found, it returns true; otherwise, it
     * returns
     * false.
     *
     * @param licenseKeyInput The license key string to be validated.
     * @param licenceKeyData  An array of strings containing precomputed SHA-256
     *                        hashes of valid
     *                        license keys.
     * @return {@code true} if the input license key matches any hash in
     *         licenseKeyData, {@code
    false}  otherwise.
     * @throws NoSuchAlgorithmException If the SHA-256 algorithm is not available in
     *                                  the
     *                                  environment.
     */
    final boolean validateLicense(String licenseKeyInput, String[] licenceKeyData) throws NoSuchAlgorithmException {

        for (String s : licenceKeyData) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(licenseKeyInput.getBytes());
            String calculatedHash = bytesToHex(hashBytes);

            if (s.equals(calculatedHash))
                return true;
        }

        return false;

    }

    private final String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
