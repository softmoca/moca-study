package com.example.board.board.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    private static final int MIN_TITLE_LENGTH = 1;
    private static final int MAX_TITLE_LENGTH = 200;
    private static final int MIN_CONTENT_LENGTH = 1;
    private static final int MAX_CONTENT_LENGTH = 10000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "board_id", nullable = false)
    private Long boardId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 10000)
    @Lob
    private String content;

    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PostStatus status;

    @Column(nullable = false)
    private Integer viewCount;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 생성자 - 새로운 게시글 생성
    public Post(Long boardId, String title, String content, Long authorId) {
        this.boardId = boardId;
        this.title = validateTitle(title);
        this.content = validateContent(content);
        this.authorId = authorId;
        this.status = PostStatus.PUBLISHED;
        this.viewCount = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 검증 로직
    private String validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }

        String trimmedTitle = title.trim();
        if (trimmedTitle.length() < MIN_TITLE_LENGTH || trimmedTitle.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("Title must be between %d and %d characters",
                            MIN_TITLE_LENGTH, MAX_TITLE_LENGTH));
        }

        return trimmedTitle;
    }

    private String validateContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }

        String trimmedContent = content.trim();
        if (trimmedContent.length() < MIN_CONTENT_LENGTH || trimmedContent.length() > MAX_CONTENT_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("Content must be between %d and %d characters",
                            MIN_CONTENT_LENGTH, MAX_CONTENT_LENGTH));
        }

        return trimmedContent;
    }

    // 비즈니스 메서드
    public void updateTitle(String newTitle) {
        if (!canEdit()) {
            throw new IllegalStateException("Cannot edit post in current status: " + status);
        }
        this.title = validateTitle(newTitle);
        this.updatedAt = LocalDateTime.now();
    }

    public void updateContent(String newContent) {
        if (!canEdit()) {
            throw new IllegalStateException("Cannot edit post in current status: " + status);
        }
        this.content = validateContent(newContent);
        this.updatedAt = LocalDateTime.now();
    }

    public void publish() {
        if (this.status == PostStatus.DELETED) {
            throw new IllegalStateException("Cannot publish deleted post");
        }
        this.status = PostStatus.PUBLISHED;
        this.updatedAt = LocalDateTime.now();
    }

    public void delete() {
        this.status = PostStatus.DELETED;
        this.updatedAt = LocalDateTime.now();
    }

    public void saveDraft() {
        if (this.status == PostStatus.DELETED) {
            throw new IllegalStateException("Cannot save deleted post as draft");
        }
        this.status = PostStatus.DRAFT;
        this.updatedAt = LocalDateTime.now();
    }

    public void increaseViewCount() {
        this.viewCount++;
        // 조회수는 updatedAt을 변경하지 않음
    }

    // 상태 체크 메서드
    public boolean canEdit() {
        return status == PostStatus.DRAFT || status == PostStatus.PUBLISHED;
    }

    public boolean canView() {
        return status == PostStatus.PUBLISHED;
    }

    public boolean isAuthor(Long userId) {
        return this.authorId.equals(userId);
    }

    public boolean isPublished() {
        return PostStatus.PUBLISHED.equals(this.status);
    }

    public boolean isDraft() {
        return PostStatus.DRAFT.equals(this.status);
    }

    public boolean isDeleted() {
        return PostStatus.DELETED.equals(this.status);
    }
}