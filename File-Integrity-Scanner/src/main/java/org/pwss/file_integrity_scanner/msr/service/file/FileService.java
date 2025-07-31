package org.pwss.file_integrity_scanner.msr.service.file;

import org.pwss.file_integrity_scanner.msr.domain.model.entities.file.File;

public interface FileService {
    File findByPath(String path);

    boolean existsByPath(String path);

    void save(File file);
}
