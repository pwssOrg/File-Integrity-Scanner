package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.scan_summary;

import java.util.List;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan_summary.ScanSummary;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.history_controller.GetSummaryForFileRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.history_controller.GetSummaryForScanRequest;

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

}
