package org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan_summary.ScanSummary;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link ScanSummary} entities.
 *
 * This interface extends {@link JpaRepository}, providing standard CRUD
 * operations
 * to interact with the database table that stores scan summary information.
 */
public interface ScanSummaryRepository extends JpaRepository<ScanSummary, Integer> {
}
