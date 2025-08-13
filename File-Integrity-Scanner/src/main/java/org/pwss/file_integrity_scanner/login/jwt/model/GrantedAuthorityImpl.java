package org.pwss.file_integrity_scanner.login.jwt.model;

import org.springframework.security.core.GrantedAuthority;

public final class GrantedAuthorityImpl implements GrantedAuthority {

    private final String role;

    public GrantedAuthorityImpl(String role) {
        this.role = role;
    }

    @Override
    public final String getAuthority() {
        return role;
    }
}