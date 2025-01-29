package com.bookmarkmanager.bookmark;

import com.bookmarkmanager.bookmarkfolder.FolderRepository;
import com.bookmarkmanager.dto.BookmarkDTO;
import com.bookmarkmanager.exception.BookmarkNotFoundException;
import com.bookmarkmanager.exception.ResourceNotFoundException;
import com.bookmarkmanager.pojo.Bookmark;
import com.bookmarkmanager.pojo.Folder;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
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


  public Bookmark createBookmark(BookmarkDTO bookmarkDTO) {
    Bookmark bookmark = new Bookmark();
    if (bookmark.getUrl() == null || bookmark.getUrl().isBlank()) {
      throw new IllegalArgumentException("URL cannot be blank");
    }

    if (bookmark.getTitle() == null || bookmark.getTitle().isBlank()) {
      throw new IllegalArgumentException("Title cannot be blank");
    }
    bookmark.setTitle(bookmarkDTO.title());
    bookmark.setUrl(bookmarkDTO.url());
    bookmark.setUserId(bookmarkDTO.userId());

    Optional<Folder> folderOptional = folderRepository.findById(bookmarkDTO.folderId());
    if (folderOptional.isPresent()) {
      bookmark.setFolder(folderOptional.get());
    } else {
      throw new IllegalArgumentException("Invalid folder ID");
    }

    return bookmarkRepository.save(bookmark);
  }


  public Bookmark getBookmarkById(UUID id) {
    return bookmarkRepository.findById(id)
            .orElseThrow(() -> new BookmarkNotFoundException("Bookmark not found with id: " + id));
  }

  public Bookmark updateBookmark(UUID id, BookmarkDTO bookmarkDTO) {

    // Find the existing bookmark by ID
    Bookmark existingBookmark = bookmarkRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Bookmark not found with ID: " + id));

    // Update title and URL from DTO
    existingBookmark.setTitle(bookmarkDTO.title());
    existingBookmark.setUrl(bookmarkDTO.url());

    // Check if folderId is provided in DTO and update folder association
    if (bookmarkDTO.folderId() != null) {
      Optional<Folder> folderOptional = folderRepository.findById(bookmarkDTO.folderId());
      if (folderOptional.isPresent()) {
        existingBookmark.setFolder(folderOptional.get());
      } else {
        throw new IllegalArgumentException("Invalid folder ID");
      }
    }

    // Save and return the updated bookmark
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
