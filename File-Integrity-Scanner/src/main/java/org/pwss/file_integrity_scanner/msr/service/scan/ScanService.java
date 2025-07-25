package org.pwss.file_integrity_scanner.msr.service.scan;


import org.pwss.file_integrity_scanner.msr.domain.model.entities.monitored_directory.MonitoredDirectory;
import org.pwss.file_integrity_scanner.msr.domain.model.entities.scan.Scan;

/**
 * Service interface for managing directory scans.
 * This interface defines methods to scan directories, stop ongoing scans,
 * and provides the ability to scan all monitored directories or a specific one.
 */
public interface ScanService {
    /**
     * Initiates scanning of all monitored directories.
     * This method will start the scanning process for each directory that is being monitored
     * within the system.
     */
    void scanAllDirectories();

    /**
     * Scans a specific monitored directory using the provided scan instance.
     *
     * @param monitoredDirectory the directory to be scanned
     * @param scanInstance       the scan instance used for scanning the directory
     */
    void scanDirectory(MonitoredDirectory monitoredDirectory, Scan scanInstance);

    /**
     * Stops any ongoing scans.
     * This method will terminate the current scanning process if one is active.
     */
    void stopScan();
}
