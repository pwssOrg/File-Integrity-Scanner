package org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner.diff;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.diff.Diff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//TODO: Add Java Docs
@Repository
public interface IntegrityRepository extends JpaRepository<Diff, Long> {

}
