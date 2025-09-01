package org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner.scan_summary;

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

    /**
     * Finds all scan summaries associated with a specific file.
     *
     * @param file the {@link File} entity for which to retrieve scan summaries
     * @return a list of {@link ScanSummary} entities associated with the given
     *         file,
     *         or an empty list if no scan summaries are found for the specified
     *         file
     */
    List<ScanSummary> findByFile(File file);

    /**
     * Finds all scan summaries associated with a specific scan.
     *
     * @param scan the {@link Scan} entity for which to retrieve scan summaries
     * @return a list of {@link ScanSummary} entities associated with the given
     *         scan,
     *         or an empty list if no scan summaries are found for the specified
     *         scan
     */
    List<ScanSummary> findByScan(Scan scan);

    //TODO: Add Java Docs
    List<ScanSummary> findByFileAndScan_isBaselineScanTrue(File file);
}
