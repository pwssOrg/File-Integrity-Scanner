package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_integrity_controller;

import jakarta.annotation.Nullable;

//TODO: Add Java Docs
public record IntegrityDiffByScanRequest(Integer scanId, int limit, @Nullable String sortField, boolean ascending) {

    /**
     * Constructs a new {@code IntegrityDiffByScanRequest} with the specified
     * parameters.
     *
     * @param scanId    The Id for the Scan
     * @param limit     the maximum number of results to return
     * @param sortField the field by which to sort results (e.g., "baseline",
     *                  "integrityFail").
     *                  If this parameter is null, it defaults to "baseline".
     * @param ascending true for ascending order sorting, false for descending
     *                  order sorting
     */
    public IntegrityDiffByScanRequest(Integer scanId, int limit, String sortField, boolean ascending) {

        this.scanId = scanId;
        this.limit = limit;
        this.ascending = ascending;

        // Set default value for sortField if it is null
        this.sortField = (sortField == null) ? "baseline" : sortField;
    }

}