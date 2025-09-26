package com.example.board.comment.infrastructure.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class CommentEntity {

    @Id
    private String commentId;

    @Column(nullable = false)
    private String postId;

    @Column(nullable = false)
    private String authorId;

    @Column(nullable = false, length = 1000)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommentStatus status;

    private String parentCommentId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 기본 생성자
    protected CommentEntity() {}

    // 생성자
    public CommentEntity(String commentId, String postId, String authorId, String content,
                         CommentStatus status, String parentCommentId,
                         LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.commentId = commentId;
        this.postId = postId;
        this.authorId = authorId;
        this.content = content;
        this.status = status;
        this.parentCommentId = parentCommentId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 도메인 모델로 변환
    public com.example.board.comment.domain.model.Comment toDomain() {
        return new com.example.board.comment.domain.model.Comment(
                com.example.board.comment.domain.model.CommentId.of(this.commentId),
                com.example.board.board.domain.model.PostId.of(this.postId),
                com.example.board.user.domain.model.UserId.of(this.authorId),
                com.example.board.comment.domain.model.CommentContent.of(this.content),
                com.example.board.comment.domain.model.CommentStatus.valueOf(this.status.name()),
                this.parentCommentId != null ?
                        com.example.board.comment.domain.model.CommentId.of(this.parentCommentId) : null,
                this.createdAt,
                this.updatedAt
        );
    }

    // 도메인 모델에서 변환
    public static CommentEntity fromDomain(com.example.board.comment.domain.model.Comment comment) {
        return new CommentEntity(
                comment.getCommentId().getValue(),
                comment.getPostId().getValue(),
                comment.getAuthorId().getValue(),
                comment.getContent().getValue(),
                CommentStatus.valueOf(comment.getStatus().name()),
                comment.getParentCommentId() != null ? comment.getParentCommentId().getValue() : null,
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }

    // JPA Entity용 CommentStatus enum
    public enum CommentStatus {
        ACTIVE, DELETED
    }

    // Getter/Setter 메서드들
    public String getCommentId() { return commentId; }
    public void setCommentId(String commentId) { this.commentId = commentId; }

    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }

    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public CommentStatus getStatus() { return status; }
    public void setStatus(CommentStatus status) { this.status = status; }

    public String getParentCommentId() { return parentCommentId; }
    public void setParentCommentId(String parentCommentId) { this.parentCommentId = parentCommentId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}