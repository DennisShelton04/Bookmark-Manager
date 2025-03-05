package com.bookmarkmanager.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Entity
@Table(name = "bookmarks")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Bookmark {
  @Id
  @GeneratedValue
  private UUID  id;
  private String title;
  private String url;
  private UUID userId;
  @ManyToOne
  @JoinColumn(name = "folder_id")
  private Folder folder;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  @PrePersist
  public void prePersist() {
    LocalDateTime now = LocalDateTime.now();
    this.createdAt = now;
    this.updatedAt = now;
  }

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

  public Bookmark() {
    this.id = UUID.randomUUID();
  }


}
