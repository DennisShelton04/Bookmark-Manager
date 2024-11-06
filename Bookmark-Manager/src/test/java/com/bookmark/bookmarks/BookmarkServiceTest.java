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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
class BookmarkServiceTest {

  BookmarkRepository bookmarkRepository = mock(BookmarkRepository.class);
  FolderRepository folderRepository = mock(FolderRepository.class);
  BookmarkService bookmarkService = new BookmarkService(bookmarkRepository, folderRepository);



  @Test
  void test_create_bookmark_with_valid_data() {
    UUID folderId = UUID.randomUUID();
    Bookmark bookmark = new Bookmark();
    bookmark.setTitle("Sample Title");
    bookmark.setUrl("http://example.com");
    bookmark.setUserId(UUID.randomUUID());
    bookmark.setFolderId(folderId);

    when(folderRepository.findById(folderId)).thenReturn(Optional.of(new Folder()));
    when(bookmarkRepository.save(any(Bookmark.class))).thenReturn(bookmark);

    Bookmark savedBookmark = bookmarkService.createBookmark(bookmark);

    assertNotNull(savedBookmark);
    assertEquals("Sample Title", savedBookmark.getTitle());
    assertEquals("http://example.com", savedBookmark.getUrl());
  }

    @Test
    void test_create_bookmark_with_invalid_data() {
        Bookmark bookmark = new Bookmark();

        try {
            bookmarkService.createBookmark(bookmark);
        } catch (BookmarkManagerException e) {
            assertEquals("Invalid Arguments", e.getMessage());
        }
    }

    @Test
    void test_create_bookmark_with_invalid_folder_id() {
        Bookmark bookmark = new Bookmark();
        bookmark.setTitle("Sample Title");
        bookmark.setUrl("http://example.com");
        bookmark.setUserId(UUID.randomUUID());
        bookmark.setFolderId(UUID.randomUUID());

        when(folderRepository.findById(bookmark.getFolderId())).thenReturn(Optional.empty());

        try {
            bookmarkService.createBookmark(bookmark);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid folder ID", e.getMessage());
        }
    }

    @Test
    void test_get_bookmark_by_id() {
        UUID bookmarkId = UUID.randomUUID();
        Bookmark bookmark = new Bookmark();
        bookmark.setId(bookmarkId);
        bookmark.setTitle("Sample Title");
        bookmark.setUrl("http://example.com");
        bookmark.setUserId(UUID.randomUUID());

        when(bookmarkRepository.findById(bookmarkId)).thenReturn(Optional.of(bookmark));

        Bookmark savedBookmark = bookmarkService.getBookmarkById(bookmarkId);

        assertNotNull(savedBookmark);
        assertEquals(bookmarkId, savedBookmark.getId());
        assertEquals("Sample Title", savedBookmark.getTitle());
        assertEquals("http://example.com", savedBookmark.getUrl());
    }

    @Test
    void test_get_bookmark_by_id_with_invalid_id() {
        UUID bookmarkId = UUID.randomUUID();

        when(bookmarkRepository.findById(bookmarkId)).thenReturn(Optional.empty());

        try {
            bookmarkService.getBookmarkById(bookmarkId);
        } catch (BookmarkNotFoundException e) {
          assertNotNull(e.getMessage());
        }
    }

    @Test
    void test_update_bookmark() {
        UUID bookmarkId = UUID.randomUUID();
        Bookmark bookmark = new Bookmark();
        bookmark.setId(bookmarkId);
        bookmark.setTitle("Sample Title");
        bookmark.setUrl("http://example.com");
        bookmark.setUserId(UUID.randomUUID());

        Bookmark updatedBookmark = new Bookmark();
        updatedBookmark.setId(bookmarkId);
        updatedBookmark.setTitle("Updated Title");
        updatedBookmark.setUrl("http://example.com");
        updatedBookmark.setUserId(UUID.randomUUID());

        when(bookmarkRepository.findById(bookmarkId)).thenReturn(Optional.of(bookmark));
        when(bookmarkRepository.save(any(Bookmark.class))).thenReturn(updatedBookmark);

        Bookmark savedBookmark = bookmarkService.updateBookmark(bookmarkId, updatedBookmark);

        assertNotNull(savedBookmark);
        assertEquals(bookmarkId, savedBookmark.getId());
        assertEquals("Updated Title", savedBookmark.getTitle());
        assertEquals("http://example.com", savedBookmark.getUrl());
    }

    @Test
    void test_update_bookmark_with_invalid_id() {
        UUID bookmarkId = UUID.randomUUID();
        Bookmark updatedBookmark = new Bookmark();
        updatedBookmark.setId(bookmarkId);
        updatedBookmark.setTitle("Updated Title");
        updatedBookmark.setUrl("http://example.com");
        updatedBookmark.setUserId(UUID.randomUUID());

        when(bookmarkRepository.findById(bookmarkId)).thenReturn(Optional.empty());

        try {
            bookmarkService.updateBookmark(bookmarkId, updatedBookmark);
        } catch (IllegalArgumentException e) {
            assertEquals("Bookmark not found with ID: " + bookmarkId, e.getMessage());
        }
    }

    @Test
    void test_update_bookmark_with_invalid_data() {
        UUID bookmarkId = UUID.randomUUID();
        Bookmark bookmark = new Bookmark();
        bookmark.setId(bookmarkId);
        bookmark.setTitle("Sample Title");
        bookmark.setUrl("http://example.com");
        bookmark.setUserId(UUID.randomUUID());

        Bookmark updatedBookmark = new Bookmark();
        updatedBookmark.setId(bookmarkId);
        updatedBookmark.setTitle("");
        updatedBookmark.setUrl("http://example.com");
        updatedBookmark.setUserId(UUID.randomUUID());

        when(bookmarkRepository.findById(bookmarkId)).thenReturn(Optional.of(bookmark));

        try {
            bookmarkService.updateBookmark(bookmarkId, updatedBookmark);
        } catch (BookmarkManagerException e) {
            assertEquals("Invalid Arguments", e.getMessage());
        }
    }

    @Test
    void test_delete_bookmark() {
        UUID bookmarkId = UUID.randomUUID();
        Bookmark bookmark = new Bookmark();
        bookmark.setId(bookmarkId);
        bookmark.setTitle("Sample Title");
        bookmark.setUrl("http://example.com");
        bookmark.setUserId(UUID.randomUUID());

        when(bookmarkRepository.existsById(bookmarkId)).thenReturn(Boolean.TRUE);

        Mockito.doNothing().when(bookmarkRepository).deleteById(bookmarkId);

        bookmarkService.deleteBookmark(bookmarkId);




    }

    @Test
    void test_delete_bookmark_with_invalid_id() {
        UUID bookmarkId = UUID.randomUUID();

        when(bookmarkRepository.existsById(bookmarkId)).thenReturn(Boolean.FALSE);
        try {
            bookmarkService.deleteBookmark(bookmarkId);
        } catch (ResourceNotFoundException e) {
            assertEquals("Bookmark not found with id: " + bookmarkId, e.getMessage());
        }
    }




}
