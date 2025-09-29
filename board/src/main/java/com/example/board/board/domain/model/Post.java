// Post.java - 게시글 엔티티
package com.example.board.board.domain.model;

import com.example.board.user.domain.model.UserId;
import java.time.LocalDateTime;
import java.util.Objects;

public class Post {
    private final PostId postId;
    private final BoardId boardId;
    private Title title;
    private Content content;
    private final UserId authorId;
    private PostStatus status;
    private int viewCount;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 생성자 - 새로운 게시글 생성d
    public Post(BoardId boardId, Title title, Content content, UserId authorId) {
        this.postId = PostId.generate();
        this.boardId = Objects.requireNonNull(boardId);
        this.title = Objects.requireNonNull(title);
        this.content = Objects.requireNonNull(content);
        this.authorId = Objects.requireNonNull(authorId);
        this.status = PostStatus.PUBLISHED;
        this.viewCount = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 생성자 - 기존 게시글 복원
    public Post(PostId postId, BoardId boardId, Title title, Content content,
                UserId authorId, PostStatus status, int viewCount,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.postId = Objects.requireNonNull(postId);
        this.boardId = Objects.requireNonNull(boardId);
        this.title = Objects.requireNonNull(title);
        this.content = Objects.requireNonNull(content);
        this.authorId = Objects.requireNonNull(authorId);
        this.status = Objects.requireNonNull(status);
        this.viewCount = viewCount;
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
    }

    // 비즈니스 메서드
    public void updateTitle(Title newTitle) {
        if (!canEdit()) {
            throw new IllegalStateException("Cannot edit post in current status: " + status);
        }
        this.title = Objects.requireNonNull(newTitle);
        this.updatedAt = LocalDateTime.now();
    }

    public void updateContent(Content newContent) {
        if (!canEdit()) {
            throw new IllegalStateException("Cannot edit post in current status: " + status);
        }
        this.content = Objects.requireNonNull(newContent);
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

    public boolean canEdit() {
        return status == PostStatus.DRAFT || status == PostStatus.PUBLISHED;
    }

    public boolean canView() {
        return status == PostStatus.PUBLISHED;
    }

    public boolean isAuthor(UserId userId) {
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

    // Getter 메서드들
    public PostId getPostId() { return postId; }
    public BoardId getBoardId() { return boardId; }
    public Title getTitle() { return title; }
    public Content getContent() { return content; }
    public UserId getAuthorId() { return authorId; }
    public PostStatus getStatus() { return status; }
    public int getViewCount() { return viewCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Post post = (Post) obj;
        return Objects.equals(postId, post.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId);
    }
}