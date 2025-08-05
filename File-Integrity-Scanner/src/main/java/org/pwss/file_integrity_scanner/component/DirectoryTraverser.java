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
     *
     * This method uses a `FileTraverserImpl` instance to traverse the specified directory
     * and retrieve a list of files. The traversal is performed asynchronously, and the
     * result is returned as a `Future`.
     *
     * @param directoryPath the path of the directory to scan
     * @return a Future containing the list of files found in the directory
     */
    public final Future<List<File>> collectFilesInDirectory(String directoryPath) {
        FileTraverserImpl traverser = new FileTraverserImpl();
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
