package org.pwss.file_integrity_scanner.dsr.repository.mixed.time;

import org.pwss.file_integrity_scanner.dsr.domain.mixed.entities.time.Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@code Time} entities.
 *
 * <p>
 * This interface provides CRUD operations for the {@code Time} entity. It
 * extends
 * JpaRepository to leverage its predefined methods for database access, such as
 * save,
 * findById, delete, and count operations.
 * </p>
 *
 * @see JpaRepository
 * @see Time
 */
@Repository
public interface TimeRepository extends JpaRepository<Time, Long> {
}