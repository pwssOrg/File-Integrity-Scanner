package org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.scan;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan.Scan;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_integrity_controller.RetrieveRecentScansRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_integrity_controller.StartAllRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_integrity_controller.StartScanByIdRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.response.file_integrity_controller.LiveFeedResponse;
import org.pwss.file_integrity_scanner.exception.file_integrity_scanner.scan.NoActiveMonitoredDirectoriesException;
import org.pwss.file_integrity_scanner.exception.file_integrity_scanner.scan.ScanAlreadyRunningException;

/**
 * Service interface for managing directory scans.
 * This interface defines methods to scan directories, stop ongoing scans,
 * and provides the ability to scan all monitored directories or a specific one.
 */
public interface ScanService {
        /**
         * Initiates scanning of all monitored directories.
         * This method will start the scanning process for each directory that is being
         * monitored within the system.
         *
         * @param request A {@link StartAllRequest} containing parameters to
         *                initiate the scan.
         *                The maximum file size for hash extraction attempts cannot be
         *                null.
         * @throws ScanAlreadyRunningException           if there's already an active
         *                                               scan running.
         * @throws NoActiveMonitoredDirectoriesException if no directories are currently
         *                                               being monitored.
         * @throws SecurityException                     If validation of the request
         *                                               fails or any
         *                                               security-related issue occurs.
         */
        void scanAllDirectories(StartAllRequest request)
                        throws ScanAlreadyRunningException, NoActiveMonitoredDirectoriesException, SecurityException;

        /**
         * Initiates scanning of a single monitored directory.
         * <p>
         * This method starts the scanning process for the specified monitored
         * directory. It ensures that only the provided directory is scanned,
         * rather than all monitored directories.
         *
         * @param request A {@link StartScanByIdRequest} containing parameters to
         *                initiate the scan.
         *                The maximum file size for hash extraction attempts cannot be
         *                null and
         *                must specify the ID of the monitored directory to be scanned.
         * @throws ScanAlreadyRunningException           if there's already an active
         *                                               scan running.
         * @throws NoActiveMonitoredDirectoriesException if no directories are currently
         *                                               being monitored.
         * @throws SecurityException                     If validation of the request
         *                                               fails or any
         *                                               security-related issue occurs.
         * @throws NoSuchElementException                If the specified monitored
         *                                               directory could not be found in
         *                                               the repository layer.
         */
        void scanSingleDirectory(StartScanByIdRequest request)
                        throws ScanAlreadyRunningException, NoActiveMonitoredDirectoriesException, SecurityException,
                        NoSuchElementException;

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

        /**
         * Retrieves the most recent scan from the repository layer.
         *
         * @return an {@code Optional} containing the most recent {@link Scan} if found,
         *         or empty if no scans are available
         */
        Optional<Scan> getMostRecentScan();

        /**
         * Retrieves a list of the most recent scans based on the parameters provided in
         * the request object.
         * This method fetches multiple scan records from the database according to the
         * filtering criteria
         * specified in the {@link RetrieveRecentScansRequest} object. It may throw a
         * SecurityException if
         * the validation fails.
         *
         * @param request an object containing parameters that define which scans should
         *                be retrieved
         * @return a list of {@link Scan} objects representing the most recent scans
         * @throws SecurityException if the validation fails
         */
        List<Scan> getMostRecentScans(RetrieveRecentScansRequest request) throws SecurityException;

        /**
         * Retrieves a list of the most recent scans based on the number of active
         * directories.
         *
         * This method fetches scan records from the database that are relevant to the
         * current state of active
         * directories,
         * and returns them in a list. The criteria for determining "most recent" can
         * vary, but it generally
         * means
         * scans performed closest to the present time.
         *
         * @return a list of {@link Scan} objects representing the most recent scans
         *         based on active directory count
         */
        List<Scan> getMostRecentScansBasedOnNrOfActiveDirectories();

        /**
         * Finds a scan by its ID in the database.
         *
         * @param id the unique identifier of the scan to find
         * @return an {@code Optional} containing the {@link Scan} with the specified ID
         *         if found,
         *         or empty if no such scan exists
         */
        Optional<Scan> findById(Integer id);

        /**
         * Retrieves the live feed response containing information about whether a scan
         * is running
         * and the current live feed text.
         *
         * @return LiveFeedResponse object containing:
         *         - A boolean indicating if a scan is currently running
         *         - The string representation of the current live feed data
         */
        LiveFeedResponse getLiveFeed();

}
