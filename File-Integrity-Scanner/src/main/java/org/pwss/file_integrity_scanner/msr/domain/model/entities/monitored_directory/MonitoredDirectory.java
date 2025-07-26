package org.pwss.file_integrity_scanner.msr.domain.model.entities.monitored_directory;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

import org.pwss.file_integrity_scanner.msr.domain.model.entities.BaseEntity;

@Entity
@Table(name = "monitored_directory")
public class MonitoredDirectory extends BaseEntity {

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
    private Boolean baselineEstablished = false;

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
}