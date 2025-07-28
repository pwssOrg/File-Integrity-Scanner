package org.pwss.file_integrity_scanner.msr.repository;

import org.pwss.file_integrity_scanner.msr.domain.model.entities.checksum.Checksum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChecksumRepository extends JpaRepository<Checksum, Long> {
}
