package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.scan_summary;

import java.util.List;
import java.util.Optional;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.file.File;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan_summary.ScanSummary;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.history_controller.GetSummaryForFileRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.history_controller.GetSummaryForScanRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.history_controller.SearchForFileRequest;

/**
 * The ScanSummaryService interface provides methods to manage scan summary
 * entities.
 * It includes operations for saving scan summaries, retrieving them based on
 * specific
 * criteria (file or scan), and obtaining the most recent scan summaries.
 */
public interface ScanSummaryService {
    /**
     * Saves a scanSummary entity to the database.
     *
     * @param scanSummary the scan summary entity to save
     */
    void save(ScanSummary scanSummary);

    /**
     * Retrieves scan summaries for a specific file based on the given request.
     *
     * @param request the request containing criteria to filter scan summaries for a
     *                file
     * @return a list of scan summaries that match the criteria
     * @throws SecurityException if there is a security issue during retrieval
     */
    List<ScanSummary> getScanSummaryForFile(GetSummaryForFileRequest request) throws SecurityException;

    /**
     * Retrieves scan summaries for a specific scan based on the given request.
     *
     * @param request the request containing criteria to filter scan summaries for a
     *                scan
     * @return a list of scan summaries that match the criteria
     * @throws SecurityException if there is a security issue during retrieval
     */
    List<ScanSummary> getScanSummaryForScan(GetSummaryForScanRequest request) throws SecurityException;

    /**
     * Retrieves the most recent scan and a summary for it.
     *
     * @return a list of summaries of the most recent scan
     */
    List<ScanSummary> getMostRecentScanSummary();

    /**
     * Searches for files by their basename using a case-insensitive search.
     *
     * @param request the search criteria encapsulated in a
     *                {@link SearchForFileRequest}
     *                object, which includes parameters like search query, limit,
     *                sort field,
     *                and sorting order (ascending/descending)
     * @return a list of {@link File} entities that match the search criteria
     * @throws SecurityException if validation of the request fails
     */
    List<File> findFilesByBasenameLikeIgnoreCase(SearchForFileRequest request) throws SecurityException;

    /**
     * Finds the scan summary with the highest ID among those where the scan is
     * marked as baseline and associated with
     * a specific file.
     *
     * This method retrieves all scan summaries that match the given file and have
     * their scan's `isBaselineScan`
     * property set to true,
     * then returns the one with the highest ID if any are found. If no such scan
     * summaries exist, it returns an empty
     * optional.
     *
     * @param file the {@link File} entity for which to retrieve the baseline scan
     *             summary
     * @return an {@link Optional} containing the {@link ScanSummary} with the
     *         highest ID among those that match the
     *         criteria,
     *         or an empty {@link Optional} if no such scan summaries are found
     * @throws SecurityException if there is a security violation while accessing
     *                           the repository
     */
    Optional<ScanSummary> findScanSummmarWithHighestIdWhereScanBaselineIsSetToTrue(File file) throws SecurityException;

}
