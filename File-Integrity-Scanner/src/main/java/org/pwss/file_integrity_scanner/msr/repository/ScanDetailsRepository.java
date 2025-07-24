package org.pwss.file_integrity_scanner.msr.repository;

import org.pwss.file_integrity_scanner.msr.domain.model.entities.scan_details.ScanDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScanDetailsRepository extends JpaRepository<ScanDetails, Integer> {
}
