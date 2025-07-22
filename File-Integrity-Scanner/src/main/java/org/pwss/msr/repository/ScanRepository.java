package org.pwss.msr.repository;

import org.pwss.msr.domain.model.entities.Scan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScanRepository extends JpaRepository<Scan, Long> {
}
