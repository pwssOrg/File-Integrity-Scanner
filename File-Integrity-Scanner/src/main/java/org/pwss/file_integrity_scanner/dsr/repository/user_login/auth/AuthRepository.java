package org.pwss.file_integrity_scanner.dsr.repository.user_login.auth;

import org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.auth.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {

}