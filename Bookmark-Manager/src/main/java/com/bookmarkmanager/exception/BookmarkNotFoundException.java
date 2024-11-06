package com.bookmarkmanager.exception;

public class BookmarkNotFoundException extends RuntimeException{
  public BookmarkNotFoundException(String message) {
    super(message);
  }
}
