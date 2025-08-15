package org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan_summary.ScanSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScanSummaryRepository extends JpaRepository<ScanSummary, Integer> {
}
