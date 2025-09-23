package org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.auth;

import org.pwss.file_integrity_scanner.dsr.domain.PWSSbaseEntity;
import org.pwss.file_integrity_scanner.dsr.domain.mixed.entities.time.Time;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

/**
 * Represents the Auth entity which stores authentication information.
 */
@Entity(name = "auth")
public class Auth extends PWSSbaseEntity {

    /**
     * Default constructor for the Auth entity.
     */
    public Auth() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "hash", nullable = false)
    private String hash;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "auth_time", nullable = false)
    private Time time;

    /**
     * Gets the authentication hash associated with this entry.
     *
     * @return The authentication hash.
     */
    public String getHash() {
        return hash;
    }

    /**
     * Sets the authentication hash for this entry.
     *
     * @param hash The hash to set.
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * Gets the unique identifier of this auth entry.
     *
     * @return The ID of the auth entry.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this auth entry.
     *
     * @param id The ID to set.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the time associated with this authentication entry.
     *
     * @return The time object.
     */
    public Time getTime() {
        return time;
    }

    /**
     * Sets the time for this authentication entry.
     *
     * @param time The time to set.
     */
    public void setTime(Time time) {
        this.time = time;
    }

    @Override
    protected String getDBSection() {
        return USER_LOGIN;
    }
}
