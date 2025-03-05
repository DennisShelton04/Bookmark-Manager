package com.bookmarkmanager.dto;

import java.util.UUID;

public record BookmarkDTO(
        UUID id,
        String title,
        String url,
        UUID userId,
        UUID folderId
) {}
