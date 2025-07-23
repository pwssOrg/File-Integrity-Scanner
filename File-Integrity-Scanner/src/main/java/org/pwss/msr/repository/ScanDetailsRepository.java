package org.pwss.msr.repository;

import org.pwss.msr.domain.model.entities.scan_details.ScanDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScanDetailsRepository extends JpaRepository<ScanDetails, Integer> {
}
