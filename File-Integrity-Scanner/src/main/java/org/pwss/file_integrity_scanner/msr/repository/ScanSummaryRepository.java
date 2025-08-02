package org.pwss.file_integrity_scanner.msr.repository;

import org.pwss.file_integrity_scanner.msr.domain.model.entities.scan_summary.ScanSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScanSummaryRepository extends JpaRepository<ScanSummary, Integer> {
}
