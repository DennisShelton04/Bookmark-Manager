package com.bookmarkmanager.bookmark;


import com.bookmarkmanager.dto.BookmarkDTO;
import com.bookmarkmanager.annotation.RateLimited;
import com.bookmarkmanager.pojo.Bookmark;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.UUID;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
  @RateLimited(maxTokens = 5, refillRate = 1, refillInterval = 1000)
  public ResponseEntity<Bookmark> getBookmarkById(@PathVariable UUID id) {
    System.out.println("calling the method");
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
