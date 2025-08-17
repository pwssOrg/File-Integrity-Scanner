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

@Entity
@Table(name = "scan_summary")
public class ScanSummary extends PWSSbaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "scan_id", nullable = false)
    private Scan scan;

    @ManyToOne(optional = false)
    @JoinColumn(name = "file_id", nullable = false)
    private File file;

    @ManyToOne(optional = false)
    @JoinColumn(name = "checksum_id", nullable = false)
    private Checksum checksum;

    // Getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Scan getScan() {
        return scan;
    }

    public void setScan(Scan scan) {
        this.scan = scan;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Checksum getChecksum() {
        return checksum;
    }

    public void setChecksum(Checksum checksum) {
        this.checksum = checksum;
    }

    @Override
    protected String getDBSection() {
        return FIS;
    }
}