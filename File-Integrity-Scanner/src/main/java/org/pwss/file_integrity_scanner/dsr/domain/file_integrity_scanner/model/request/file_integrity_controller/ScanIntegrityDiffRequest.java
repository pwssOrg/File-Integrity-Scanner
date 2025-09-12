package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_integrity_controller;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;

/**
 * Represents a request to retrieve integrity differences by scan.
 * This class is used to encapsulate parameters for querying
 * integrity diffs based on a specific scan, with options to limit results,
 * sort by a specified field, and determine the sorting order.
 */
@Schema(description = "Represents a request to retrieve integrity differences by scan.")
public record ScanIntegrityDiffRequest( /**
                                         * The ID of the scan for which to retrieve integrity differences.
                                         */
@Schema(description = "The ID of the scan", example = "123") Integer scanId,

        /**
         * The maximum number of results to return.
         */
        @Schema(description = "The maximum number of results to return", example = "50") int limit,

        /**
         * The field by which to sort results (e.g., "baseline", "integrityFail").
         * If this parameter is null, it defaults to "baseline".
         */
        @Nullable @Schema(description = "The field by which to sort results", example = "baseline") String sortField,

        /**
         * A flag indicating the sorting order. True for ascending order,
         * false for descending order.
         */
        @Schema(description = "True for ascending order, false for descending order", example = "true") boolean ascending) {

    /**
     * Constructs a new {@code ScanIntegrityDiffRequest} with the specified
     * parameters.
     *
     * @param scanId    The ID for the Scan
     * @param limit     The maximum number of results to return
     * @param sortField The field by which to sort results (e.g., "baseline",
     *                  "integrityFail").
     *                  If this parameter is null, it defaults to "baseline".
     * @param ascending True for ascending order sorting, false for descending order
     *                  sorting
     */
    public ScanIntegrityDiffRequest(Integer scanId, int limit, String sortField, boolean ascending) {

        this.scanId = scanId;
        this.limit = limit;
        this.ascending = ascending;

        // Set default value for sortField if it is null
        this.sortField = (sortField == null) ? "baseline" : sortField;
    }
}