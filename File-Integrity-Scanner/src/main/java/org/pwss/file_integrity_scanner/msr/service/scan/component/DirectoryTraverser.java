package org.pwss.file_integrity_scanner.msr.service.scan.component;

import org.pwss.FileNavigatorImpl;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Component responsible for traversing directories and retrieving all file paths.
 */
@Component
public class DirectoryTraverser {

    /**
     * Scans a directory and retrieves a list of all file paths within it.
     *
     * @param directoryPath the path of the directory to scan
     * @return a list of file paths found in the directory
     * @throws java.io.IOException if an I/O error occurs while accessing the directory
     * @throws InterruptedException if the thread is interrupted while waiting for the scan to complete
     * @throws ExecutionException if the computation threw an exception
     */
    public final List<Path> scanDirectory(String directoryPath) throws java.io.IOException, InterruptedException, ExecutionException {
        FileNavigatorImpl navigator = new FileNavigatorImpl(directoryPath);
        List<Future<List<Path>>> futures = navigator.traverseFiles();

        List<Path> allPaths = new ArrayList<>();
        for (Future<List<Path>> future : futures) {
            allPaths.addAll(future.get());
        }
        return allPaths;
    }
}
