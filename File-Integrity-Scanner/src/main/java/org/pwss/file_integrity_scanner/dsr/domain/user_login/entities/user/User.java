package org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.user;

import org.pwss.file_integrity_scanner.dsr.domain.PWSSbaseEntity;
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

/**
 * Entity representing a user in the system.
 */
@Entity(name = "user_")
public class User extends PWSSbaseEntity {

    /**
     * Default constructor for the User entity.
     */
    public User() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", nullable = false, unique = true, updatable = false)
    private String username;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "auth_id")
    private Auth auth;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_time")
    private Time time;

    /**
     * Gets the unique identifier of the user.
     *
     * @return The ID of the user.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the user.
     *
     * @param id The ID to set for the user.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the username of the user.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username The username to set for the user.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the authentication details of the user.
     *
     * @return The authentication object associated with the user.
     */
    public Auth getAuth() {
        return auth;
    }

    /**
     * Sets the authentication details for the user.
     *
     * @param auth The authentication object to set for the user.
     */
    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    /**
     * Gets the time details of the user.
     *
     * @return The time object associated with the user.
     */
    public Time getTime() {
        return time;
    }

    /**
     * Sets the time details for the user.
     *
     * @param time The time object to set for the user.
     */
    public void setTime(Time time) {
        this.time = time;
    }

    @Override
    protected String getDBSection() {
        return USER_LOGIN;
    }
}
