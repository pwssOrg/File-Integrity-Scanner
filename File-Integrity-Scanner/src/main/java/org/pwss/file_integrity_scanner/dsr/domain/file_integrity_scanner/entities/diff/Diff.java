package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.diff;

import org.pwss.file_integrity_scanner.dsr.domain.PWSSbaseEntity;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan_summary.ScanSummary;
import org.pwss.file_integrity_scanner.dsr.domain.mixed.time.Time;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

  // TODO: Add Java Docs For The Entire Class

/**
 * Entity class representing a integrity check fail in the File Integrity
 * Scanner domain.
 */
@Entity
@Table(name = "diff")
public class Diff extends PWSSbaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "baseline_id", nullable = false)
    private ScanSummary baselineId;

    @OneToOne(optional = false)
    @JoinColumn(name = "integrity_fail_id", nullable = false)
    private ScanSummary integrityFailId;

    @OneToOne(optional = false)
    @JoinColumn(name = "time_id", nullable = false)
    private Time time;


    public Diff() {
    }


    public Diff(ScanSummary baselineScan, ScanSummary integrityFailScan, Time time) {
        this.baselineId = baselineScan;
        this.integrityFailId = integrityFailScan;
        this.time = time;
    }



    public ScanSummary getBaselineId() {
        return baselineId;
    }



    public void setBaselineId(ScanSummary baselineId) {
        this.baselineId = baselineId;
    }



    public ScanSummary getIntegrityFailId() {
        return integrityFailId;
    }



    public void setIntegrityFailId(ScanSummary integrityFailId) {
        this.integrityFailId = integrityFailId;
    }



    public Time getTime() {
        return time;
    }



    public void setTime(Time time) {
        this.time = time;
    }



    @Override
    protected String getDBSection() {
        return FIS;
    }
}