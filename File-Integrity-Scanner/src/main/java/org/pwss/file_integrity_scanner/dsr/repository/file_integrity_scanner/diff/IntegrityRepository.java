package org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner.diff;

import java.util.List;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.diff.Diff;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan.Scan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Todo: Add Java Docs
@Repository
public interface IntegrityRepository extends JpaRepository<Diff, Long> {

    List<Diff> findByIntegrityFail_Scan(Scan scan,Pageable information);

}
