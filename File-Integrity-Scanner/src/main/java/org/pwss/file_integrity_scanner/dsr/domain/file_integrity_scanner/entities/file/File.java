package org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.file;

import java.time.OffsetDateTime;

import org.pwss.file_integrity_scanner.dsr.domain.PWSSbaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity class representing a file in the File Integrity Scanner domain.
 */
@Entity
@Table(name = "file")
public class File extends PWSSbaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String path;

    @Column(nullable = false)
    private String basename;

    @Column(nullable = false)
    private String directory;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false)
    private OffsetDateTime mtime;

    

    /**
     * The unique identifier for this file. This is an auto-generated primary key.
     *
     * @return the ID of the file
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this file.
     *
     * @param id the ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the path of the file. This is a unique value representing the full
     * filesystem path.
     *
     * @return the path of the file
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the path of the file.
     *
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Gets the basename of the file (the filename without directory).
     *
     * @return the basename of the file
     */
    public String getBasename() {
        return basename;
    }

    /**
     * Sets the basename of the file.
     *
     * @param basename the basename to set
     */
    public void setBasename(String basename) {
        this.basename = basename;
    }

    /**
     * Gets the directory containing the file (the path without the filename).
     *
     * @return the directory of the file
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * Sets the directory containing the file.
     *
     * @param directory the directory to set
     */
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    /**
     * Gets the size of the file in bytes.
     *
     * @return the size of the file
     */
    public Long getSize() {
        return size;
    }

    /**
     * Sets the size of the file.
     *
     * @param size the size to set
     */
    public void setSize(Long size) {
        this.size = size;
    }

    /**
     * Gets the modification time of the file.
     *
     * @return the modification time of the file
     */
    public OffsetDateTime getMtime() {
        return mtime;
    }

    /**
     * Sets the modification time of the file.
     *
     * @param mtime the modification time to set
     */
    public void setMtime(OffsetDateTime mtime) {
        this.mtime = mtime;
    }

    @Override
    protected String getDBSection() {
        return FIS;
    }
}