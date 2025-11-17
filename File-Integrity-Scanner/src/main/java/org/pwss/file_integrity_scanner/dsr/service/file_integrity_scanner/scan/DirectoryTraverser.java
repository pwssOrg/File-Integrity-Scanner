package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.scan;

import org.pwss.io_file.FileTraverser;
import org.pwss.util.PWSSDirectoryNavUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * Component responsible for traversing directories and retrieving all file
 * paths.
 * <p>
 * This component uses the SLF4J logger for logging directory traversal events.
 * It provides methods to explore directories and collect file paths
 * recursively.
 * </p>
 */
@Component
class DirectoryTraverser {

    private final org.slf4j.Logger log;

    /**
     * Constructs a new {@link DirectoryTraverser} instance.
     * <p>
     * The constructor initializes the SLF4J logger for the class, allowing
     * it to log messages related to directory traversal and file processing.
     * </p>
     */
    DirectoryTraverser() {
        this.log = org.slf4j.LoggerFactory.getLogger(DirectoryTraverser.class);
    }

    /**
     * Collects all files in a directory asynchronously.
     * <p>
     * This method uses a provided {@link FileTraverser} instance to traverse the
     * specified
     * directory and retrieve a list of files. The traversal is performed
     * asynchronously,
     * and the result is returned as a {@link Future}.
     *
     * @param directoryPath the path of the directory to scan
     * @param fileTraverser the FileTraverser implementation used for directory
     *                      traversal
     * @return a Future containing the list of files found in the directory
     */
    @Async
    Future<List<File>> collectFilesInDirectory(String directoryPath, FileTraverser fileTraverser) {
        log.info("Starting asynchronous traversal scan for directory: {}", directoryPath);
        return fileTraverser.traverse(directoryPath);
    }

    /**
     * Collects top-level files from a directory using an asynchronous call.
     * <p>
     * This method retrieves the files located directly in the specified directory
     * without traversing its subdirectories. The operation is performed
     * synchronously,
     * but returns a {@link CompletableFuture} to align with async frameworks like
     * Spring's @Async annotation,
     * allowing for further chaining or handling of results asynchronously.
     *
     * @param directoryPath the directory to scan for top-level files
     * @return a CompletableFuture containing the list of top-level files in the
     *         directory
     */
    @Async
    CompletableFuture<List<File>> collectTopLevelFiles(File directoryPath) {
        log.info("Starting synchronous collection of top-level files from directory: {}", directoryPath);

        // Retrieve the files directly within the specified directory, excluding
        // subdirectories
        List<File> files = PWSSDirectoryNavUtil.GetSelectedFolderWithoutSubFolders(directoryPath);

        // Return a completed CompletableFuture with the list of files.
        // Note that this does not make the method itself asynchronous,
        // but it enables integration with async frameworks.
        return CompletableFuture.completedFuture(files);

    }

}
