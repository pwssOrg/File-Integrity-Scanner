package org.pwss.file_integrity_scanner.login.jwt.model.request;

import java.io.Serial;
import java.io.Serializable;


public final class JwtRequest implements Serializable {
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 2636936156391265891L;
    private String username;
    private String password;
    public JwtRequest() {
    }
    public JwtRequest(String username, String password) {
        super();
        this.username = username; this.password = password;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}