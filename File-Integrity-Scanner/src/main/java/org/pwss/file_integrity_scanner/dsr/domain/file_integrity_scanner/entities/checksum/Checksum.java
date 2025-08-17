package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.checksum;


import org.pwss.file_integrity_scanner.dsr.domain.PWSSbaseEntity;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.file.File;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;



@Entity
@Table(name = "checksum")
public class Checksum extends PWSSbaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "file_id", nullable = false)
    private File file;

    @Column(name = "checksum_sha256", nullable = false)
    private String checksumSha256;

    @Column(name = "checksum_sha3", nullable = false)
    private String checksumSha3;

    @Column(name = "checksum_blake_2b", nullable = false)
    private String checksumBlake2b;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getChecksumSha256() {
        return checksumSha256;
    }

    public void setChecksumSha256(String checksumSha256) {
        this.checksumSha256 = checksumSha256;
    }

    public String getChecksumSha3() {
        return checksumSha3;
    }

    public void setChecksumSha3(String checksumSha3) {
        this.checksumSha3 = checksumSha3;
    }

    public String getChecksumBlake2b() {
        return checksumBlake2b;
    }

    public void setChecksumBlake2b(String checksumBlake2b) {
        this.checksumBlake2b = checksumBlake2b;
    }

    @Override
    protected String getDBSection() {
       return FIS;
    }
}