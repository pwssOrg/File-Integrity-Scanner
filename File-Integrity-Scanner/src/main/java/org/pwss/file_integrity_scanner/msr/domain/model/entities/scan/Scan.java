package org.pwss.file_integrity_scanner.msr.domain.model.entities.scan;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

import org.pwss.file_integrity_scanner.msr.domain.model.entities.monitored_directory.MonitoredDirectory;

@Entity
@Table(name = "scans")
public class Scan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "scan_time", nullable = false)
    private OffsetDateTime scanTime;

    @Column(nullable = false)
    private String status;

    @Column
    private String notes;

    @ManyToOne(optional = false)
    @JoinColumn(name = "monitored_directory_id", nullable = false)
    private MonitoredDirectory monitoredDirectory;

    // Getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OffsetDateTime getScanTime() {
        return scanTime;
    }

    public void setScanTime(OffsetDateTime scanTime) {
        this.scanTime = scanTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public MonitoredDirectory getMonitoredDirectory() {
        return monitoredDirectory;
    }

    public void setMonitoredDirectory(MonitoredDirectory monitoredDirectory) {
        this.monitoredDirectory = monitoredDirectory;
    }
}