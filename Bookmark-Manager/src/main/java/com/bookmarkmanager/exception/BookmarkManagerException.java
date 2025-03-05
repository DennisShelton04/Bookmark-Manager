package com.bookmarkmanager.exception;

import java.util.List;
import java.util.Map;

public class BookmarkManagerException  extends RuntimeException{

  private final List<Map<String, String>> errors;
  public record ErrorResponse(
          String message,
          List<Map<String, String>> errors
  ) {}

  public BookmarkManagerException(String message,List<Map<String, String>> errors) {
    super(message);
    this.errors = errors;
  }

  public List<Map<String, String>> getErrors() {
    return errors;
  }
}
