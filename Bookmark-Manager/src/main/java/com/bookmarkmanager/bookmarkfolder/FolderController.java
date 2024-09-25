package com.bookmarkmanager.bookmarkfolder;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;

@RequiredArgsConstructor()
public class FolderController {
  private final FolderService folderService;

}
