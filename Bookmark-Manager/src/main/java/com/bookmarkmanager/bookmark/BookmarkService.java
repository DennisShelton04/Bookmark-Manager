package com.bookmarkmanager.bookmark;

import com.bookmarkmanager.bookmarkfolder.FolderRepository;
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


    return bookmarkRepository.save(bookmark);
  }

  public Bookmark getBookmarkById(UUID id) {
    return bookmarkRepository.findById(id)
            .orElseThrow(() -> new BookmarkNotFoundException("Bookmark not found with id: " + id));
  }

  public Bookmark updateBookmark(UUID id, Bookmark bookmarkDetails) {

    Bookmark existingBookmark = bookmarkRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Bookmark not found with ID: " + id));


    existingBookmark.setTitle(bookmarkDetails.getTitle());
    existingBookmark.setUrl(bookmarkDetails.getUrl());
    if(existingBookmark.getFolderId()!=null) {
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
