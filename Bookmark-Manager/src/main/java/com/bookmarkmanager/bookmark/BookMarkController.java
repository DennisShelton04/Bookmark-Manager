package com.bookmarkmanager.bookmark;


import com.bookmarkmanager.dto.BookmarkDTO;
import com.bookmarkmanager.pojo.Bookmark;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookmarks")
@RequiredArgsConstructor
public class BookMarkController {
  private final BookmarkService bookmarkService;



  @PostMapping
  public ResponseEntity<UUID> createBookmark(@RequestBody BookmarkDTO bookmark) {
    Bookmark createdBookmark = bookmarkService.createBookmark(bookmark);
    return ResponseEntity.created(URI.create("/bookmarks/" + createdBookmark.getId()))
            .body(createdBookmark.getId());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Bookmark> getBookmarkById(@PathVariable UUID id) {
    Bookmark bookmark = bookmarkService.getBookmarkById(id);
    return ResponseEntity.ok(bookmark);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Bookmark> updateBookmark(@PathVariable UUID id, @RequestBody BookmarkDTO bookmarkDetails) {
    Bookmark updatedBookmark = bookmarkService.updateBookmark(id, bookmarkDetails);
    return ResponseEntity.ok(updatedBookmark);
  }


  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBookmark(@PathVariable UUID id) {
    bookmarkService.deleteBookmark(id);
    return ResponseEntity.noContent().build();
  }

}
