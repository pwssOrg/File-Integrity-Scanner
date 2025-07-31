package org.pwss.file_integrity_scanner.msr.service.scan_summary;

import org.pwss.file_integrity_scanner.msr.domain.model.entities.scan_summary.ScanSummary;

public interface ScanSummaryService {
    /**
     * Saves the given scan summary entity to the database.
     *
     * @param scanSummary the scan summary entity to save
     */
    void save(ScanSummary scanSummary);
}
