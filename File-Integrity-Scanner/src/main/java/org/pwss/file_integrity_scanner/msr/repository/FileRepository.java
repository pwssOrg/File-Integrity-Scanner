package org.pwss.file_integrity_scanner.msr.repository;

import org.pwss.file_integrity_scanner.msr.domain.model.entities.file.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
