package com.bookmarkmanager.bookmark;

import com.bookmarkmanager.pojo.Bookmark;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, UUID> {
}
