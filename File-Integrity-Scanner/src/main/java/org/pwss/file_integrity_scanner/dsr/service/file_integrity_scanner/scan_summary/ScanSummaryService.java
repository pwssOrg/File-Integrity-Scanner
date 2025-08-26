package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.scan_summary;

import java.util.List;


import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan_summary.ScanSummary;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.history_controller.GetSummaryForFileRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.history_controller.GetSummaryForScanRequest;

public interface ScanSummaryService {
    /**
     * Saves a scanSummary entity to the database.
     *
     * @param scanSummary the scan summary entity to save
     */
    void save(ScanSummary scanSummary);

    // TODO Add Java Docs
    List<ScanSummary> getScanSummaryForFile(GetSummaryForFileRequest request) throws SecurityException; 

    // TODO Add Java Docs
    List<ScanSummary> getScanSummaryForScan(GetSummaryForScanRequest request) throws SecurityException;

    // TODO Add Java Docs
    List<ScanSummary> getMostRecentScanSummary();
}
