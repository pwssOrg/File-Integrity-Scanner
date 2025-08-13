package org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.auth;

import org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.time.Time;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;



@Entity(name = "auth")
public class Auth {

    public Auth() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(name = "hash", nullable = false)
    public String hash;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "auth_time", nullable = false)
    public Time time;


    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
