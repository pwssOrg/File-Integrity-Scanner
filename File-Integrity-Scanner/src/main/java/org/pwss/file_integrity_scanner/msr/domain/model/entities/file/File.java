package org.pwss.file_integrity_scanner.msr.domain.model.entities.file;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

import org.pwss.file_integrity_scanner.msr.domain.model.entities.BaseEntity;

@Entity
@Table(name = "files")
public class File extends BaseEntity {

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

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBasename() {
        return basename;
    }

    public void setBasename(String basename) {
        this.basename = basename;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public OffsetDateTime getMtime() {
        return mtime;
    }

    public void setMtime(OffsetDateTime mtime) {
        this.mtime = mtime;
    }
}