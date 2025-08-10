package org.pwss.file_integrity_scanner.component;

import org.pwss.io_file.FileTraverserImpl;
import org.pwss.util.PWSSDirectoryNavUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Component responsible for traversing directories and retrieving all file
 * paths.
 */
@Component
public class DirectoryTraverser {

    private final org.slf4j.Logger log;

    public DirectoryTraverser() {
        this.log = org.slf4j.LoggerFactory.getLogger(DirectoryTraverser.class);
    }

    /**
     * Collects all files in a directory asynchronously.
     * <p>
     * This method uses a `FileTraverserImpl` instance to traverse the specified directory
     * and retrieve a list of files. The traversal is performed asynchronously, and the
     * result is returned as a `Future`.
     *
     * @param directoryPath the path of the directory to scan
     * @return a Future containing the list of files found in the directory
     */
    @Async
    public Future<List<File>> collectFilesInDirectory(String directoryPath) {
        log.info("Starting asynchronous traversal scan for directory: {}", directoryPath);
        FileTraverserImpl traverser = new FileTraverserImpl();
        return traverser.traverse(directoryPath);
    }

   /**
 * Collects top-level files from a directory using an asynchronous call.
 * <p>
 * This method retrieves the files located directly in the specified directory
 * without traversing its subdirectories. The operation is performed synchronously,
 * but returns a `CompletableFuture` to align with async frameworks like Spring's @Async annotation,
 * allowing for further chaining or handling of results asynchronously.
 *
 * @param directoryPath the directory to scan for top-level files
 * @return a CompletableFuture containing the list of top-level files in the directory
 */
@Async
public CompletableFuture<List<File>> collectTopLevelFiles(File directoryPath) {
    log.info("Starting synchronous collection of top-level files from directory: {}", directoryPath);

    // Retrieve the files directly within the specified directory, excluding subdirectories
    List<File> files = PWSSDirectoryNavUtil.GetSelectedFolderWithoutSubFolders(directoryPath);

    // Return a completed CompletableFuture with the list of files.
    // Note that this does not make the method itself asynchronous,
    // but it enables integration with async frameworks.
    return CompletableFuture.completedFuture(files);

   
}

}
