package org.pwss.file_integrity_scanner.msr.service.scan_summary;

import org.pwss.file_integrity_scanner.msr.domain.model.entities.scan_summary.ScanSummary;

public interface ScanSummaryService {
    void save(ScanSummary scanSummary);
}
