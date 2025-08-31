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

/**
 * Entity class representing a integrity check fail in the File Integrity
 * Scanner domain.
 */
@Entity
@Table(name = "diff")
public class Diff extends PWSSbaseEntity {

    // TODO: Add Java Docs For The Entire Class

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

    @Override
    protected String getDBSection() {
        return FIS;
    }
}