package com.example.board.board.application.dto;

import com.example.board.board.domain.model.Post;
import com.example.board.board.domain.model.PostStatus;
import java.time.LocalDateTime;

public record PostResponse(
        String postId,
        String boardId,
        String title,
        String content,
        String authorId,
        String authorName,
        PostStatus status,
        int viewCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PostResponse from(Post post, String authorName) {
        return new PostResponse(
                post.getPostId().getValue(),
                post.getBoardId().getValue(),
                post.getTitle().getValue(),
                post.getContent().getValue(),
                post.getAuthorId().getValue(),
                authorName,
                post.getStatus(),
                post.getViewCount(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}