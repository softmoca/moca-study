package com.example.board.comment.application.dto;

import com.example.board.comment.domain.Comment;
import com.example.board.comment.domain.CommentStatus;
import java.time.LocalDateTime;
import java.util.List;

public record CommentResponse(
        Long commentId,           // String → Long
        Long postId,              // String → Long
        String content,
        Long authorId,            // String → Long
        String authorName,
        CommentStatus status,
        Long parentCommentId,     // String → Long
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<CommentResponse> replies
) {
    public static CommentResponse from(Comment comment, String authorName, List<CommentResponse> replies) {
        return new CommentResponse(
                comment.getId(),
                comment.getPostId(),
                comment.getContent(),
                comment.getAuthorId(),
                authorName,
                comment.getStatus(),
                comment.getParentCommentId(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                replies
        );
    }
}