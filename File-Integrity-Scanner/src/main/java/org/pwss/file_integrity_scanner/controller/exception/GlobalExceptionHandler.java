package org.pwss.file_integrity_scanner.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleException(Exception ex) {
    log.error("Error message - {}", ex.getMessage());
    if (ex instanceof AccessDeniedException)
      return new ResponseEntity<>("Access denied", HttpStatus.UNAUTHORIZED);
    else
      return new ResponseEntity<>("An error occurred when calling an endpoint: ", HttpStatus.INTERNAL_SERVER_ERROR);
  }
}