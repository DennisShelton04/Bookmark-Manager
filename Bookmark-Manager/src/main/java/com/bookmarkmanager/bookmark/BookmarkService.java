package com.bookmarkmanager.bookmark;

import com.bookmarkmanager.bookmarkfolder.FolderRepository;
import com.bookmarkmanager.exception.BookmarkNotFoundException;
import com.bookmarkmanager.pojo.Bookmark;
import com.bookmarkmanager.pojo.Folder;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {
  private final BookmarkRepository bookmarkRepository;
  private final FolderRepository folderRepository;

  // Method to add a new bookmark
  @Transactional
  public Bookmark addBookmark(Bookmark bookmark) {
    // Validate the URL and title (basic example)
    if (bookmark.getUrl() == null || bookmark.getUrl().isBlank()) {
      throw new IllegalArgumentException("URL cannot be blank");
    }
    if (bookmark.getTitle() == null || bookmark.getTitle().isBlank()) {
      throw new IllegalArgumentException("Title cannot be blank");
    }

    // If folderId is provided, associate the bookmark with the folder
    if (bookmark.getFolderId() != null) {
      Optional<Folder> folderOptional = folderRepository.findById(bookmark.getFolderId());
      if (folderOptional.isPresent()) {
        bookmark.setFolderId(folderOptional.get().getId());
      } else {
        throw new IllegalArgumentException("Invalid folder ID");
      }
    }

    // Save the bookmark in the database
    return bookmarkRepository.save(bookmark);
  }

  public Bookmark getBookmarkById(UUID id) {
    return bookmarkRepository.findById(id)
            .orElseThrow(() -> new BookmarkNotFoundException("Bookmark not found with id: " + id));
  }
}
