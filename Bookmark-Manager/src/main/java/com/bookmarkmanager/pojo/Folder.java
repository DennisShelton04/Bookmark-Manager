package com.bookmarkmanager.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data                       // Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor           // Generates no-args constructor
@AllArgsConstructor
@Entity
@Table(name = "folder")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Folder {
  @Id
  @GeneratedValue()
  private UUID id;
  private String name;
  private Long userId;
  @OneToMany(mappedBy = "folder")
  private List<Bookmark> bookmarks;  // List of bookmarks in the folder
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
