package org.pwss.file_integrity_scanner.dsr.service.user_login.auth;

import org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.auth.Auth;

public interface AuthService {
    /**
     * Saves the given authentication object to the repository.
     *
     * @param auth The authentication object to be saved. Must not be null.
     */
    void save(Auth auth);

    /**
     * Checks if the repository is empty.
     *
     * @return {@code true} if the repository has no entries; {@code false}
     *         otherwise.
     */
    Boolean isEmpty();
}