package org.pwss.file_integrity_scanner.controller.scan;

import java.util.Optional;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.monitored_directory.MonitoredDirectory;

import org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.monitored_directory.MonitoredDirectoryServiceImpl;
import org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.scan.ScanServiceImpl;
import org.pwss.file_integrity_scanner.exception.file_integrity_scanner.ScanAlreadyRunningException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing file integrity scans.
 */
@RestController
@RequestMapping("/file-integrity")
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
     * @return A response indicating the start of the scan
     */
    @GetMapping("/start-scan")
    @PreAuthorize("hasAuthority('AUTHORIZED')")
    public ResponseEntity<String> startFileIntegrityScan() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("Authorities: {} ", authentication.getAuthorities());

        try {
            scanService.scanAllDirectories();
        } catch (ScanAlreadyRunningException sRunningException) {
            return scanAlreadyRunningResponseEntity(sRunningException);
        }
        return new ResponseEntity<>(
                "Sit back and relax friend :) ... while File Integrity Scanner scans the integrity your monitored files. \n\nStarted scan...",
                HttpStatus.OK);
    }

    /**
     * Starts a file integrity scan for a specific monitored directory, requires
     * AUTHORIZED role.
     *
     * @param scanMonitoredDirectoryRequest The request containing the ID of the
     *                                      monitored directory to scan
     * @return A response indicating the start of the scan or an error if the
     *         directory is not found
     */
    @GetMapping("/start-scan/{id}")
    @PreAuthorize("hasAuthority('AUTHORIZED')")
    public ResponseEntity<String> startFileIntegrityScanMonitoredDirectory(
            @PathVariable("id") Integer id) {

        final Optional<MonitoredDirectory> oMonitoredDirectory = monitoredDirectoryService
                .findById(id);

        if (oMonitoredDirectory.isPresent()) {

            try {
                scanService.scanSingleDirectory(oMonitoredDirectory.get());
            } catch (ScanAlreadyRunningException sRunningException) {
                return scanAlreadyRunningResponseEntity(sRunningException);
            }
            return new ResponseEntity<>(
                    "Scanning 1 Monitored Directory\n\nStarted scan...",
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
    @GetMapping("/stop-scan")
    @PreAuthorize("hasAuthority('AUTHORIZED')")
    public ResponseEntity<String> stopFileIntegrityScan() {

        scanService.stopScan();
        return new ResponseEntity<>(
                "Stopped Scan",
                HttpStatus.OK);
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

}