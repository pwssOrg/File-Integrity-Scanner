package org.pwss.file_integrity_scanner.dsr.repository;

import org.pwss.file_integrity_scanner.dsr.domain.entities.checksum.Checksum;
import org.pwss.file_integrity_scanner.dsr.domain.entities.file.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChecksumRepository extends JpaRepository<Checksum, Long> {
    Optional<List<Checksum>> findByFile(File file);
}
