package org.pwss.file_integrity_scanner.controller.scan_summary;

import java.util.LinkedList;
import java.util.List;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.file.File;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.scan_summary.ScanSummary;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.history_controller.GetSummaryForFileRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.history_controller.GetSummaryForScanRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.history_controller.SearchForFileRequest;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

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

    /**
     * Gets the most recent summaries of a scan .
     *
     * @return ResponseEntity containing a list of scan summaries or
     *         HttpStatus.NOT_FOUND if none exist
     */
    @Operation(summary = "Get Most Recent Summaries of a Scan", description = "Retrieves the most recent scan summaries.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved scan summaries"),
            @ApiResponse(responseCode = "401", description = "Unauthorized. User doesn't have AUTHORIZED role."),
            @ApiResponse(responseCode = "404", description = "No scan summaries found")
    })
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

    /**
     * Gets the scan summaries for a specific file.
     *
     * @param request The GetSummaryForFileRequest containing details about the file
     * @return ResponseEntity containing a list of scan summaries or
     *         HttpStatus.NOT_FOUND if none exist
     */
    @Operation(summary = "Get Scan Summaries for File", description = "Retrieves the scan summaries for a specific file.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved scan summaries"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized. User doesn't have AUTHORIZED role."),
            @ApiResponse(responseCode = "404", description = "No scan summaries found"),
    })
    @PostMapping("/file")
    @PreAuthorize("hasAuthority('AUTHORIZED')")
    public ResponseEntity<List<ScanSummary>> getSummaryForFile(@RequestBody GetSummaryForFileRequest request) {
        List<ScanSummary> scanSummaries = new LinkedList<>();
        try {
            scanSummaries = service.getScanSummaryForFile(request);
        } catch (SecurityException securityException) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (scanSummaries.isEmpty()) {
            log.debug("List of scanSummaries is empty");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(scanSummaries, HttpStatus.OK);

    }

    /**
     * Gets the scan summaries for a specific scan.
     *
     * @param request The GetSummaryForScanRequest containing details about the scan
     * @return ResponseEntity containing a list of scan summaries or
     *         HttpStatus.NOT_FOUND if none exist
     */
    @Operation(summary = "Get Scan Summaries for Scan", description = "Retrieves the scan summaries for a specific scan.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved scan summaries"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized. User doesn't have AUTHORIZED role."),
            @ApiResponse(responseCode = "404", description = "No scan summaries found"),
    })
    @PostMapping("/scan")
    @PreAuthorize("hasAuthority('AUTHORIZED')")
    public ResponseEntity<List<ScanSummary>> getSummaryForScan(@RequestBody GetSummaryForScanRequest request) {
        List<ScanSummary> scanSummaries = new LinkedList<>();

        try {
            scanSummaries = service.getScanSummaryForScan(request);
        }

        catch (SecurityException securityException) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (scanSummaries.isEmpty()) {
            log.debug("List of scanSummaries is empty");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(scanSummaries, HttpStatus.OK);

    }

    /**
     * Searches for files based on the provided criteria.
     *
     * @param request the search criteria encapsulated in a
     *                {@link SearchForFileRequest}
     *                object, which includes parameters like search query, limit,
     *                sort field,
     *                and sorting order (ascending/descending)
     * @return a ResponseEntity containing a list of matching files or appropriate
     *         HTTP status codes
     */
    @Operation(summary = "Search for files by basename", description = "Searches for files based on the provided search criteria.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Files found successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = File.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User does not have the required authority"),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity - Validation or security exception occurred"),
            @ApiResponse(responseCode = "404", description = "Not Found - No files found matching the criteria")
    })
    @PostMapping("/file/search")
    @PreAuthorize("hasAuthority('AUTHORIZED')")
    public ResponseEntity<List<File>> searchForFile(@RequestBody SearchForFileRequest request) {
        List<File> listOfFiles = new LinkedList<>();

        try {
            listOfFiles = service.findFilesByBasenameLikeIgnoreCase(request);
        }

        catch (SecurityException securityException) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (listOfFiles.isEmpty()) {
            log.debug("List of files is empty");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(listOfFiles, HttpStatus.OK);

    }

}
