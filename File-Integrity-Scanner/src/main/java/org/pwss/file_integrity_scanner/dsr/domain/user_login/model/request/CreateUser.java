package org.pwss.file_integrity_scanner.dsr.domain.user_login.model.request;

public final class CreateUser {

    public String password;
    public String username;

    public CreateUser() {
    }

    public final String getPassword() {
        return password;
    }



    public final String getUsername() {
        return username;
    }

    public final void setUsername(String username) {
        this.username = username;
    }

}