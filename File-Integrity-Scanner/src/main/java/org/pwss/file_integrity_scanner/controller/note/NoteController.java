package org.pwss.file_integrity_scanner.controller.note;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.note_controller.RestoreNoteRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.note_controller.UpdateNoteRequest;

import org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.note.NoteService;

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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST controller for various note actions.
 */
@RestController
@RequestMapping("/note")
public class NoteController {

   /**
    * Logger instance for logging messages.
    */
   private final org.slf4j.Logger log;

   /**
    * Service layer interface for handling note-related business logic.
    */
   private final NoteService service;

   /**
    * Constructs a new NoteController with the provided service.
    *
    * @param service The NoteService instance to be used by this controller.
    */
   @Autowired
   public NoteController(NoteService service) {
      this.service = service;
      this.log = org.slf4j.LoggerFactory.getLogger(NoteController.class);
   }

   /**
    * Updates a note with the information provided in the request body.
    *
    * This endpoint is protected and requires the user to have 'AUTHORIZED'
    * authority.
    *
    * @param request The UpdateNoteRequest object containing data for updating the
    *                note.
    * @return ResponseEntity containing a boolean indicating success or failure,
    *         with appropriate HTTP status
    *         code.
    */
   @Operation(summary = "Update Note", description = "Updates a note with the information provided in the request body.")
   @ApiResponses(value = {
         @ApiResponse(responseCode = "200", description = "Note updated successfully", content = {
               @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class)) }),
               @ApiResponse(responseCode = "400", description = "Security exception occurred")
   })
   @PostMapping("/update")
   @PreAuthorize("hasAuthority('AUTHORIZED')")
   public ResponseEntity<Boolean> updateNoteEndpoint(
         @RequestBody UpdateNoteRequest request) {
      boolean response;
      try {
         response = service.updateNote(request);
         log.debug("Note updated successfully: {}", response);
      }

      catch (SecurityException securityException) {
         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }

      return new ResponseEntity<>(response, HttpStatus.OK);
   }

   /**
    * Restores a note based on the provided request.
    */
   @PostMapping("/restore")
   @PreAuthorize("hasAuthority('AUTHORIZED')")
   @Operation(summary = "Restores an old note", description = "Endpoints that restores a deleted note from its previous state.")
   @ApiResponses(value = {
         @ApiResponse(responseCode = "200", description = "Note restored successfully"),
         @ApiResponse(responseCode = "400", description = "Could not restore note due to security restrictions or validation failures"),
         @ApiResponse(responseCode = "401", description = "Unauthorized. User doesn't have AUTHORIZED role.")
   })
   public ResponseEntity<Boolean> restoreNoteEndpoint(@RequestBody RestoreNoteRequest request) {
      boolean response;

      try {
         response = service.restoreOldNote(request);
         log.debug("Note restored successfully: {}", response);
      }

      catch (SecurityException securityException) {
         log.error("Could not restore note from the input request object", securityException);
         return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
      }

      return new ResponseEntity<>(response, HttpStatus.OK);
   }

}