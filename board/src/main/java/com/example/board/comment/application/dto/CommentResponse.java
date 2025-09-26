package com.example.board.comment.application.dto;

import com.example.board.comment.domain.model.Comment;
import com.example.board.comment.domain.model.CommentStatus;

import java.time.LocalDateTime;
import java.util.List;

public class CommentResponse {
    private final String commentId;
    private final String postId;
    private final String content;
    private final String authorId;
    private final String authorName;
    private final CommentStatus status;
    private final String parentCommentId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final List<CommentResponse> replies; // 대댓글 목록

    public CommentResponse(String commentId, String postId, String content,
                           String authorId, String authorName, CommentStatus status,
                           String parentCommentId, LocalDateTime createdAt, LocalDateTime updatedAt,
                           List<CommentResponse> replies) {
        this.commentId = commentId;
        this.postId = postId;
        this.content = content;
        this.authorId = authorId;
        this.authorName = authorName;
        this.status = status;
        this.parentCommentId = parentCommentId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.replies = replies;
    }

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

    // Getter 메서드들
    public String getCommentId() { return commentId; }
    public String getPostId() { return postId; }
    public String getContent() { return content; }
    public String getAuthorId() { return authorId; }
    public String getAuthorName() { return authorName; }
    public CommentStatus getStatus() { return status; }
    public String getParentCommentId() { return parentCommentId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public List<CommentResponse> getReplies() { return replies; }
}