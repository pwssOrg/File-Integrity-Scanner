package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.monitored_directory;

import java.time.OffsetDateTime;

import org.pwss.file_integrity_scanner.dsr.domain.PWSSbaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity representing a monitored directory.
 */
@Entity
@Table(name = "monitored_directory")
public class MonitoredDirectory extends PWSSbaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String path;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "added_at", nullable = false)
    private OffsetDateTime addedAt;

    @Column(name = "last_scanned")
    private OffsetDateTime lastScanned;

    @Column
    private String notes;

    @Column(name = "baseline_established", nullable = false)
    private Boolean baselineEstablished;

    @Column(name = "include_subdirectories", nullable = false)
    private Boolean includeSubdirectories;

    // Getters and setters

    /**
     * The unique identifier for the monitored directory.
     *
     * @return the ID of this directory
     */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * The path to the monitored directory.
     *
     * @return the path of this directory
     */
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Indicates whether the monitoring for this directory is active.
     *
     * @return true if monitoring is active, false otherwise
     */
    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * The date and time when the directory was added to monitoring.
     *
     * @return the timestamp of when this directory was added
     */
    public OffsetDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(OffsetDateTime addedAt) {
        this.addedAt = addedAt;
    }

    /**
     * The date and time of the last scan for changes in the directory.
     *
     * @return the timestamp of the last scan
     */
    public OffsetDateTime getLastScanned() {
        return lastScanned;
    }

    public void setLastScanned(OffsetDateTime lastScanned) {
        this.lastScanned = lastScanned;
    }

    /**
     * Additional notes or comments about the monitored directory.
     *
     * @return any notes associated with this directory
     */
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Indicates whether a baseline has been established for the directory.
     *
     * @return true if a baseline is established, false otherwise
     */
    public Boolean getBaselineEstablished() {
        return baselineEstablished;
    }

    public void setBaselineEstablished(Boolean baselineEstablished) {
        this.baselineEstablished = baselineEstablished;
    }

    /**
     * Indicates whether subdirectories should be included in monitoring.
     *
     * @return true if subdirectories are included, false otherwise
     */
    public Boolean getIncludeSubdirectories() {
        return includeSubdirectories;
    }

    public void setIncludeSubdirectories(Boolean includeSubdirectories) {
        this.includeSubdirectories = includeSubdirectories;
    }

    @Override
    protected String getDBSection() {
        return FIS;
    }

}