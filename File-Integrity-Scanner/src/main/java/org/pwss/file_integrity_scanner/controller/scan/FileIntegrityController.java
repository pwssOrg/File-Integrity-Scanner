package org.pwss.file_integrity_scanner.controller.scan;

import org.pwss.file_integrity_scanner.dsr.service.scan.ScanServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/file-integrity")
public class FileIntegrityController {

    @Autowired
    private final ScanServiceImpl scanService;

    public FileIntegrityController(ScanServiceImpl scanService) {
        this.scanService = scanService;
    }

    // Endpoint to start a file integrity scan, requires AUTHORIZED role
    @PostMapping("/start-scan")
    @PreAuthorize("hasRole('AUTHORIZED')") // Ensure the user has the required role
    public final ResponseEntity<String> startFileIntegrityScan() {

        scanService.scanAllDirectories();
        return new ResponseEntity<>(
                "Sit back and relax Sir while File Integrity Scanner scans the integrity of your important files :)\n\nStarted scan...",
                HttpStatus.OK);
    }

    // Endpoint to Stop a file integrity scan, requires AUTHORIZED role
    @PostMapping("/stop-scan")
    @PreAuthorize("hasRole('AUTHORIZED')") // Ensure the user has the required role
    public final ResponseEntity<String> stopFileIntegrityScan() {

        scanService.stopScan();
        return new ResponseEntity<>(
                "Stopped Scan",
                HttpStatus.ACCEPTED);
    }
}