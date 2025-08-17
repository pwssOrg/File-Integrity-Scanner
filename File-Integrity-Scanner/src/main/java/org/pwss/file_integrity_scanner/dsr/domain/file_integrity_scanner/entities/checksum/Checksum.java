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

/**
 * The Checksum entity represents a checksum record associated with a file.
 */
@Entity
@Table(name = "checksum")
public class Checksum extends PWSSbaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Many-to-one relationship with the File entity. This field is mandatory and
     * not nullable.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "file_id", nullable = false)
    private File file;

    /**
     * SHA-256 checksum of the file.
     */
    @Column(name = "checksum_sha256", nullable = false)
    private String checksumSha256;

    /**
     * SHA-3 checksum of the file.
     */
    @Column(name = "checksum_sha3", nullable = false)
    private String checksumSha3;

    /**
     * Blake2b checksum of the file.
     */
    @Column(name = "checksum_blake_2b", nullable = false)
    private String checksumBlake2b;

    // Getters and setters

    /**
     * Gets the unique identifier of this checksum record.
     *
     * @return The ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of this checksum record.
     *
     * @param id The new ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the associated file.
     *
     * @return The file
     */
    public File getFile() {
        return file;
    }

    /**
     * Sets the associated file.
     *
     * @param file The new file
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Gets the SHA-256 checksum of the file.
     *
     * @return The SHA-256 checksum
     */
    public String getChecksumSha256() {
        return checksumSha256;
    }

    /**
     * Sets the SHA-256 checksum of the file.
     *
     * @param checksumSha256 The new SHA-256 checksum
     */
    public void setChecksumSha256(String checksumSha256) {
        this.checksumSha256 = checksumSha256;
    }

    /**
     * Gets the SHA-3 checksum of the file.
     *
     * @return The SHA-3 checksum
     */
    public String getChecksumSha3() {
        return checksumSha3;
    }

    /**
     * Sets the SHA-3 checksum of the file.
     *
     * @param checksumSha3 The new SHA-3 checksum
     */
    public void setChecksumSha3(String checksumSha3) {
        this.checksumSha3 = checksumSha3;
    }

    /**
     * Gets the Blake2b checksum of the file.
     *
     * @return The Blake2b checksum
     */
    public String getChecksumBlake2b() {
        return checksumBlake2b;
    }

    /**
     * Sets the Blake2b checksum of the file.
     *
     * @param checksumBlake2b The new Blake2b checksum
     */
    public void setChecksumBlake2b(String checksumBlake2b) {
        this.checksumBlake2b = checksumBlake2b;
    }

    @Override
    protected String getDBSection() {
        return FIS;
    }
}