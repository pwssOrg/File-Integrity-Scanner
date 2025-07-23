package org.pwss.msr.domain.model.entities.scan_details;

import jakarta.persistence.*;
import org.pwss.msr.domain.model.entities.BaseEntity;
import org.pwss.msr.domain.model.entities.checksum.Checksum;
import org.pwss.msr.domain.model.entities.file.File;
import org.pwss.msr.domain.model.entities.scan.Scan;

@Entity
@Table(name = "scan_details")
public class ScanDetails extends BaseEntity {

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
}