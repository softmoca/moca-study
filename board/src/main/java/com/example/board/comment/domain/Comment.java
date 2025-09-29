package com.example.board.comment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    private static final int MIN_CONTENT_LENGTH = 1;
    private static final int MAX_CONTENT_LENGTH = 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @Column(nullable = false, length = 1000)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CommentStatus status;

    @Column(name = "parent_comment_id")
    private Long parentCommentId;  // 대댓글을 위한 부모 댓글 ID

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 생성자 - 최상위 댓글 생성
    public Comment(Long postId, Long authorId, String content) {
        this(postId, authorId, content, null);
    }

    // 생성자 - 대댓글 생성
    public Comment(Long postId, Long authorId, String content, Long parentCommentId) {
        this.postId = postId;
        this.authorId = authorId;
        this.content = validateContent(content);
        this.parentCommentId = parentCommentId;
        this.status = CommentStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 검증 로직
    private String validateContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Comment content cannot be null or empty");
        }

        String trimmedContent = content.trim();
        if (trimmedContent.length() < MIN_CONTENT_LENGTH || trimmedContent.length() > MAX_CONTENT_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("Comment content must be between %d and %d characters",
                            MIN_CONTENT_LENGTH, MAX_CONTENT_LENGTH));
        }

        return trimmedContent;
    }

    // 비즈니스 메서드
    public void updateContent(String newContent) {
        if (!canEdit()) {
            throw new IllegalStateException("Cannot edit comment in current status: " + status);
        }
        this.content = validateContent(newContent);
        this.updatedAt = LocalDateTime.now();
    }

    public void delete() {
        this.status = CommentStatus.DELETED;
        this.updatedAt = LocalDateTime.now();
    }

    // 상태 체크 메서드
    public boolean canEdit() {
        return CommentStatus.ACTIVE.equals(this.status);
    }

    public boolean isAuthor(Long userId) {
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
}