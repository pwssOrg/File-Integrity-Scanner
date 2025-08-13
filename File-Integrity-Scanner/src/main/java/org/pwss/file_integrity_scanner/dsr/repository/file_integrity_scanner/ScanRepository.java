package org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan.Scan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScanRepository extends JpaRepository<Scan, Integer> {
}
