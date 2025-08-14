package org.pwss.file_integrity_scanner.dsr.repository.user_login.user;

import java.util.Optional;

import org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@code User} entities.
 *
 * <p>
 * This interface provides CRUD operations for the {@code User} entity and
 * includes
 * custom query methods such as finding a user by their username. It extends
 * JpaRepository
 * to leverage its predefined methods for database access, such as save,
 * findById,
 * delete, and count operations.
 * </p>
 *
 * @see JpaRepository
 * @see User
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds a user by their username.
     *
     * <p>
     * This method queries the database to find a {@code User} entity that matches
     * the given username.
     * </p>
     *
     * @param username The username of the user to be found
     * @return An Optional containing the user with the specified username, or an
     *         empty Optional if no such user
     *         exists.
     */
    Optional<User> findByUsername(String username);

}