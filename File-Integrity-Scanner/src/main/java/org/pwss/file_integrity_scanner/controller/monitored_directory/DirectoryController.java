package org.pwss.file_integrity_scanner.controller.monitored_directory;

import java.util.List;
import java.util.Optional;

import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.entities.monitored_directory.MonitoredDirectory;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.directory_controller.CreateMonitoredDirectoryRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.directory_controller.GetDirectoryByIdRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.directory_controller.ResetBaseLineRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.request.directory_controller.UpdateMonitoredDirectoryRequest;
import org.pwss.file_integrity_scanner.dsr.domain.file_integrity_scanner.model.response.directory_controller.CreateMonitoredDirectoryResponse;
import org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.monitored_directory.MonitoredDirectoryService;
import org.pwss.file_integrity_scanner.dsr.service.file_integrity_scanner.monitored_directory.MonitoredDirectoryServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for various monitored directory actions.
 */
@RestController
@RequestMapping("/directory")
public class DirectoryController {

   private final MonitoredDirectoryServiceImpl monitoredDirectoryService;

   private final org.slf4j.Logger log;

   @Value("${directory.endpoint-code}")
   private Long ENDPOINT_CODE;

   /**
    * Constructs a new instance of the {@code DirectoryController} class,
    * initializing it with the provided {@link MonitoredDirectoryServiceImpl}.
    *
    * @param monitoredDirectoryService the service to handle business logic
    *                                  related to monitored directories
    */

   @Autowired
   public DirectoryController(MonitoredDirectoryServiceImpl monitoredDirectoryService) {
      this.monitoredDirectoryService = monitoredDirectoryService;
      this.log = org.slf4j.LoggerFactory.getLogger(DirectoryController.class);
      
   }

   @PostMapping("/id")
   @PreAuthorize("hasAuthority('AUTHORIZED')")
   public ResponseEntity<?> getDirectoryById(@RequestBody GetDirectoryByIdRequest request) {

      Optional<MonitoredDirectory> oDirectory = monitoredDirectoryService.findById(request.directoryId());

      if (oDirectory.isPresent()) {

         return new ResponseEntity<MonitoredDirectory>(oDirectory.get(), HttpStatus.OK);
      }

      else {
         return new ResponseEntity<>("Did not find any directory for your provided id", HttpStatus.NOT_FOUND);
      }

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

      CreateMonitoredDirectoryResponse monitoredDirectoryResponse = monitoredDirectoryService
            .createMonitoredDirectoryFromRequest(request);

      if (monitoredDirectoryResponse == null)
         return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
      else
         return new ResponseEntity<>(HttpStatus.CREATED);
   }

   /**
    * Creates a new baseline for a specific monitored directory. This operation
    * requires
    * the 'AUTHORIZED' role.
    *
    * @param request The request object containing:
    *                - endpointCode: A code that needs to match with the predefined
    *                ENDPOINT_CODE,
    *                - directoryId: The ID of the monitored directory for which the
    *                new baseline will be created.
    * @return A {@link ResponseEntity} with:
    *         - Status 200 (OK) and a success message in the response body if the
    *         baseline is successfully reset, or
    *         - Status 404 (Not Found) and an error message in the response body if
    *         no monitored directory is found
    *         at the given ID,
    *         - Status 500 (Internal Server Error) with an appropriate error
    *         message if the baseline could not be
    *         reset,
    *         - Status 203 (Non-Authoritative Information) and an error message in
    *         the response body if the provided
    *         endpointCode does not match.
    */
   @PutMapping("new-baseline")
   @PreAuthorize("hasAuthority('AUTHORIZED')")
   public ResponseEntity<String> setNewBaseline(@RequestBody ResetBaseLineRequest request) {

      log.debug("new-baseline endpoint");

      if (request.endpointCode().longValue() == ENDPOINT_CODE) {

         Optional<MonitoredDirectory> oMonitoredDirectory = monitoredDirectoryService
               .findById(request.directoryId());

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

      } else {

         return new ResponseEntity<>("The Provided code did not match",
               HttpStatus.NON_AUTHORITATIVE_INFORMATION);

      }

   }

   /**
    * Retrieves a list of all monitored directories. This operation requires
    * the 'AUTHORIZED' role.
    *
    * @return A {@link ResponseEntity} containing:
    *         - Status 200 (OK) and a list of {@link MonitoredDirectory} objects in
    *         the response body if the
    *         retrieval is successful, or
    *         - Status 500 (Internal Server Error) with an appropriate error
    *         message if no monitored directories
    *         could be retrieved.
    */
   @GetMapping("/all")
   @PreAuthorize("hasAuthority('AUTHORIZED')")
   public ResponseEntity<List<MonitoredDirectory>> getAll() {

      Optional<List<MonitoredDirectory>> mOptional = monitoredDirectoryService.findAll();

      if (mOptional.isPresent())
         return new ResponseEntity<>(mOptional.get(), HttpStatus.OK);
      else
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

   }

   /**
    * Updates an existing monitored directory based on the provided request.
    * This operation requires the 'AUTHORIZED' role.
    *
    * @param request The {@link UpdateMonitoredDirectoryRequest} containing the
    *                information needed to update a
    *                monitored directory.
    * @return A {@link ResponseEntity} containing:
    *         - Status 200 (OK) and a success message if the update is successful,
    *         or
    *         - Status 422 (Unprocessable Entity) if the update could not be
    *         processed.
    */
   @PutMapping("/update")
   @PreAuthorize("hasAuthority('AUTHORIZED')")
   public ResponseEntity<String> update(@RequestBody UpdateMonitoredDirectoryRequest request) {

      Boolean updateSuccessful = monitoredDirectoryService.updateMonitoredDirectoryFromRequest(request);

      if (updateSuccessful)
         return new ResponseEntity<>("Successful Update!", HttpStatus.OK);
      else
         return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
   }

}
