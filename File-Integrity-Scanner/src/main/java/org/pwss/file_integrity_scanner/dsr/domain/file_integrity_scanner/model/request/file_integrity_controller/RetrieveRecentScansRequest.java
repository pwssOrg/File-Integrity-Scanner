package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_integrity_controller;

//TODO: Add Java Docs
public record RetrieveRecentScansRequest(int nrOfScans) {

    /**
     * Constructs a new {@code FindXmostRecentScansRequest} with the specified
     * parameters.
     * 
     * @param nrOfScans the number of most recent scans to return
     */
    public RetrieveRecentScansRequest(int nrOfScans) {

        this.nrOfScans = nrOfScans;

    }

}