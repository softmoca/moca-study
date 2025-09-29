package com.example.board.board.application.dto;

import java.time.LocalDateTime;

public record PostListResponse(
        Long postId,      // String → Long
        String title,
        String authorName,
        int viewCount,
        int commentCount,
        LocalDateTime createdAt
) {
}