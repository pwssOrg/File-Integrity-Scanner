package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_integrity_controller;

/**
 * Represents a request to retrieve recent scans.
 * This class is used to encapsulate parameters for querying
 * the most recent scans, specifying how many scan results to return.
 */
public record RetrieveRecentScansRequest(int nrOfScans) {

    /**
     * Constructs a new {@code RetrieveRecentScansRequest} with the specified
     * parameters.
     *
     * @param nrOfScans The number of most recent scans to return
     */
    public RetrieveRecentScansRequest(int nrOfScans) {

        this.nrOfScans = nrOfScans;

    }

}