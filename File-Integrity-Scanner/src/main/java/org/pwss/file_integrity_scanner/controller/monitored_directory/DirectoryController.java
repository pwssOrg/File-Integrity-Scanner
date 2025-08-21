package org.pwss.file_integrity_scanner.controller.monitored_directory;

import java.util.Optional;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.monitored_directory.MonitoredDirectory;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.CreateMonitoredDirectoryRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.response.CreateMonitoredDirectoryResponse;
import org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.monitored_directory.MonitoredDirectoryService;
import org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.monitored_directory.MonitoredDirectoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for various monitored directory actions.
 */
@RestController
@RequestMapping("/directory")
public class DirectoryController {

   private final MonitoredDirectoryServiceImpl mDirectoryServiceImpl;

   /**
    * Constructs a new instance of the {@code DirectoryController} class,
    * initializing it with the provided {@link MonitoredDirectoryServiceImpl}.
    *
    * @param monitoredDirectoryService the service to handle business logic
    *                                  related to monitored directories
    */

   @Autowired
   public DirectoryController(MonitoredDirectoryServiceImpl mDirectoryServiceImpl) {
      this.mDirectoryServiceImpl = mDirectoryServiceImpl;
   }

   /**
    * Creates a new monitored directory based on the request.
    *
    * This method handles HTTP POST requests to create a new monitored directory.
    * It requires the caller to have 'AUTHORIZED' authority. The creation process
    * is managed by the {@link MonitoredDirectoryService}, which is injected via
    * constructor injection.
    *
    * @param request the {@link CreateMonitoredDirectoryRequest} containing details
    *                for the new monitored
    *                directory
    * @return a {@code ResponseEntity} with HTTP status and optionally a body:
    *         - {@code HttpStatus.CREATED} if the creation was successful, no
    *         content in response body.
    *         - {@code HttpStatus.UNPROCESSABLE_ENTITY} if the request could not be
    *         processed
    */
   @PostMapping("/new")
   @PreAuthorize("hasAuthority('AUTHORIZED')")
   public ResponseEntity<CreateMonitoredDirectoryResponse> createNewMonitoredDirectory(
         @RequestBody CreateMonitoredDirectoryRequest request) {

      CreateMonitoredDirectoryResponse monitoredDirectoryResponse = mDirectoryServiceImpl
            .createMonitoredDirectoryFromRequest(request);

      if (monitoredDirectoryResponse == null)
         return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
      else
         return new ResponseEntity<>(HttpStatus.CREATED);
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
   
   @GetMapping("/newbaseline/{id}")
   @PreAuthorize("hasAuthority('AUTHORIZED')")
   public ResponseEntity<String> setNewBaseline(@PathVariable("id") Integer id) {

      Optional<MonitoredDirectory> oMonitoredDirectory = mDirectoryServiceImpl
            .findById(id);

      if (oMonitoredDirectory.isPresent()) {

         if (mDirectoryServiceImpl.resetBaseline(oMonitoredDirectory.get())) {
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
