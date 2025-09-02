package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan;

import org.pwss.file_integrity_scanner.dsr.domain.PWSSbaseEntity;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.monitored_directory.MonitoredDirectory;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.note.Note;
import org.pwss.file_integrity_scanner.dsr.domain.mixed.time.Time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Entity class representing a scan in the file integrity scanner.
 */
@Entity
@Table(name = "scan")
public class Scan extends PWSSbaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The time when the scan was performed.
     */
    @OneToOne(optional = false)
    @JoinColumn(name = "scan_time_id", nullable = false)
    private Time scanTime;

    /**
     * The status of the scan (e.g., completed, failed).
     */
    @Column(nullable = false)
    private String status;

    /**
     * Additional notes about the scan.
     */
    @OneToOne(optional = false)
    @JoinColumn(name = "note_id", nullable = true)
    private Note notes;

    /**
     * The monitored directory associated with this scan.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "monitored_directory_id", nullable = false)
    private MonitoredDirectory monitoredDirectory;

    // TODO: Add Java Docs
    @Column(name = "is_baseline_scan", nullable = false)
    private Boolean isBaselineScan;

    /**
     * Default constructor for creating an empty {@link Scan}.
     *
     * This constructor initializes a new instance of {@link Scan} with default
     * values
     * (null or zero for all fields). It is used by persistence
     * framework JPA.
     */
    public Scan() {
    }


    //TODO: Add Java Docs that reflects the new constructor parameter

    /**
     * Constructs a new {@link Scan} instance with the specified scan time, status,
     * and monitored directory.
     *
     * @param scanTime           the time when this scan was initiated or performed
     * @param status             the current status of the scan (e.g.,
     *                           "IN_PROGRESS", "COMPLETED", etc.)
     * @param monitoredDirectory the {@link MonitoredDirectory} that was scanned
     *                           during this scan
     */
    public Scan(Time scanTime, String status, 
    MonitoredDirectory monitoredDirectory,
    Note notes,
    Boolean isBaseLineScan) {
        this.scanTime = scanTime;
        this.status = status;
        this.monitoredDirectory = monitoredDirectory;
        this.notes = notes;
        this.isBaselineScan = isBaseLineScan;
    }

    /**
     * The unique identifier for this scan entity.
     *
     * @return the ID of the scan
     */
    public Integer getId() {
        return id;
    }

    /**
     * Gets the time when the scan was performed.
     *
     * @return the scan time
     */
    public Time getScanTime() {
        return scanTime;
    }

    /**
     * Sets the time when the scan was performed.
     *
     * @param scanTime the scan time to set
     */
    public void setScanTime(Time scanTime) {
        this.scanTime = scanTime;
    }

    /**
     * Gets the status of the scan.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the scan.
     *
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets additional notes about the scan.
     *
     * @return the notes
     */
    public Note getNotes() {
        return notes;
    }

    /**
     * Sets additional notes about the scan.
     *
     * @param notes the notes to set
     */
    public void setNotes(Note notes) {
        this.notes = notes;
    }

    /**
     * Gets the monitored directory associated with this scan.
     *
     * @return the monitored directory
     */
    public MonitoredDirectory getMonitoredDirectory() {
        return monitoredDirectory;
    }

    /**
     * Sets the monitored directory associated with this scan.
     *
     * @param monitoredDirectory the monitored directory to set
     */
    public void setMonitoredDirectory(MonitoredDirectory monitoredDirectory) {
        this.monitoredDirectory = monitoredDirectory;
    }

    // TODO: Add Java Docs
    public Boolean getIsBaselineScan() {
        return isBaselineScan;
    }

    // TODO Add Java Docs
    public void setIsBaselineScan(Boolean isBaselineScan) {
        this.isBaselineScan = isBaselineScan;
    }

    @Override
    protected String getDBSection() {
        return FIS;
    }
}