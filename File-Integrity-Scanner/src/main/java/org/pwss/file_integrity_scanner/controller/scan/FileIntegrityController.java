package org.pwss.file_integrity_scanner.controller.scan;

import java.util.List;
import java.util.Optional;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.diff.Diff;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.monitored_directory.MonitoredDirectory;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan.Scan;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_integrity_controller.RetrieveRecentScansRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_integrity_controller.ScanIntegrityDiffRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_integrity_controller.StartScanByIdRequest;
import org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.diff.IntegrityService;
import org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.monitored_directory.MonitoredDirectoryServiceImpl;
import org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.scan.ScanServiceImpl;
import org.pwss.file_integrity_scanner.exception.file_integrity_scanner.NoActiveMonitoredDirectoriesException;
import org.pwss.file_integrity_scanner.exception.file_integrity_scanner.ScanAlreadyRunningException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * REST controller for managing file integrity scans.
 */
@RestController
@RequestMapping("/scan")
public class FileIntegrityController {

    private final ScanServiceImpl scanService;

    private final MonitoredDirectoryServiceImpl monitoredDirectoryService;

    private final IntegrityService integrityService;

    private final org.slf4j.Logger log;

    /**
     * Constructs a new FileIntegrityController with the specified services.
     *
     * @param scanService               The service to handle file integrity scans
     * @param monitoredDirectoryService The service to manage monitored directories
     */
    @Autowired
    public FileIntegrityController(ScanServiceImpl scanService,
            MonitoredDirectoryServiceImpl monitoredDirectoryService,
            IntegrityService integrityService) {
        this.scanService = scanService;
        this.monitoredDirectoryService = monitoredDirectoryService;
        this.integrityService = integrityService;
        this.log = org.slf4j.LoggerFactory.getLogger(FileIntegrityController.class);
    }

    /**
     * Starts a file integrity scan for all directories, requires AUTHORIZED role.
     *
     * @return A {@link ResponseEntity} With:
     *         - Status 200 (OK) and a success message in the response body if the
     *         scan is successfully started or,
     *         - Status 401 (Unauthorized) or,
     *         - Status 412 (Precondition Failed) and an error message in the
     *         response body if no monitored directories are
     *         found using a Response Entity from
     *         {@link #noActiveMonitoredDirectoriesResponseEntity(NoActiveMonitoredDirectoriesException)}
     *         or,
     *         - Status 425 (TOO_EARLY) using a Response Entity from
     *         {@link #scanAlreadyRunningResponseEntity(ScanAlreadyRunningException)}
     *
     */
    @Operation(summary = "Starts a file integrity scan for all directories", description = "Requires AUTHORIZED role.", security = @SecurityRequirement(name = "JSession Token"), responses = {
            @ApiResponse(responseCode = "200", description = "Success message indicating the scan has started successfully.", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized. User doesn't have AUTHORIZED role."),
            @ApiResponse(responseCode = "412", description = "Precondition Failed. No active monitored directories found.", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "425", description = "Too Early. Scan is already running.", content = @Content(schema = @Schema(type = "string")))
    })
    @GetMapping("/start/all")
    @PreAuthorize("hasAuthority('AUTHORIZED')")
    public ResponseEntity<String> startFileIntegrityScan() {

        try {
            scanService.scanAllDirectories();
        }

        catch (NoActiveMonitoredDirectoriesException nMonitoredDirectoriesException) {
            return noActiveMonitoredDirectoriesResponseEntity(nMonitoredDirectoriesException);
        }

        catch (ScanAlreadyRunningException sRunningException) {
            return scanAlreadyRunningResponseEntity(sRunningException);
        }

        return new ResponseEntity<>(
                "Successfully started the scan",
                HttpStatus.OK);

    }

    /**
     * Starts a file integrity scan for a specific monitored directory, requires
     * AUTHORIZED role.
     *
     * @param request The {@link StartScanByIdRequest} containing the ID of the
     *                monitored directory to scan
     * @return A {@link ResponseEntity} With:
     *         - Status 200 (OK) and a success message in the response body if the
     *         scan is successfully started or,
     *         - Status 401 (Unauthorized) or,
     *         - Status 404 (Not Found) and an error message in the response body if
     *         no monitored directory is found
     *         or,
     *         - Status 412 Precondition Failed and an error message in the response
     *         body if no monitored directories are
     *         found using a Response Entity from
     *         {@link #noActiveMonitoredDirectoriesResponseEntity(NoActiveMonitoredDirectoriesException)}
     *         or,
     *         - Status 425 (TOO_EARLY) using a Response Entity from
     *         {@link #scanAlreadyRunningResponseEntity(ScanAlreadyRunningException)}
     * 
     */
    @Operation(summary = "Start File Integrity Scan for Monitored Directory by ID", description = "This endpoint allows authorized users to start a file integrity scan for a monitored directory identified by its ID.", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully started the scan", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - user not authorized to perform this action"),
            @ApiResponse(responseCode = "404", description = "No MonitoredDirectory found with the provided ID", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "412", description = "Precondition Failed - Monitored directory is not active"),
            @ApiResponse(responseCode = "425", description = "TOO_EARLY", content = @Content(schema = @Schema(type = "string")))

    })
    @PostMapping("/start/id")
    @PreAuthorize("hasAuthority('AUTHORIZED')")
    public ResponseEntity<String> startFileIntegrityScanForMonitoredDirectoryById(
            @RequestBody StartScanByIdRequest request) {

        final Optional<MonitoredDirectory> oMonitoredDirectory = monitoredDirectoryService
                .findById(request.id());

        if (oMonitoredDirectory.isPresent()) {

            log.debug("Monitored Directory found with id - {}", oMonitoredDirectory.get().getId());
            try {
                scanService.scanSingleDirectory(oMonitoredDirectory.get());
            } catch (ScanAlreadyRunningException sRunningException) {
                return scanAlreadyRunningResponseEntity(sRunningException);
            } catch (NoActiveMonitoredDirectoriesException nMonitoredDirectoriesException) {
                return noActiveMonitoredDirectoriesResponseEntity(nMonitoredDirectoriesException);
            }
            return new ResponseEntity<>(
                    "Successfully started the scan",
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No MonitoredDirectory was found at the ID you have provided",
                    HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Stops a file integrity scan, requires AUTHORIZED role.
     *
     * @return A response indicating the stop of the scan
     */
    @Operation(summary = "Stop File Integrity Scan", description = "Stops a file integrity scan. Requires AUTHORIZED role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File integrity scan stopped successfully", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - user not authorized to perform this action"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/stop")
    @PreAuthorize("hasAuthority('AUTHORIZED')")
    public ResponseEntity<String> stopFileIntegrityScan() {

        scanService.stopScan();
        return new ResponseEntity<>(
                "Stopped Scan",
                HttpStatus.OK);
    }

    /**
     * Checks if a file integrity scan is currently running. Requires AUTHORIZED
     * role.
     *
     * @return A response indicating whether the file integrity scan is running or
     *         not
     */
    @Operation(summary = "Check File Integrity Scan Status", description = "Checks if a file integrity scan is currently running. Requires AUTHORIZED role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File integrity scan status retrieved successfully", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - user not authorized to perform this action"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/status")
    @PreAuthorize("hasAuthority('AUTHORIZED')")
    public ResponseEntity<Boolean> isFileIntegrityScanRunning() {
        Boolean isRunning = scanService.isScanRunning();
        return new ResponseEntity<>(isRunning, HttpStatus.OK);
    }

    /**
     * Endpoint to retrieve the most recent scans based on a specified count.
     *
     * This endpoint returns a list of scan objects that were performed most
     * recently,
     * limited by the number provided in the RetrieveRecentScansRequest. It requires
     * the caller to have 'AUTHORIZED' authority.
     *
     * @param request The request object containing the number of scans to retrieve.
     * @return A ResponseEntity with:
     *         - 200 OK: A list of Scan objects if scans are found
     *         - 401 Unauthorized: If the user does not have AUTHORIZED role
     *         - 404 Not Found: If no scans exist for the given request criteria
     */
    @Operation(summary = "Get the X most recent scans", description = "This endpoint retrieves a list of scan objects that were performed most recently, "
            +
            "limited by the number provided in the RetrieveRecentScansRequest.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved (X) scans"),
            @ApiResponse(responseCode = "401", description = "Unauthorized. User doesn't have AUTHORIZED role."),
            @ApiResponse(responseCode = "404", description = "No scans found for the given criteria")
    })
    @PostMapping("/most-recent")
    @PreAuthorize("hasAuthority('AUTHORIZED')")

    public ResponseEntity<List<Scan>> getMostRecentScans(@RequestBody RetrieveRecentScansRequest request) {

        List<Scan> scans = scanService.getMostRecentScans(request);
        if (scans.isEmpty()) {
            log.debug("List of scans is empty");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(scans, HttpStatus.OK);

    }

    /**
     * Endpoint to retrieve the most recent scans based on active monitored
     * directories.
     *
     * This endpoint returns a list of scan objects that were performed in the most
     * recently active
     * monitored directories, limited by the number of currently active monitored
     * directories. For example,
     * if there are 3 active monitored directories in the system, this method will
     * return the 3 most recent scans.
     * It requires the caller to have 'AUTHORIZED' authority.
     *
     * @return A ResponseEntity with:
     *         - 200 OK: A list of Scan objects if scans are found
     *         - 401 Unauthorized: If the user does not have AUTHORIZED role
     *         - 404 Not Found: If no scans exist for active directories
     */
    @Operation(summary = "Get most recent scans based on active monitored directories", description = "This endpoint retrieves a list of scan objects that were performed in the most recently active monitored directories, limited by the number of currently active monitored directories. For example, if there are 3 active monitored directories in the system, this method will return the 3 most recent scans.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved scans"),
            @ApiResponse(responseCode = "401", description = "Unauthorized. User doesn't have AUTHORIZED role."),
            @ApiResponse(responseCode = "404", description = "No scans found for active directories")
    })
    @GetMapping("/active-directory/most-recent")
    @PreAuthorize("hasAuthority('AUTHORIZED')")

    public ResponseEntity<List<Scan>> getMostRecentScansBasedOnNrOfActiveDirectories() {

        List<Scan> scans = scanService.getMostRecentScansBasedOnNrOfActiveDirectories();
        if (scans.isEmpty()) {
            log.debug("List of scans is empty");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(scans, HttpStatus.OK);

    }

    /**
     * Endpoint to get file integrity failures from a scan.
     *
     * This endpoint retrieves a list of differences (diffs) between expected and
     * actual file states based on a
     * provided scan request.
     * It requires the caller to have 'AUTHORIZED' authority.
     *
     * @param request The IntegrityDiffByScanRequest containing details about which
     *                scan's diffs should be retrieved
     * @return A ResponseEntity with:
     *         - 200 OK: A list of Diff objects if differences are found
     *         - 401 Unauthorized: If the user does not have the AUTHORIZED role
     *         - 404 Not Found: If no diffs are found for the provided scan request
     */
    @Operation(summary = "Retrieve file integrity failures from a scan", description = "This endpoint returns differences between expected and actual file states based on a given scan request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A list of Diff objects is returned"),
            @ApiResponse(responseCode = "401", description = "Unauthorized. User doesn't have AUTHORIZED role."),
            @ApiResponse(responseCode = "404", description = "No diffs found for the provided scan request")
    })
    @PostMapping("/diff")
    @PreAuthorize("hasAuthority('AUTHORIZED')")
    public ResponseEntity<List<Diff>> getFileIntegrityFailsFromScan(@RequestBody ScanIntegrityDiffRequest request) {

        List<Diff> dList = integrityService.retrieveDiffListFromScan(request);
        if (dList.isEmpty()) {
            log.debug("List of diffs is empty");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(dList, HttpStatus.OK);

    }

    /**
     * Creates a response entity when a scan is already running.
     *
     * This method logs an error message with the exception details and creates a
     * ResponseEntity containing an appropriate HTTP status code (TOO_EARLY) and
     * a user-friendly message indicating that a scan is currently in progress,
     * preventing new scans from being started at this time.
     *
     * @param e the ScanAlreadyRunningException that was thrown
     * @return a ResponseEntity with an HTTP TOO_EARLY status code and a message
     *         explaining why the request cannot be processed
     */
    private ResponseEntity<String> scanAlreadyRunningResponseEntity(ScanAlreadyRunningException e) {
        log.error("ScanAlreadyRunningException - {}", e.getMessage());
        return new ResponseEntity<>(
                "Scan is already running! Not possible to start a Scan at this time.\n\nTry again in a minute or two , maybe even seconds ;) :)",
                HttpStatus.TOO_EARLY);
    }

    /**
     * Handles the NoActiveMonitoredDirectoriesException by logging an error message
     * and returning a ResponseEntity with a conflict status.
     *
     * @param e The exception that was thrown when no active monitored directories
     *          were found.
     * @return A ResponseEntity containing an error message and a CONFLICT
     *         HttpStatus.
     */
    private ResponseEntity<String> noActiveMonitoredDirectoriesResponseEntity(NoActiveMonitoredDirectoriesException e) {
        log.error("NoActiveMonitoredDirectoriesException - {}", e.getMessage());
        return new ResponseEntity<>(
                "No active monitored directory found\nScan will not start!",
                HttpStatus.PRECONDITION_FAILED);
    }

}