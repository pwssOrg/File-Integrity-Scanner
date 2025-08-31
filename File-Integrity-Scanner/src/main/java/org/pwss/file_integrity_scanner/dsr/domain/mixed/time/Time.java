package org.pwss.file_integrity_scanner.dsr.domain.mixed.time;

import java.time.OffsetDateTime;

import org.pwss.file_integrity_scanner.dsr.domain.PWSSbaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Entity representing time records in the database.
 */
@Entity(name = "time")
public class Time extends PWSSbaseEntity {

    /**
     * Constructs a new Time object with specified creation and update timestamps.
     *
     * @param created The timestamp when this record was created
     * @param updated The timestamp when this record was last updated
     */
    public Time(OffsetDateTime created, OffsetDateTime updated) {
        this.created = created;
        this.updated = updated;

    }

    /**
     * Default constructor.
     */
    public Time() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created", nullable = false)
    private OffsetDateTime created;

    @Column(name = "updated", nullable = false)
    private OffsetDateTime updated;

    /**
     * Gets the unique identifier of this time record.
     *
     * @return The ID of this time record
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of this time record.
     *
     * @param id The ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the timestamp when this record was created.
     *
     * @return The creation timestamp
     */
    public OffsetDateTime getCreated() {
        return created;
    }

    /**
     * Sets the timestamp when this record was created.
     *
     * @param created The creation timestamp to set
     */
    public void setCreated(OffsetDateTime created) {
        this.created = created;
    }

    /**
     * Sets the timestamp when this record was created.
     *
     * @param created The creation timestamp to set
     */
    public OffsetDateTime getUpdated() {
        return updated;
    }

    /**
     * Sets the timestamp when this record was last updated.
     *
     * @param updated The update timestamp to set
     */
    public void setUpdated(OffsetDateTime updated) {
        this.updated = updated;
    }

    @Override
    protected String getDBSection() {
        return MIXED;
    }

}