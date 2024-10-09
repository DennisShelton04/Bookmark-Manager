package com.bookmarkmanager.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
  private Long id;
  private String username;
  private String email;
  private List<Folder> folders;
}
