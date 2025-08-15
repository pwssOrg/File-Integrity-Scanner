package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.scan.enumeration;

/**
 * Enumeration representing the status of a scan.
 */
public enum ScanStatus {
    /**
     * The scan is currently in progress.
     */
    IN_PROGRESS("IN_PROGRESS"),

    /**
     * The scan has completed successfully.
     */
    COMPLETED("COMPLETED"),

    /**
     * The scan has been cancelled before completion.
     */
    CANCELLED("CANCELLED"),

    /**
     * The scan has failed due to an error.
     */
    FAILED("FAILED");

    /**
     * The string value representing the status.
     */
    private final String value;

    /**
     * Constructs a ScanStatus with the specified string representation.
     *
     * @param value the string representation of the status
     */
    ScanStatus(String value) {
        this.value = value;
    }

    /**
     * Returns the string representation of this scan status.
     *
     * @return the string representation of the scan status
     */
    @Override
    public String toString() {
        return value;
    }
}
