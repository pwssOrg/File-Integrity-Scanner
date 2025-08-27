package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.history_controller;

/**
 * A record that encapsulates search criteria for file searches.
 *
 * This record is used to define parameters for a search operation,
 * including the search query, result limit, sort field, and sorting order
 * (ascending/descending).
 */
public record SearchForFileRequest(String searchQuery, int limit, String sortField, boolean ascending) {

    /**
     * Constructs a new {@code SearchForFileRequest} with the specified parameters.
     *
     * @param searchQuery the search string to match against file basenames. Can
     *                    contain wildcards like % and _.
     *                    Must not be null or blank, and must have a length less
     *                    than 200 characters.
     * @param limit       the maximum number of results to return
     * @param sortField   the field by which to sort results (e.g., "basename",
     *                    "createdDate").
     *                    If this parameter is null, it defaults to "basename".
     * @param ascending   true for ascending order sorting, false for descending
     *                    order sorting
     */
    public SearchForFileRequest(String searchQuery, int limit, String sortField, boolean ascending) {

        if (searchQuery == null || searchQuery.isBlank() || searchQuery.length() >= 200) {
            throw new IllegalArgumentException("Search query cannot be blank or exceed 200 characters");
        }

        this.searchQuery = searchQuery;
        this.limit = limit;
        this.ascending = ascending;

        // Set default value for sortField if it is null
        this.sortField = (sortField == null) ? "basename" : sortField;
    }

}
