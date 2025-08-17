package org.pwss.file_integrity_scanner.dsr.domain.user_login.entities.time;



import java.time.OffsetDateTime;

import org.pwss.file_integrity_scanner.dsr.domain.PWSSbaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "time")
public class Time extends PWSSbaseEntity {

    public Time(OffsetDateTime created, OffsetDateTime updated) {
        this.created = created;
        this.updated = updated;

    }

    public Time() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    @Column(name = "created", nullable = false)
    public OffsetDateTime created;

    @Column(name = "updated", nullable = false)
    public OffsetDateTime updated;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OffsetDateTime getCreated() {
        return created;
    }

    public void setCreated(OffsetDateTime created) {
        this.created = created;
    }

    public OffsetDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(OffsetDateTime updated) {
        this.updated = updated;
    }

    @Override
    protected String getDBSection() {
        return USER_LOGIN;
    }


}