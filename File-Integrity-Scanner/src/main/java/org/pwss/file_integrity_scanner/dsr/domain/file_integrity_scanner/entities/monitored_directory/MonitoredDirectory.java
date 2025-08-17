package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.monitored_directory;

import java.time.OffsetDateTime;

import org.pwss.file_integrity_scanner.dsr.domain.PWSSbaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public OffsetDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(OffsetDateTime addedAt) {
        this.addedAt = addedAt;
    }

    public OffsetDateTime getLastScanned() {
        return lastScanned;
    }

    public void setLastScanned(OffsetDateTime lastScanned) {
        this.lastScanned = lastScanned;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getBaselineEstablished() {
        return baselineEstablished;
    }

    public void setBaselineEstablished(Boolean baselineEstablished) {
        this.baselineEstablished = baselineEstablished;
    }

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