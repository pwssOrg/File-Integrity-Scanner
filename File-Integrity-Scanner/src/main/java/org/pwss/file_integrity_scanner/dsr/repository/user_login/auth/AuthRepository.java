package org.pwss.file_integrity_scanner.dsr.repository.user_login.auth;

import org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.auth.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@code Auth} entities.
 *
 * <p>
 * This interface provides CRUD operations for the {@code Auth} entity. It
 * extends
 * JpaRepository to leverage its predefined methods for database access, such as
 * save,
 * findById, delete, and count operations.
 * </p>
 *
 * @see JpaRepository
 * @see Auth
 */
@Repository
public interface AuthRepository extends JpaRepository<Auth, Integer> {

}