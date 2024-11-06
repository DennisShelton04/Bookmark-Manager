package com.bookmarkmanager.bookmarkfolder;

import com.bookmarkmanager.pojo.Folder;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderRepository extends JpaRepository<Folder, UUID> {
}
