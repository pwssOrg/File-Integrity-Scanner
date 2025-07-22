package org.pwss.msr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitoredDirectoryRepository extends JpaRepository<org.pwss.msr.domain.model.entities.MonitoredDirectory, Integer> {
}
