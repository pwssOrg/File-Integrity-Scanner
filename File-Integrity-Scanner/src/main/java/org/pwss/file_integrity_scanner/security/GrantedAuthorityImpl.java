package org.pwss.file_integrity_scanner.security;

import org.springframework.security.core.GrantedAuthority;

/**
 * Implementation of the GrantedAuthority interface for Spring Security.
 * This class represents a single authority/role granted to an authentication principal.
 */
public final class GrantedAuthorityImpl implements GrantedAuthority {

    private final String role;

    /**
     * Constructs a new GrantedAuthorityImpl with the specified role.
     *
     * @param role The name of the role/authority being granted
     */
    public GrantedAuthorityImpl(String role) {
        this.role = role;
    }

    /**
     * Returns the authority granted to the authentication principal.
     * <p>
     * In Spring Security, authorities are generally represented as strings like "ROLE_USER" or "ADMIN".
     *
     * @return The name of the role/authority
     */
    @Override
    public final String getAuthority() {
        return role;
    }
}