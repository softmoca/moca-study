package com.example.board.comment.domain.model;

import com.example.board.board.domain.model.PostId;
import com.example.board.user.domain.model.UserId;

import java.time.LocalDateTime;
import java.util.Objects;

public class Comment {
    private final CommentId commentId;
    private final PostId postId;
    private final UserId authorId;
    private CommentContent content;
    private CommentStatus status;
    private final CommentId parentCommentId; // 대댓글을 위한 부모 댓글 ID (null이면 최상위 댓글)
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 생성자 - 새로운 댓글 생성 (최상위 댓글)
    public Comment(PostId postId, UserId authorId, CommentContent content) {
        this(postId, authorId, content, null);
    }

    // 생성자 - 새로운 대댓글 생성
    public Comment(PostId postId, UserId authorId, CommentContent content, CommentId parentCommentId) {
        this.commentId = CommentId.generate();
        this.postId = Objects.requireNonNull(postId);
        this.authorId = Objects.requireNonNull(authorId);
        this.content = Objects.requireNonNull(content);
        this.parentCommentId = parentCommentId;
        this.status = CommentStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 생성자 - 기존 댓글 복원
    public Comment(CommentId commentId, PostId postId, UserId authorId, CommentContent content,
                   CommentStatus status, CommentId parentCommentId,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.commentId = Objects.requireNonNull(commentId);
        this.postId = Objects.requireNonNull(postId);
        this.authorId = Objects.requireNonNull(authorId);
        this.content = Objects.requireNonNull(content);
        this.status = Objects.requireNonNull(status);
        this.parentCommentId = parentCommentId;
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
    }

    // 비즈니스 메서드
    public void updateContent(CommentContent newContent) {
        if (!canEdit()) {
            throw new IllegalStateException("Cannot edit comment in current status: " + status);
        }
        this.content = Objects.requireNonNull(newContent);
        this.updatedAt = LocalDateTime.now();
    }

    public void delete() {
        this.status = CommentStatus.DELETED;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean canEdit() {
        return CommentStatus.ACTIVE.equals(this.status);
    }

    public boolean isAuthor(UserId userId) {
        return this.authorId.equals(userId);
    }

    public boolean isActive() {
        return CommentStatus.ACTIVE.equals(this.status);
    }

    public boolean isDeleted() {
        return CommentStatus.DELETED.equals(this.status);
    }

    public boolean isTopLevel() {
        return parentCommentId == null;
    }

    public boolean isReply() {
        return parentCommentId != null;
    }

    // Getter 메서드들
    public CommentId getCommentId() { return commentId; }
    public PostId getPostId() { return postId; }
    public UserId getAuthorId() { return authorId; }
    public CommentContent getContent() { return content; }
    public CommentStatus getStatus() { return status; }
    public CommentId getParentCommentId() { return parentCommentId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Comment comment = (Comment) obj;
        return Objects.equals(commentId, comment.commentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId);
    }
}