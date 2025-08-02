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
     * Scans a directory and retrieves a list of all files within it.
     *
     * @param directoryPath the path of the directory to scan
     * @return a list of files found in the directory
     * @throws ExecutionException   if an error occurs during the asynchronous file
     *                              traversal
     * @throws InterruptedException if the thread executing the file traversal is
     *                              interrupted
     */
    public final List<File> collectFilesInDirectory(String directoryPath)
            throws ExecutionException, InterruptedException {
        FileTraverserImpl traverser = new FileTraverserImpl();
        Future<List<File>> future = traverser.traverse(directoryPath);

        List<File> files = future.get();

        traverser.shutdownThreadPool();

        return files;
    }

    /**
     * Collects all top-level files from the specified directory, excluding any
     * files in subdirectories.
     *
     * @param directoryPath the path of the directory to scan for top-level files
     * @return an Optional containing a list of top-level files found in the
     *         directory,
     *         or empty if no files are found
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
