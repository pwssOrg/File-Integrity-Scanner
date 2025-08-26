package org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner;

import java.util.List;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.file.File;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan.Scan;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan_summary.ScanSummary;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link ScanSummary} entities.
 *
 * This interface extends {@link JpaRepository}, providing standard CRUD
 * operations
 * to interact with the database table that stores scan summary information.
 */
public interface ScanSummaryRepository extends JpaRepository<ScanSummary, Long> {

    //TODO: Add Java Docs
    List<ScanSummary> findByFile(File file);

    //TODO: Add Java Docs
    List<ScanSummary> findByScan(Scan scan);
    
}
