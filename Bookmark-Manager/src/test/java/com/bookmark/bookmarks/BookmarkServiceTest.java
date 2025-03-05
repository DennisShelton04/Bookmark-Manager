package com.bookmark.bookmarks;

import com.bookmarkmanager.bookmark.BookmarkRepository;
import com.bookmarkmanager.bookmark.BookmarkService;
import com.bookmarkmanager.bookmarkfolder.FolderRepository;
import com.bookmarkmanager.exception.BookmarkManagerException;
import com.bookmarkmanager.exception.BookmarkNotFoundException;
import com.bookmarkmanager.exception.ResourceNotFoundException;
import com.bookmarkmanager.pojo.Bookmark;
import com.bookmarkmanager.pojo.Folder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;


class BookmarkServiceTest {

  BookmarkRepository bookmarkRepository = mock(BookmarkRepository.class);
  FolderRepository folderRepository = mock(FolderRepository.class);
  BookmarkService bookmarkService = new BookmarkService(bookmarkRepository, folderRepository);
  Bookmark bookmark = new Bookmark();

  private UUID generateUUID() {
    return UUID.randomUUID();
  }

  private Bookmark createSampleBookmark(UUID folderId) {
    bookmark.setTitle("Sample Title");
    bookmark.setUrl("http://example.com");
    bookmark.setUserId(generateUUID());
    bookmark.getFolder().setId(folderId);
    return bookmark;
  }

  private Bookmark createSampleBookmark(UUID id, String title, String url, UUID userId) {
    bookmark.setId(id);
    bookmark.setTitle(title);
    bookmark.setUrl(url);
    bookmark.setUserId(userId);
    return bookmark;
  }

  private Folder createSampleFolder() {
    return new Folder();
  }


  @Test
  void shouldCreateBookmarkWhenValidDataProvided() {
    // Given
    UUID folderId = generateUUID();
    Bookmark bookmark = createSampleBookmark(folderId);

    when(folderRepository.findById(folderId)).thenReturn(Optional.of(createSampleFolder()));
    when(bookmarkRepository.save(any(Bookmark.class))).thenReturn(bookmark);

    // When
    Bookmark savedBookmark = bookmarkService.createBookmark(bookmark);

    // Then
    assertNotNull(savedBookmark);
    assertEquals("Sample Title", savedBookmark.getTitle());
    assertEquals("http://example.com", savedBookmark.getUrl());
  }

  @Test
  void test_create_bookmark_with_invalid_folder_id() {
    // Given
    Bookmark bookmark = createSampleBookmark(generateUUID());
    when(folderRepository.findById(bookmark.getFolder().getId())).thenReturn(Optional.empty());

    // When & Then
    IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> bookmarkService.createBookmark(bookmark)
    );
    assertEquals("Invalid folder ID", exception.getMessage());
  }

  @Test
  void shouldGetBookmarkByIdWhenValidIdProvided() {
    // Given
    UUID bookmarkId = generateUUID();
    Bookmark bookmark = createSampleBookmark(generateUUID());
    bookmark.setId(bookmarkId);

    when(bookmarkRepository.findById(bookmarkId)).thenReturn(Optional.of(bookmark));

    // When
    Bookmark retrievedBookmark = bookmarkService.getBookmarkById(bookmarkId);

    // Then
    assertNotNull(retrievedBookmark);
    assertEquals(bookmarkId, retrievedBookmark.getId());
  }

  @Test
  void test_create_bookmark_with_invalid_url() {
    // Given
    UUID bookmarkId = generateUUID();
    Bookmark invalidBookmark = createSampleBookmark(
            bookmarkId,
            "Sample Title",
            "htp:/example.com", // Invalid URL
            generateUUID()
    );

    // When
    BookmarkManagerException exception = assertThrows(
            BookmarkManagerException.class, // Custom exception for validation errors
            () -> bookmarkService.createBookmark(invalidBookmark)
    );

    // Then
    assertEquals("Invalid Arguments", exception.getMessage()); // Expect the custom exception message
  }

  @Test
  void test_create_bookmark_with_title_too_long() {
    // Given
    UUID bookmarkId = UUID.randomUUID();
    String longTitle = "This is a very long title that exceeds the fifty character limit";
    Bookmark bookmark = createSampleBookmark(bookmarkId, longTitle, "http://valid-url.com", UUID.randomUUID());

    // When
    BookmarkManagerException exception = assertThrows(
            BookmarkManagerException.class,
            () -> bookmarkService.createBookmark(bookmark)
    );

    // Then
    assertEquals("Invalid Arguments", exception.getMessage());
    List<Map<String, String>> errors = exception.getErrors();
    assertEquals(1, errors.size());
    assertEquals("Title cannot exceed 50 characters", errors.get(0).get("message"));
    assertEquals("TITLE_TOO_LONG", errors.get(0).get("errorCode"));
  }



  @Test
  void test_get_bookmark_by_id_with_invalid_id() {
    // Given
    UUID bookmarkId = generateUUID();
    when(bookmarkRepository.findById(bookmarkId)).thenReturn(Optional.empty());

    // When & Then
    BookmarkNotFoundException exception = assertThrows(
            BookmarkNotFoundException.class,
            () -> bookmarkService.getBookmarkById(bookmarkId)
    );
    assertNotNull(exception.getMessage());
  }


  @Test
  void test_get_bookmark_by_id() {
    //Given
    UUID bookmarkId = generateUUID();
    Bookmark bookmark = createSampleBookmark(bookmarkId);
    when(bookmarkRepository.findById(bookmarkId)).thenReturn(Optional.of(bookmark));

    //When
    Bookmark savedBookmark = bookmarkService.getBookmarkById(bookmarkId);

    //Then
    assertNotNull(savedBookmark);
    assertEquals("Sample Title", savedBookmark.getTitle());
    assertEquals("http://example.com", savedBookmark.getUrl());
  }


  @Test
  void test_update_bookmark() {
    // Given
    UUID bookmarkId = generateUUID();
    Bookmark existingBookmark = createSampleBookmark(bookmarkId);
    Bookmark updatedBookmark = createSampleBookmark(bookmarkId, "Updated Title", "http://example.com", existingBookmark.getUserId());

    when(bookmarkRepository.findById(bookmarkId)).thenReturn(Optional.of(existingBookmark));
    when(bookmarkRepository.save(any(Bookmark.class))).thenReturn(updatedBookmark);

    // When
    Bookmark savedBookmark = bookmarkService.updateBookmark(bookmarkId, updatedBookmark);

    // Then
    assertNotNull(savedBookmark);
    assertEquals("Updated Title", savedBookmark.getTitle());
  }

  @Test
  void shouldThrowExceptionWhenUpdatingWithInvalidId() {
    // Given
    UUID bookmarkId = generateUUID();
    Bookmark updatedBookmark = createSampleBookmark(bookmarkId);

    when(bookmarkRepository.findById(bookmarkId)).thenReturn(Optional.empty());

    // When & Then
    IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> bookmarkService.updateBookmark(bookmarkId, updatedBookmark)
    );
    assertEquals("Bookmark not found with ID: " + bookmarkId, exception.getMessage());
  }

  @Test
  void test_update_bookmark_with_invalid_data() {
    // Given
    UUID bookmarkId = generateUUID();
    Bookmark existingBookmark = createSampleBookmark(bookmarkId, "Sample Title", "http://example.com", generateUUID());
    Bookmark invalidUpdatedBookmark = createSampleBookmark(bookmarkId, "", "http://example.com", generateUUID());

    when(bookmarkRepository.findById(bookmarkId)).thenReturn(Optional.of(existingBookmark));

    // When & Then
    BookmarkManagerException exception = assertThrows(
            BookmarkManagerException.class,
            () -> bookmarkService.updateBookmark(bookmarkId, invalidUpdatedBookmark)
    );
    assertEquals("Invalid Arguments", exception.getMessage());
  }

  @Test
  void test_delete_bookmark() {

    // Given
    UUID bookmarkId = generateUUID();

    when(bookmarkRepository.existsById(bookmarkId)).thenReturn(true);
    doNothing().when(bookmarkRepository).deleteById(bookmarkId);

    // When
    bookmarkService.deleteBookmark(bookmarkId);

    // Then
    verify(bookmarkRepository).deleteById(bookmarkId);
    verify(bookmarkRepository).existsById(bookmarkId);
  }


  @Test
  void test_delete_bookmark_with_invalid_id() {
    // Given
    UUID bookmarkId = generateUUID();
    when(bookmarkRepository.existsById(bookmarkId)).thenReturn(false);

    // When & Then
    ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> bookmarkService.deleteBookmark(bookmarkId)
    );
    assertEquals("Bookmark not found with id: " + bookmarkId, exception.getMessage());
  }


}
