package org.pwss.file_integrity_scanner.exception.user_login;

import java.io.Serial;

/**
 * <p>
 * Exception thrown to indicate that a provided password is incorrect.
 * </p>
 *
 * <p>
 * This exception is typically used in authentication scenarios where a username
 * (or other identifier)
 * is recognized, but the corresponding password does not match what's stored
 * for that user.
 * </p>
 *
 * @see UsernameNotFoundException
 */

public final class WrongPasswordException extends Exception {

    @Serial
    private static final long serialVersionUID = 3L;

}