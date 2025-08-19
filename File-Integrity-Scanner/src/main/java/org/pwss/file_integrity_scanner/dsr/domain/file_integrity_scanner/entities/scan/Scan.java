package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

import org.pwss.file_integrity_scanner.dsr.domain.PWSSbaseEntity;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.monitored_directory.MonitoredDirectory;

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
    @Column(name = "scan_time", nullable = false)
    private OffsetDateTime scanTime;

    /**
     * The status of the scan (e.g., completed, failed).
     */
    @Column(nullable = false)
    private String status;

    /**
     * Additional notes about the scan.
     */
    @Column
    private String notes;

    /**
     * The monitored directory associated with this scan.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "monitored_directory_id", nullable = false)
    private MonitoredDirectory monitoredDirectory;

    // Getters and setters

    /**
     * The unique identifier for this scan entity.
     *
     * @return the ID of the scan
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this scan entity.
     *
     * @param id the ID to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the time when the scan was performed.
     *
     * @return the scan time
     */
    public OffsetDateTime getScanTime() {
        return scanTime;
    }

    /**
     * Sets the time when the scan was performed.
     *
     * @param scanTime the scan time to set
     */
    public void setScanTime(OffsetDateTime scanTime) {
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
    public String getNotes() {
        return notes;
    }

    /**
     * Sets additional notes about the scan.
     *
     * @param notes the notes to set
     */
    public void setNotes(String notes) {
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

    @Override
    protected String getDBSection() {
        return FIS;
    }
}