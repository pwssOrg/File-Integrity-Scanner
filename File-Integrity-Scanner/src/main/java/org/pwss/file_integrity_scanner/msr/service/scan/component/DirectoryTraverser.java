package org.pwss.file_integrity_scanner.msr.service.scan.component;

import org.pwss.io_file.FileTraverserImpl;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Component responsible for traversing directories and retrieving all file paths.
 */
@Component
public class DirectoryTraverser {

    /**
     * Scans a directory and retrieves a list of all files within it.
     *
     * @param directoryPath the path of the directory to scan
     * @return a list of files found in the directory
     * @throws ExecutionException   if an error occurs during the asynchronous file traversal
     * @throws InterruptedException if the thread executing the file traversal is interrupted
     */
    public final List<File> scanDirectory(String directoryPath) throws ExecutionException, InterruptedException {
        FileTraverserImpl traverser = new FileTraverserImpl();
        Future<List<File>> future = traverser.traverse(directoryPath);

        List<File> files = future.get();

        traverser.shutdownThreadPool();

        return files;
    }
}
