package org.pwss.file_integrity_scanner.msr.service.scan.component;

import lib.pwss.hash.FileHashHandler;
import lib.pwss.hash.model.HashForFilesOutput;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Component responsible for computing hashes for files.
 */
@Component
public final class FileHashComputer {

    
    public FileHashComputer() {
    }

    private final FileHashHandler fileHashHandler = new FileHashHandler();

    /**
     * Computes all hashes for the given file.
     *
     * @param file the file for which hashes need to be computed
     * @return an object containing the computed hashes for the file
     */
    public final HashForFilesOutput computeHashes(File file) {
        return fileHashHandler.GetAllHashes(file);
    }
}
