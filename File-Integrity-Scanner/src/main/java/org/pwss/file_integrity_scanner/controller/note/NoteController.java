package org.pwss.file_integrity_scanner.controller.note;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.file_integrity_controller.UpdateNoteRequest;

import org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.note.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for various note actions.
 */

 //TODO Java Docs And Refactor
@RestController
@RequestMapping("/note")
public class NoteController {

   private final org.slf4j.Logger log;
   private final NoteService service;

   @Autowired
   public NoteController(NoteService service) {
      this.service = service;
      this.log = org.slf4j.LoggerFactory.getLogger(NoteController.class);
   }

   @PostMapping("/update")
   @PreAuthorize("hasAuthority('AUTHORIZED')")
   public ResponseEntity<Boolean> updateNoteEndpoint(
         @RequestBody UpdateNoteRequest request) {
      boolean response;
      try {
         response = service.updateNote(request);
      }

      catch (SecurityException securityException) {
         return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

      }

      return new ResponseEntity<>(response, HttpStatus.CREATED);
   }

}