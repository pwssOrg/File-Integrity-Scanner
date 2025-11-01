package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.scan;

import lib.pwss.hash.file_hash_handler.BigFileHashHandler;
import lib.pwss.hash.file_hash_handler.FileHashHandler;
import lib.pwss.hash.FileHash;
import lib.pwss.hash.compare.util.HashCompareUtil;
import lib.pwss.hash.model.HashForFilesOutput;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.checksum.Checksum;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Optional;

/**
 * Component responsible for computing hashes for files.
 */
@Component
final class FileHashComputer {

    /**
     * The maximum limit in bytes (100 MB) for reading hashes from files using
     * memory.
     * Any file that exceeds this size will have its hashes extracted using
     * a buffer
     * instead of being fully loaded into memory at once.
     */
    private final int MEMORY_STRATEGY_LIMIT = 100000000;

    private final org.slf4j.Logger log;

    // Instance of FileHashHandler for computing hashes of smaller files
    private final FileHash fileHashHandler;

    // Instance of BigFileHashHandler for computing hashes of larger files
    private final BigFileHashHandler bigFileHashHandler;

    FileHashComputer() {
        this.log = org.slf4j.LoggerFactory.getLogger(FileHashComputer.class);
        this.fileHashHandler = new FileHashHandler();
        this.bigFileHashHandler = new BigFileHashHandler(-1l);
    }

    /**
     * Computes hashes for the given file using appropriate hash handler based on
     * file size.
     *
     * @param file The file to compute hashes for
     * @return An Optional containing {@link HashForFilesOutput} if successful, or
     *         empty if an error occurs
     */
    Optional<HashForFilesOutput> computeHashes(File file) {

        try {

            if (file.length() > MEMORY_STRATEGY_LIMIT)
                return Optional.of(bigFileHashHandler.GetAllHashes(file));
            else
                return Optional.of(fileHashHandler.GetAllHashes(file));

        } catch (OutOfMemoryError outOfMemoryError) {
            log.debug("OutOfMemoryError occurred, switching to BigFileHashHandler for file: {}", file.getPath());
            return Optional.of(bigFileHashHandler.GetAllHashes(file));
        }

        catch (NullPointerException nullPointerException) {

            log.error("nullPointerException for file: {} - {}", file.getPath(), nullPointerException.getMessage());
            return Optional.empty();
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
    boolean compareHashes(Checksum first, Checksum second) {
        return HashCompareUtil.compareUsingXorAndJavaEquals(first.getChecksumSha256(), second.getChecksumSha256()) &&
                HashCompareUtil.compareUsingXorAndJavaEquals(first.getChecksumSha3(), second.getChecksumSha3()) &&
                HashCompareUtil.compareUsingXorAndJavaEquals(first.getChecksumBlake2b(), second.getChecksumBlake2b());
    }

    /**
     * Sets a user-defined maximum file size limit in the hash computer.
     *
     * This method delegates the setting of the user-defined maximum file size limit
     * to the {@link BigFileHashHandler} instance. The purpose of this limit is to
     * determine
     * whether to attempt extracting a hash from a file or not, based on its size.
     *
     * @param userDefinedMaxLimit The maximum file size limit to be set, as
     *                            specified by the
     *                            user.
     */
    final void setUserDefinedMaxLimitInHashComputer(long userDefinedMaxLimit) {
        bigFileHashHandler.setUserDefinedMaxLimit(userDefinedMaxLimit);
    }

}
