package org.pwss.file_integrity_scanner.dsr.repository.user_login.user;

import org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.user.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
}