package com.bookmarkmanager.bookmark;

import com.bookmarkmanager.bookmarkfolder.FolderRepository;
import com.bookmarkmanager.exception.BookmarkManagerException;
import com.bookmarkmanager.dto.BookmarkDTO;
import com.bookmarkmanager.exception.BookmarkNotFoundException;
import com.bookmarkmanager.exception.ResourceNotFoundException;
import com.bookmarkmanager.pojo.Bookmark;
import com.bookmarkmanager.pojo.Folder;
import lombok.RequiredArgsConstructor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {
  public static final String FIELD = "field";
  public static final String MESSAGE = "message";
  public static final String ERROR_CODE = "errorCode";
  private final BookmarkRepository bookmarkRepository;
  private final FolderRepository folderRepository;


  @Transactional
  public Bookmark createBookmark(Bookmark bookmark) {

    validateBookmark(bookmark);
    if (bookmark.getFolder() != null && bookmark.getFolder().getId() != null) {
      Optional<Folder> folderOptional = folderRepository.findById(bookmark.getFolder().getId());
      if (folderOptional.isPresent()) {
        bookmark.getFolder().setId(folderOptional.get().getId());
      } else {
        throw new IllegalArgumentException("Invalid folder ID");
      }
    }


    return bookmarkRepository.save(bookmark);
  }

  private void validateBookmark(Bookmark bookmark) {
    List<Map<String, String>> errors = new ArrayList<>();

    if (bookmark.getUrl() == null || bookmark.getUrl().isBlank()) {
      errors.add(Map.of(
              FIELD, "url",
              MESSAGE, "URL cannot be blank",
              ERROR_CODE, "INVALID_URL"
      ));
    } else {
      try {
        new URL(bookmark.getUrl());
      } catch (MalformedURLException e) {
        errors.add(Map.of(
                FIELD, "url",
                MESSAGE, "Invalid URL format",
                ERROR_CODE, "INVALID_URL_FORMAT"
        ));
      }
    }

    if (bookmark.getTitle() == null || bookmark.getTitle().isBlank()) {
      errors.add(Map.of(
              FIELD, "title",
              MESSAGE, "Title cannot be blank",
              ERROR_CODE, "INVALID_TITLE"
      ));
    } else if (bookmark.getTitle().length() > 50) {
      errors.add(Map.of(
              FIELD, "title",
              MESSAGE, "Title cannot exceed 50 characters",
              ERROR_CODE, "TITLE_TOO_LONG"
      ));
    }


    if (bookmark.getUserId() == null) {
      errors.add(Map.of(
              FIELD, "userId",
              MESSAGE, "User ID cannot be null",
              ERROR_CODE, "INVALID_USER_ID"
      ));
    }

    if (!errors.isEmpty()) {
      throw new BookmarkManagerException("Invalid Arguments", errors);
    }
  }



  public Bookmark getBookmarkById(UUID id) {
    return bookmarkRepository.findById(id)
            .orElseThrow(() -> new BookmarkNotFoundException("Bookmark not found with id: " + id));
  }

  @Transactional
  public Bookmark updateBookmark(UUID id, Bookmark bookmarkDetails) {
    validateBookmark(bookmarkDetails);

    Bookmark existingBookmark = bookmarkRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Bookmark not found with ID: " + id));


    existingBookmark.setTitle(bookmarkDetails.getTitle());
    existingBookmark.setUrl(bookmarkDetails.getUrl());
    if (existingBookmark.getFolder().getId() != null) {
      existingBookmark.setFolder(bookmarkDetails.getFolder());
    }
    
    return bookmarkRepository.save(existingBookmark);
  }


  public void deleteBookmark(UUID id) {
    if (bookmarkRepository.existsById(id)) {
      bookmarkRepository.deleteById(id);
    } else {
      throw new ResourceNotFoundException("Bookmark not found with id: " + id);
    }
  }
}
