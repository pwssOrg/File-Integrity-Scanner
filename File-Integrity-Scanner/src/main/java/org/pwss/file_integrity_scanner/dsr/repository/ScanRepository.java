package org.pwss.file_integrity_scanner.dsr.repository;

import org.pwss.file_integrity_scanner.dsr.domain.entities.scan.Scan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScanRepository extends JpaRepository<Scan, Integer> {
}
