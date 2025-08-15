package org.pwss.file_integrity_scanner.controller.scan;

import java.util.Optional;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.monitored_directory.MonitoredDirectory;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.MonitoredDirectoryRequest;

import org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.monitored_directory.MonitoredDirectoryServiceImpl;
import org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.scan.ScanServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing file integrity scans.
 */
@RestController
@RequestMapping("/api/file-integrity")
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
    @PostMapping("/start-scan")
    @PreAuthorize("hasAuthority('AUTHORIZED')") // Ensure the user has the required role
    public ResponseEntity<String> startFileIntegrityScan() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("Authorities: {} ", authentication.getAuthorities());

        scanService.scanAllDirectories();
        return new ResponseEntity<>(
                "Sit back and relax Sir while File Integrity Scanner scans the integrity of your important files :)\n\nStarted scan...",
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
    @PostMapping("/start-scan/monitored-directory")
    @PreAuthorize("hasAuthority('AUTHORIZED')")
    public ResponseEntity<String> startFileIntegrityScanMonitoredDirectory(
            @RequestBody MonitoredDirectoryRequest scanMonitoredDirectoryRequest) {

        final Optional<MonitoredDirectory> oMonitoredDirectory = monitoredDirectoryService
                .findById(scanMonitoredDirectoryRequest.monitoredDirectoryId());

        if (oMonitoredDirectory.isPresent()) {

            scanService.scanSingleDirectory(oMonitoredDirectory.get());
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
    @PostMapping("/stop-scan")
    @PreAuthorize("hasAuthority('AUTHORIZED')") // Ensure the user has the required role
    public ResponseEntity<String> stopFileIntegrityScan() {

        scanService.stopScan();
        return new ResponseEntity<>(
                "Stopped Scan",
                HttpStatus.ACCEPTED);
    }

    /**
     * Creates a new baseline for a specific monitored directory, requires
     * AUTHORIZED role.
     *
     * @param scanMonitoredDirectoryRequest The request containing the ID of the
     *                                      monitored directory
     * @return A response indicating the creation of the new baseline or an error if
     *         the directory is not found
     */
    @PostMapping("/new-baseline")
    @PreAuthorize("hasAuthority('AUTHORIZED')") // Ensure the user has the required role
    public ResponseEntity<String> newBaseline(@RequestBody MonitoredDirectoryRequest scanMonitoredDirectoryRequest) {

        final Optional<MonitoredDirectory> oMonitoredDirectory = monitoredDirectoryService
                .findById(scanMonitoredDirectoryRequest.monitoredDirectoryId());

        if (oMonitoredDirectory.isPresent()) {

            if (monitoredDirectoryService.resetBaseline(oMonitoredDirectory.get())) {
                return new ResponseEntity<>(
                        "Your Baseline has been reset.\nA new Baseline will be created on your next scan! ",
                        HttpStatus.OK);
            }

            else {
                return new ResponseEntity<>("The baseline could not be reset.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        else {
            return new ResponseEntity<>("No MonitoredDirectory was found at the ID you have provided.",
                    HttpStatus.NOT_FOUND);
        }

    }

}