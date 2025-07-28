package org.pwss.file_integrity_scanner.msr.repository;

import org.pwss.file_integrity_scanner.msr.domain.model.entities.scan.Scan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScanRepository extends JpaRepository<Scan, Integer> {
}
