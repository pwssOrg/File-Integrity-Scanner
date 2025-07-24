package org.pwss.file_integrity_scanner.msr.repository;

import org.pwss.file_integrity_scanner.msr.domain.model.entities.monitored_directory.MonitoredDirectory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitoredDirectoryRepository extends JpaRepository<MonitoredDirectory, Integer> {
}
