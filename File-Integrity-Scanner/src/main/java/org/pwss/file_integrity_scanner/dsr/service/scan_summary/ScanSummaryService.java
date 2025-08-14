package org.pwss.file_integrity_scanner.dsr.service.scan_summary;

import org.pwss.file_integrity_scanner.dsr.domain.entities.scan_summary.ScanSummary;

public interface ScanSummaryService {
    /**
     * Saves a scanSummary entity to the database.
     *
     * @param scanSummary the scan summary entity to save
     */
    void save(ScanSummary scanSummary);
}
