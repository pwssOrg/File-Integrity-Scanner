package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.file;

import java.nio.file.Path;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.file.File;
import org.pwss.file_integrity_scanner.exception.file_integrity_scanner.file.QuarantineFailedException;
import org.pwss.file_integrity_scanner.exception.file_integrity_scanner.file.UnquarantineFailedException;
import org.pwss.quarantineManager_aes.FileQuarantineManager;
import org.pwss.quarantineManager_aes.dto.MetaDataResult;

import org.springframework.stereotype.Component;

/**
 * Component responsible for handling file isolation operations,
 * including quarantining and unquarantining files.
 */
@Component
final class FileIsolationComponent {

    /**
     * Logger for logging information, warnings, errors, etc.
     */
    private final org.slf4j.Logger log;

    /**
     * Manager used to perform quarantine and unquarantine operations on files.
     */
    private final FileQuarantineManager quarantineManager;

    /**
     * Constructor for the FileIsolationComponent. Initializes the logger
     * and creates a new instance of FileQuarantineManager.
     */
    FileIsolationComponent() {
        this.log = org.slf4j.LoggerFactory.getLogger(FileIsolationComponent.class);
        this.quarantineManager = new FileQuarantineManager();

    }

    /**
     * Quarantines the specified file.
     *
     * @param file The file to be quarantined.
     * @return A MetaDataResult containing information about the quarantine
     *         operation.
     * @throws QuarantineFailedException if the quarantine operation fails.
     */
    final MetaDataResult quarantineFile(File file) throws QuarantineFailedException {
        final Path fileToQuarantine;
        try {
            fileToQuarantine = Path.of(file.getPath());
            return quarantineManager.quarantine(fileToQuarantine);
        } catch (Exception e) {
            log.error("The qurantine operation failed - {}", e);
            throw new QuarantineFailedException("Qurantine operation failed!", file.getPath());
        }
    }

    /**
     * Unquarantines the specified file using its key path.
     *
     * @param keyPath The key path of the file to be unquarantined.
     * @return A MetaDataResult containing information about the unquarantine
     *         operation.
     * @throws UnquarantineFailedException if the unquarantine operation fails.
     */
    final MetaDataResult unquarantineFile(String keyPath) throws UnquarantineFailedException {
        try {
            return quarantineManager.unquarantine(keyPath);
        } catch (Exception e) {
            log.error("The un-qurantine operation failed - {}", e);
            throw new UnquarantineFailedException("Un-qurantine operation failed", keyPath);
        }
    }

}
