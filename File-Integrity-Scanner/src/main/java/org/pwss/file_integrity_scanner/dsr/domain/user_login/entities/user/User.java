package org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.user;

import org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.auth.Auth;
import org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.time.Time;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity(name = "user_")
public class User {

    public User() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(name = "username", nullable = false, unique = true, updatable = false)
    public String username;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "auth_id")
    public Auth auth;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_time")
    public Time time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
