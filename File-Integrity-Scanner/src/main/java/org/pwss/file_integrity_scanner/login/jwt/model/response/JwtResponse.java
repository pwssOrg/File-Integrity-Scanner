package org.pwss.file_integrity_scanner.login.jwt.model.response;

import java.io.Serializable;

public final class JwtResponse implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final String token;
    public JwtResponse(String token) {
        this.token = token;
    }
    public final String getToken() {
        return token;
    }
}
