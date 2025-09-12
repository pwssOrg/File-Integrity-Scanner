package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.diff;

import org.pwss.file_integrity_scanner.dsr.domain.PWSSbaseEntity;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan_summary.ScanSummary;
import org.pwss.file_integrity_scanner.dsr.domain.mixed.entities.time.Time;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * This entity captures information about integrity failures detected during
 * scans,
 * including references to baseline and failed scan summaries, as well as the
 * time of detection.
 */
@Entity
@Table(name = "diff")
public class Diff extends PWSSbaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The scan summary representing the baseline for comparison.
     *
     * @return the {@link ScanSummary} object for the baseline scan
     */
    @OneToOne(optional = false)
    @JoinColumn(name = "baseline_id", nullable = false)
    private ScanSummary baseline;

    /**
     * The scan summary representing the failed integrity check.
     *
     * @return the {@link ScanSummary} object for the failed integrity scan
     */
    @OneToOne(optional = false)
    @JoinColumn(name = "integrity_fail_id", nullable = false)
    private ScanSummary integrityFail;

    /**
     * The time when the integrity failure was detected.
     *
     * @return the {@link Time} object representing the detection time
     */
    @OneToOne(optional = false)
    @JoinColumn(name = "time_id", nullable = false)
    private Time time;

    /**
     * Default constructor for Diff entity.
     */
    public Diff() {
    }

    /**
     * Constructor to create a new Diff entity with the specified baseline scan,
     * failed integrity check scan,
     * and detection time.
     *
     * @param baselineScan      the {@link ScanSummary} object representing the
     *                          baseline scan
     * @param integrityFailScan the {@link ScanSummary} object representing the
     *                          failed integrity scan
     * @param time              the {@link Time} object representing when the
     *                          failure was detected
     */
    public Diff(ScanSummary baselineScan, ScanSummary integrityFailScan, Time time) {
        this.baseline = baselineScan;
        this.integrityFail = integrityFailScan;
        this.time = time;
    }

    /**
     * Gets the scan summary representing the baseline for comparison.
     *
     * @return the {@link ScanSummary} object for the baseline scan
     */
    public ScanSummary getBaseline() {
        return baseline;
    }

    /**
     * Sets the scan summary representing the baseline for comparison.
     *
     * @param baselineId the {@link ScanSummary} object to be set as the baseline
     *                   scan
     */
    public void setBaseline(ScanSummary baselineId) {
        this.baseline = baselineId;
    }

    /**
     * Gets the scan summary representing the failed integrity check.
     *
     * @return the {@link ScanSummary} object for the failed integrity scan
     */
    public ScanSummary getIntegrityFail() {
        return integrityFail;
    }

    /**
     * Sets the scan summary representing the failed integrity check.
     *
     * @param integrityFailId the {@link ScanSummary} object to be set as the failed
     *                        integrity scan
     */
    public void setIntegrityFail(ScanSummary integrityFailId) {
        this.integrityFail = integrityFailId;
    }

    /**
     * Gets the time when the integrity failure was detected.
     *
     * @return the {@link Time} object representing the detection time
     */
    public Time getTime() {
        return time;
    }

    /**
     * Sets the time when the integrity failure was detected.
     *
     * @param time the {@link Time} object to be set as the detection time
     */
    public void setTime(Time time) {
        this.time = time;
    }

    @Override
    protected String getDBSection() {
        return FIS;
    }
}