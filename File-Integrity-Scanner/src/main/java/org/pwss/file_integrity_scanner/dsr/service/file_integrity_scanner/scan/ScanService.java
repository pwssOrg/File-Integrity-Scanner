package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.scan;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.monitored_directory.MonitoredDirectory;
import org.pwss.file_integrity_scanner.exception.file_integrity_scanner.NoActiveMonitoredDirectoriesException;
import org.pwss.file_integrity_scanner.exception.file_integrity_scanner.ScanAlreadyRunningException;

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
     * 
     * @throws ScanAlreadyRunningException
     * @throws NoActiveMonitoredDirectoriesException
     */
    void scanAllDirectories()
            throws ScanAlreadyRunningException, NoActiveMonitoredDirectoriesException;

    /**
     * Initiates scanning of a single monitored directory.
     * <p>
     * This method starts the scanning process for the specified monitored
     * directory.
     * It ensures that only the provided directory is scanned, rather than all
     * monitored directories.
     *
     * @param monitoredDirectory the monitored directory to be scanned
     * @throws ScanAlreadyRunningException
     * @throws NoActiveMonitoredDirectoriesException
     */
    void scanSingleDirectory(MonitoredDirectory monitoredDirectory)
            throws ScanAlreadyRunningException, NoActiveMonitoredDirectoriesException;

    /**
     * Stops any ongoing scans.
     * This method will terminate the current scanning process if one is active.
     */
    void stopScan();

    /**
     * Checks if a scan is currently running.
     *
     * This method returns the status of whether any scanning process is active or
     * not.
     * It helps in determining if the system is busy with a scan operation at the
     * moment.
     *
     * @return true if a scan is currently running, false otherwise
     */
    Boolean isScanRunning();
}
