package com.bookmarkmanager.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(BookmarkNotFoundException.class)
  public ResponseEntity<String> handleBookmarkNotFound(BookmarkNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(RateLimitException.class)
  public ResponseEntity<String> handleIllegalArgumentException(RateLimitException ex) {
    return ResponseEntity.status(429).body(ex.getMessage());
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<String> resourceNotFoundException(ResourceNotFoundException ex){
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }
  //Common Exception Handler
  @ExceptionHandler(BookmarkManagerException.class)
  public ResponseEntity<BookmarkManagerException.ErrorResponse> BookmarkManagerException(BookmarkManagerException ex){
    BookmarkManagerException.ErrorResponse errorResponse = new BookmarkManagerException.ErrorResponse(ex.getMessage(), ex.getErrors());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }
}
