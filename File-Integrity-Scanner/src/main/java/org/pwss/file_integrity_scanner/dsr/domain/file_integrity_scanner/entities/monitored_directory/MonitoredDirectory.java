package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.monitored_directory;

import java.time.OffsetDateTime;

import org.pwss.file_integrity_scanner.dsr.domain.PWSSbaseEntity;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.note.Note;
import org.pwss.file_integrity_scanner.dsr.domain.mixed.time.Time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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

    @OneToOne(optional = false)
    @JoinColumn(name = "time_id", nullable = false)
    private Time addedAt;

    @Column(name = "last_scanned", nullable = true)
    private OffsetDateTime lastScanned;

    @OneToOne(optional = false)
    @JoinColumn(name = "note_id", nullable = true)
    private Note notes;

    @Column(name = "baseline_established", nullable = false)
    private Boolean baselineEstablished;

    @Column(name = "include_subdirectories", nullable = false)
    private Boolean includeSubdirectories;

    /**
     * Default constructor.
     */
    public MonitoredDirectory() {
    }

    /**
     * Constructs a new MonitoredDirectory with specified attributes.
     * The baselineEstablished variable is initialized to false.
     *
     * @param path                  The path to be monitored.
     * @param isActive              A boolean indicating whether the directory
     *                              should be active (true) or inactive
     *                              (false).
     * @param includeSubdirectories A boolean indicating whether subdirectories
     *                              should be included (true) or not
     *                              (false).
     * @param addedAt               The date and time when this MonitoredDirectory
     *                              was created.
     */
    public MonitoredDirectory(String path, Boolean isActive, Boolean includeSubdirectories, Time addedAt, Note note) {
        this.path = path;
        this.isActive = isActive;
        this.includeSubdirectories = includeSubdirectories;

        this.baselineEstablished = false;
        this.addedAt = addedAt;
        this.notes = note;
    }

    /**
     * The unique identifier for the monitored directory.
     *
     * @return the ID of this directory
     */
    public Integer getId() {
        return id;
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
    public Time getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Time addedAt) {
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
    public Note getNotes() {
        return notes;
    }

    public void setNotes(Note notes) {
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
    
    public boolean areNotesPresent(){

        return this.notes == null ? false: true; 
    }

    @Override
    protected String getDBSection() {
        return FIS;
    }

}