package com.bookmark.bookmarks;

import com.bookmarkmanager.bookmark.BookmarkRepository;
import com.bookmarkmanager.bookmark.BookmarkService;
import com.bookmarkmanager.bookmarkfolder.FolderRepository;
import com.bookmarkmanager.pojo.Bookmark;
import com.bookmarkmanager.pojo.Folder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class BookmarkServiceTest {

  @Test
  void test_create_bookmark_with_valid_data() {
    BookmarkRepository bookmarkRepository = mock(BookmarkRepository.class);
    FolderRepository folderRepository = mock(FolderRepository.class);
    BookmarkService bookmarkService = new BookmarkService(bookmarkRepository, folderRepository);

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
}
