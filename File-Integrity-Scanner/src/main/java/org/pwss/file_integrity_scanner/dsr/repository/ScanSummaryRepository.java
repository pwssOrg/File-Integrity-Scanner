package org.pwss.file_integrity_scanner.dsr.repository;

import org.pwss.file_integrity_scanner.dsr.domain.entities.scan_summary.ScanSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScanSummaryRepository extends JpaRepository<ScanSummary, Integer> {
}
