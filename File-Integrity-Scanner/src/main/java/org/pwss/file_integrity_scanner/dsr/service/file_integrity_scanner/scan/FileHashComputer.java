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

    private long USER_DEFINED_MAX_LIMIT = 10000L * 1024 * 1024; // 10 000 MB

    /**
     * Max size of a byte array in java -2
     * (2^31-1) - 2
     * You may get OOM exceptions for less if there is not enough memory free.
     */
    private final int MAX_SIZE_OF_BYTE_ARRAY = 2147483645;

    private final org.slf4j.Logger log;

    // Instance of FileHashHandler for computing hashes of smaller files
    private final FileHash fileHashHandler;

    // Instance of BigFileHashHandler for computing hashes of larger files
    private final BigFileHashHandler bigFileHashHandler;

     FileHashComputer() {
        this.log = org.slf4j.LoggerFactory.getLogger(FileHashComputer.class);
        this.fileHashHandler = new FileHashHandler();
        this.bigFileHashHandler = new BigFileHashHandler(USER_DEFINED_MAX_LIMIT);
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

            if (file.length() > MAX_SIZE_OF_BYTE_ARRAY)
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

    //TODO: Add Java Docs
    public final void setUserDefinedMaxLimitInHashComputer(long userDefinedMaxLimit) {
        USER_DEFINED_MAX_LIMIT = userDefinedMaxLimit;
        //TODO: Add Setter to Lib for version 1.23 
    }

    
}
