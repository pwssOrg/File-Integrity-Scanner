package org.pwss.file_integrity_scanner.dsr.repository.user_login.user;

import java.util.Optional;

import org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

}