package org.pwss.file_integrity_scanner.dsr.repository.file_integrity_scanner;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.monitored_directory.MonitoredDirectory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link MonitoredDirectory} entities.
 * This interface extends Spring Data JPA's {@link JpaRepository}, providing
 * CRUD operations and custom query methods for the MonitoredDirectory entity.
 */
public interface MonitoredDirectoryRepository extends JpaRepository<MonitoredDirectory, Integer> {

    /**
     * Finds all monitored directories that are active or inactive based on the
     * given status.
     *
     * @param isActive true to find active directories, false to find inactive ones
     * @return an optional list of monitored directories matching the specified
     *         active status,
     *         which may be empty if no matches are found
     */
    Optional<List<MonitoredDirectory>> findByIsActive(boolean isActive);

   
}