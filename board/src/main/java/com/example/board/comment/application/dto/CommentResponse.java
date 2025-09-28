package com.example.board.comment.application.dto;

import com.example.board.comment.domain.model.Comment;
import com.example.board.comment.domain.model.CommentStatus;
import java.time.LocalDateTime;
import java.util.List;

public record CommentResponse(
        String commentId,
        String postId,
        String content,
        String authorId,
        String authorName,
        CommentStatus status,
        String parentCommentId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<CommentResponse> replies
) {
    public static CommentResponse from(Comment comment, String authorName, List<CommentResponse> replies) {
        return new CommentResponse(
                comment.getCommentId().getValue(),
                comment.getPostId().getValue(),
                comment.getContent().getValue(),
                comment.getAuthorId().getValue(),
                authorName,
                comment.getStatus(),
                comment.getParentCommentId() != null ? comment.getParentCommentId().getValue() : null,
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                replies
        );
    }
}