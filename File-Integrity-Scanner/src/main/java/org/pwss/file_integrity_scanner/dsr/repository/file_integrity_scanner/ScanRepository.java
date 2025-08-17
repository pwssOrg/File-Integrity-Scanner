package org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan.Scan;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Scan} entities.
 *
 * This interface extends {@link JpaRepository}, providing standard CRUD
 * operations
 * to interact with the database table that stores scan information.
 */
public interface ScanRepository extends JpaRepository<Scan, Integer> {
}
