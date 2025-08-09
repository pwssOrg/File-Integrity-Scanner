package org.pwss.file_integrity_scanner.component;

import lib.pwss.hash.FileHashHandler;
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


    public FileHashComputer() {
    }

    private final FileHashHandler fileHashHandler = new FileHashHandler();

    /**
     * Computes all hashes for the given file.
     *
     * @param file the file for which hashes need to be computed
     * @return an object containing the computed hashes for the file
     */
    public HashForFilesOutput computeHashes(File file) {
        return fileHashHandler.GetAllHashes(file);
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
