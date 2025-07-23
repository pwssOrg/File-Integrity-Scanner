package org.pwss.msr.repository;

import org.pwss.msr.domain.model.entities.file.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
