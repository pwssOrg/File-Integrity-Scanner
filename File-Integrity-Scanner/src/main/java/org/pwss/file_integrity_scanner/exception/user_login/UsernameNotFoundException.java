package org.pwss.file_integrity_scanner.exception.user_login;

import java.io.Serial;

import org.pwss.file_integrity_scanner.exception.PWSSbaseException;

/**
 * Exception thrown when a username is not found in the system.
 */
public final class UsernameNotFoundException extends PWSSbaseException {

    @Serial
    private static final long serialVersionUID = 3L;
}
