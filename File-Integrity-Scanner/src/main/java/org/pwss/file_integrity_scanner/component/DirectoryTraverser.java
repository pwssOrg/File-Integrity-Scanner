package org.pwss.file_integrity_scanner.component;

import org.pwss.io_file.FileTraverserImpl;
import org.pwss.util.PWSSDirectoryNavUtil;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Component responsible for traversing directories and retrieving all file
 * paths.
 */
@Component
public class DirectoryTraverser {

    /**
     * Collects all files in a directory asynchronously.
     * <p>
     * This method uses a `FileTraverserImpl` instance to traverse the specified directory
     * and retrieve a list of files. The traversal can include subdirectories based on the
     * `includeSubFolders` flag.
     *
     * @param directoryPath the path of the directory to scan
     * @param includeSubFolders a flag indicating whether to include subdirectories in the scan
     * @return a Future containing the list of files found in the directory
     * @throws ExecutionException if an error occurs during the asynchronous file traversal
     * @throws InterruptedException if the thread executing the file traversal is interrupted
     */
    public final Future<List<File>> collectFilesInDirectory(String directoryPath, boolean includeSubFolders)
            throws ExecutionException, InterruptedException {
        FileTraverserImpl traverser = new FileTraverserImpl();
        // TODO: Make includeSubFolders working
        return traverser.traverse(directoryPath);
    }

    /**
     * Collects all top-level files from the specified directory, excluding any
     * files in subdirectories.
     *
     * @param directoryPath the path of the directory to scan for top-level files
     * @return an Optional containing a list of top-level files found in the
     * directory,
     * or empty if no files are found
     * @throws ExecutionException   if an error occurs during the asynchronous file
     *                              traversal
     * @throws InterruptedException if the thread executing the file traversal is
     *                              interrupted
     */
    public final Optional<List<File>> collectTopLevelFiles(File directoryPath)
            throws ExecutionException, InterruptedException {

        return Optional.of(PWSSDirectoryNavUtil.GetSelectedFolderWithoutSubFolders(directoryPath));
    }

}
