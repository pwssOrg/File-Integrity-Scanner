package org.pwss.file_integrity_scanner.dsr.domain.user_login.model.request;

/**
 * 
 * @param username
 * @param password
 */
public record LoginRequest(String username,String password) {

    public final String getUsername(){

        return this.username;
    }

    public final String getPassword(){

        return this.password;
    }
}