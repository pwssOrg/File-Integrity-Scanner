package org.pwss.file_integrity_scanner.msr.domain.model.entities.scan;

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

    private final String value;

    ScanStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
