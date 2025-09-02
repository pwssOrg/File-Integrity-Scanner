package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_integrity_controller;

//TODO: Add Java Docs
public record FindXmostRecentScansRequest(int nrOfScans) {

    /**
     * Constructs a new {@code FindXmostRecentScansRequest} with the specified
     * parameters.
     * 
     * @param nrOfScans the number of most recent scans to return
     */
    public FindXmostRecentScansRequest(int nrOfScans) {

        this.nrOfScans = nrOfScans;

    }

    // TODO: Add Java Docs
    public final boolean getAscending() {
        return false;
    }

    public final String getSortField() {

        return "id";
    }

}