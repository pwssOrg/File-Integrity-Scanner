package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan_summary;

import org.pwss.file_integrity_scanner.dsr.domain.PWSSbaseEntity;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.checksum.Checksum;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.file.File;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan.Scan;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * This entity represents a summary of a scan performed on a file.
 * It maps to a table in the database and establishes
 * relationships
 * with Scan, File, and Checksum entities through foreign keys.
 */
@Entity
@Table(name = "scan_summary")
public class ScanSummary extends PWSSbaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Many-to-one relationship to the Scan entity.
     * This field represents the scan that this summary belongs to.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "scan_id", nullable = false)
    private Scan scan;

    /**
     * Many-to-one relationship to the File entity.
     * This field represents the file that was scanned.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "file_id", nullable = false)
    private File file;

    /**
     * Many-to-one relationship to the Checksum entity.
     * This field represents the checksum of the scanned file.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "checksum_id", nullable = false)
    private Checksum checksum;

    /**
     * Default constructor for creating an empty {@link ScanSummary}.
     *
     * This constructor initializes a new instance of {@link ScanSummary} with
     * default values
     * (null for all fields). It is used by persistence
     * framework JPA.
     */
    public ScanSummary() {
    }

    /**
     * Constructs a new {@link ScanSummary} instance with the specified scan, file,
     * and checksum.
     *
     * @param scan     the {@link Scan} entity associated with this summary
     * @param file     the {@link File} entity being summarized in this scan
     * @param checksum the {@link Checksum} generated for the file during the scan
     */
    public ScanSummary(Scan scan, File file, Checksum checksum) {
        this.scan = scan;
        this.file = file;
        this.checksum = checksum;

    }

    /**
     * Gets the unique identifier of this scan summary entry.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this scan summary entry.
     *
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the Scan entity associated with this scan summary.
     *
     * @return the scan
     */
    public Scan getScan() {
        return scan;
    }

    /**
     * Sets the Scan entity for this scan summary.
     *
     * @param scan the scan to set
     */
    public void setScan(Scan scan) {
        this.scan = scan;
    }

    /**
     * Gets the File entity associated with this scan summary.
     *
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * Sets the File entity for this scan summary.
     *
     * @param file the file to set
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Gets the Checksum entity associated with this scan summary.
     *
     * @return the checksum
     */
    public Checksum getChecksum() {
        return checksum;
    }

    /**
     * Sets the Checksum entity for this scan summary.
     *
     * @param checksum the checksum to set
     */
    public void setChecksum(Checksum checksum) {
        this.checksum = checksum;
    }

    @Override
    protected String getDBSection() {
        return FIS;
    }

}