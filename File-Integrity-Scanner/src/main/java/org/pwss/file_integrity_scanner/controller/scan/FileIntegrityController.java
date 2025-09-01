package org.pwss.file_integrity_scanner.controller.scan;


import java.util.Optional;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.monitored_directory.MonitoredDirectory;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_integrity_controller.StartScanByIdRequest;
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

    private final org.slf4j.Logger log;

    /**
     * Constructs a new FileIntegrityController with the specified services.
     *
     * @param scanService               The service to handle file integrity scans
     * @param monitoredDirectoryService The service to manage monitored directories
     */
    @Autowired
    public FileIntegrityController(ScanServiceImpl scanService,
            MonitoredDirectoryServiceImpl monitoredDirectoryService) {
        this.scanService = scanService;
        this.monitoredDirectoryService = monitoredDirectoryService;
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

            log.debug("Monitored Directory found with id - {}",oMonitoredDirectory.get().getId());
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