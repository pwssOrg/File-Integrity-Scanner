package org.pwss.msr.repository;

import org.pwss.msr.domain.model.entities.monitored_directory.MonitoredDirectory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitoredDirectoryRepository extends JpaRepository<MonitoredDirectory, Integer> {
}
