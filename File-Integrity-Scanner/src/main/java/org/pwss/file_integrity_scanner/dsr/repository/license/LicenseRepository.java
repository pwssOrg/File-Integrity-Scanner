package org.pwss.file_integrity_scanner.dsr.repository.license;

import org.pwss.file_integrity_scanner.dsr.domain.license.entities.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LicenseRepository extends JpaRepository<License,Integer> {}
