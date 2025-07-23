package org.pwss.msr.repository;

import org.pwss.msr.domain.model.entities.checksum.Checksum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChecksumRepository extends JpaRepository<Checksum, Long> {
}
