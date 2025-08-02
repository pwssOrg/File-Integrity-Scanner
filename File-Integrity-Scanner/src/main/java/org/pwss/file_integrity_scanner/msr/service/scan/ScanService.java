package org.pwss.file_integrity_scanner.msr.service.scan;

import org.pwss.file_integrity_scanner.msr.domain.model.entities.scan.Scan;

import java.io.File;

/**
 * Service interface for managing directory scans.
 * This interface defines methods to scan directories, stop ongoing scans,
 * and provides the ability to scan all monitored directories or a specific one.
 */
public interface ScanService {
    /**
     * Initiates scanning of all monitored directories.
     * This method will start the scanning process for each directory that is being
     * monitored
     * within the system.
     */
    void scanAllDirectories();

    /**
     * Scans a monitored directory using the provided scan instance.
     *
     * This method is an overloaded variant that calls the two-parameter method with
     * default value set to true for
     * includeSubFolders.
     *
     * @param scanInstance the scan instance used for scanning the directory
     * @return true if the monitored directory scan is successful, false otherwise
     */
    Boolean scanMonitoredDirectory(Scan scanInstance);

    /**
     * Scans a monitored directory using the provided scan instance.
     *
     * @param scanInstance      the scan instance used for scanning the directory
     * @param includeSubFolders whether to include subfolders in the scan (default
     *                          is false)
     * @return true if the monitored directory scan is successful, false otherwise
     */
    Boolean scanMonitoredDirectory(Scan scanInstance, boolean includeSubFolders);

    /**
     * Processes a file by checking its existence in the database, updating or
     * creating
     * the corresponding file entity, and saving associated checksum and scan
     * details.
     *
     * @param file         the file to process
     * @param scanInstance the scan instance associated with the file
     */
    void processFile(File file, Scan scanInstance);

    /**
     * Stops any ongoing scans.
     * This method will terminate the current scanning process if one is active.
     */
    void stopScan();
}
