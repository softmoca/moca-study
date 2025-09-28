package com.example.board.board.application.dto;

import java.time.LocalDateTime;

public record PostListResponse(
        String postId,
        String title,
        String authorName,
        int viewCount,
        int commentCount,
        LocalDateTime createdAt
) {
}