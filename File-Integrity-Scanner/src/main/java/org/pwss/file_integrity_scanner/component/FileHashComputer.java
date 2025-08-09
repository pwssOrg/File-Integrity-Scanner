package org.pwss.file_integrity_scanner.component;

import lib.pwss.hash.file_hash_handler.BigFileHashHandler;
import lib.pwss.hash.file_hash_handler.FileHashHandler;
import lib.pwss.hash.compare.util.HashCompareUtil;
import lib.pwss.hash.model.HashForFilesOutput;
import org.pwss.file_integrity_scanner.msr.domain.model.entities.checksum.Checksum;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Component responsible for computing hashes for files.
 */
@Component
public final class FileHashComputer {

    // TODO: Let the user adjust the maximum limit
    private final long TEMP_USER_DEFINED_MAX_LIMIT = 5000L * 1024 * 1024; // 5000 MB

    private final org.slf4j.Logger log;

    // Instance of FileHashHandler for computing hashes of smaller files
    private final FileHashHandler fileHashHandler;

    // Instance of BigFileHashHandler for computing hashes of larger files
    private final BigFileHashHandler bigFileHashHandler;

    public FileHashComputer() {
        this.log = org.slf4j.LoggerFactory.getLogger(FileHashComputer.class);
        this.fileHashHandler = new FileHashHandler();
        this.bigFileHashHandler = new BigFileHashHandler(TEMP_USER_DEFINED_MAX_LIMIT);
    }

    /**
     * Computes all hashes for the given file.
     *
     * @param file the file for which hashes need to be computed
     * @return an object containing the computed hashes for the file
     */
    public HashForFilesOutput computeHashes(File file) {
        try {
            return fileHashHandler.GetAllHashes(file);
        } catch (OutOfMemoryError outOfMemoryError) {
            log.debug("Large file detected, switching to BigFileHashHandler for file: {}", file.getPath());
            return bigFileHashHandler.GetAllHashes(file);
        }
    }

    /**
     * Compares the hashes of two checksum objects.
     * <p>
     * This method compares the SHA-256, SHA-3, and Blake2b hashes of the provided
     * checksum objects using XOR and Java's equals method. It returns true only if
     * all three hash comparisons match.
     *
     * @param first  the first checksum object to compare
     * @param second the second checksum object to compare
     * @return true if all hash comparisons match, false otherwise
     */
    public boolean compareHashes(Checksum first, Checksum second) {
        return HashCompareUtil.compareUsingXorAndJavaEquals(first.getChecksumSha256(), second.getChecksumSha256()) &&
                HashCompareUtil.compareUsingXorAndJavaEquals(first.getChecksumSha3(), second.getChecksumSha3()) &&
                HashCompareUtil.compareUsingXorAndJavaEquals(first.getChecksumBlake2b(), second.getChecksumBlake2b());
    }
}
