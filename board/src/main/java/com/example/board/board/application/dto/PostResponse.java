package com.example.board.board.application.dto;

import com.example.board.board.domain.Post;
import com.example.board.board.domain.PostStatus;
import java.time.LocalDateTime;

public record PostResponse(
        Long postId,           // String → Long
        Long boardId,          // String → Long
        String title,
        String content,
        Long authorId,         // String → Long
        String authorName,
        PostStatus status,
        int viewCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PostResponse from(Post post, String authorName) {
        return new PostResponse(
                post.getId(),
                post.getBoardId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthorId(),
                authorName,
                post.getStatus(),
                post.getViewCount(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}