package org.pwss.file_integrity_scanner.dsr.service.user_login.auth;


import org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.auth.Auth;

public interface AuthService {
        /**
     * Saves a Auth entity to the database.
     *
     * @param auth the auth entity to save
     */
    void save(Auth auth);
}