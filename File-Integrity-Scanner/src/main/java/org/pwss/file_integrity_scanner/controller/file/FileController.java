package org.pwss.file_integrity_scanner.controller.file;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_controller.QuarantineFileRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_controller.UnQurantineFileRequest;
import org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.file.FileService;
import org.pwss.file_integrity_scanner.exception.file_integrity_scanner.file.QuarantineFailedException;
import org.pwss.file_integrity_scanner.exception.file_integrity_scanner.file.UnquarantineFailedException;
import org.pwss.quarantineManager_aes.dto.MetaDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * REST controller for managing file operations related to quarantine and
 * unquarantine actions.
 */
@RestController
@RequestMapping("/file")
public class FileController {

    /**
     * The service used to perform file operations like quarantining and
     * unquarantining files.
     */
    private final FileService service;

    /**
     * Constructor for the FileController. Initializes the FileService.
     *
     * @param fileService The FileService instance to be used by this controller.
     */
    @Autowired
    public FileController(FileService fileService) {
        this.service = fileService;
    }

    /**
     * Endpoint for quarantining a file.
     *
     * @param request The QuarantineFileRequest object containing details about the
     *                file to be quarantined.
     * @return ResponseEntity with MetaDataResult and appropriate HttpStatus.
     */
    @Operation(summary = "Quarantines a specified file", description = "This endpoint attempts to quarantine a specified file based on the given request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation. Returns metadata result with success status.", content = @Content(schema = @Schema(implementation = MetaDataResult.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - user not authorized to perform this action", content = @Content),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/quarantine")
    @PreAuthorize("hasAuthority('AUTHORIZED')")
    public ResponseEntity<MetaDataResult> qurantineFile(
            @RequestBody QuarantineFileRequest request) {
        final MetaDataResult response;
        try {
            response = service.quranantine(request);
        } catch (SecurityException securityException) {
            return new ResponseEntity<>(new MetaDataResult(false, "ERROR - Input Validation"),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (QuarantineFailedException quarantineFailedException) {
            return new ResponseEntity<>(new MetaDataResult(false, "ERROR - "+quarantineFailedException.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<MetaDataResult>(
                response,
                HttpStatus.OK);
    }

    /**
     * Endpoint for unquarantining a file.
     *
     * @param request The UnQurantineFileRequest object containing details about the
     *                file to be unquarantined.
     * @return ResponseEntity with MetaDataResult and appropriate HttpStatus.
     */
    @Operation(summary = "Unquarantines a specified file", description = "This endpoint attempts to unquarantine a specified file based on the given request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation. Returns metadata result with success status.", content = @Content(schema = @Schema(implementation = MetaDataResult.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - user not authorized to perform this action", content = @Content),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/unquarantine")
    @PreAuthorize("hasAuthority('AUTHORIZED')")
    public ResponseEntity<MetaDataResult> unQurantineFile(
            @RequestBody UnQurantineFileRequest request) {
        final MetaDataResult response;
        try {
            response = service.unQuarantine(request);
        } catch (SecurityException securityException) {
            return new ResponseEntity<>(new MetaDataResult(false, "ERROR - Input Validation"),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (UnquarantineFailedException unquarantineFailedException) {
            return new ResponseEntity<>(new MetaDataResult(false, "ERROR - Unquarantine Operation"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<MetaDataResult>(
                response,
                HttpStatus.OK);
    }
}
