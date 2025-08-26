package org.pwss.file_integrity_scanner.controller.scan_summary;

import java.util.LinkedList;
import java.util.List;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan_summary.ScanSummary;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.history_controller.GetSummaryForFileRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.history_controller.GetSummaryForScanRequest;
import org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.scan_summary.ScanSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for various scan summary actions.
 */
@RestController
@RequestMapping("/summary")
public class ScanHistoryController {

    private final org.slf4j.Logger log;
    private final ScanSummaryService service;

    @Autowired
    public ScanHistoryController(ScanSummaryService service) {
        this.service = service;
        this.log = org.slf4j.LoggerFactory.getLogger(ScanHistoryController.class);
    }

    @GetMapping("/most-recent")
    @PreAuthorize("hasAuthority('AUTHORIZED')")
    public ResponseEntity<List<ScanSummary>> getMostRecentScanSummaries() {

        List<ScanSummary> scanSummaries = service.getMostRecentScanSummary();
        if (scanSummaries.isEmpty()) {
            log.debug("List of scanSummaries is empty");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(scanSummaries, HttpStatus.OK);

    }

    @PostMapping("/file")
    @PreAuthorize("hasAuthority('AUTHORIZED')")
    public ResponseEntity<List<ScanSummary>> getSummaryForFile(@RequestBody GetSummaryForFileRequest request) {
        List<ScanSummary> scanSummaries = new LinkedList<>();
        try {
            scanSummaries = service.getScanSummaryForFile(request);
        } catch (SecurityException securityException) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (scanSummaries.isEmpty()) {
            log.debug("List of scanSummaries is empty");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(scanSummaries, HttpStatus.OK);

    }

    @PostMapping("/scan")
    @PreAuthorize("hasAuthority('AUTHORIZED')")
    public ResponseEntity<List<ScanSummary>> getSummaryForScan(@RequestBody GetSummaryForScanRequest request) {
        List<ScanSummary> scanSummaries = new LinkedList<>();

        try {
            scanSummaries = service.getScanSummaryForScan(request);
        }

        catch (SecurityException securityException) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (scanSummaries.isEmpty()) {
            log.debug("List of scanSummaries is empty");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(scanSummaries, HttpStatus.OK);

    }

}
