package com.example.board.board.infrastructure.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
public class PostEntity {

    @Id
    private String postId;

    @Column(nullable = false)
    private String boardId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 10000)
    @Lob
    private String content;

    @Column(nullable = false)
    private String authorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status;

    @Column(nullable = false)
    private Integer viewCount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 기본 생성자
    protected PostEntity() {}

    // 생성자
    public PostEntity(String postId, String boardId, String title, String content,
                      String authorId, PostStatus status, Integer viewCount,
                      LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.postId = postId;
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.status = status;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 도메인 모델로 변환
    public com.example.board.board.domain.model.Post toDomain() {
        return new com.example.board.board.domain.model.Post(
                com.example.board.board.domain.model.PostId.of(this.postId),
                com.example.board.board.domain.model.BoardId.of(this.boardId),
                com.example.board.board.domain.model.Title.of(this.title),
                com.example.board.board.domain.model.Content.of(this.content),
                com.example.board.user.domain.model.UserId.of(this.authorId),
                com.example.board.board.domain.model.PostStatus.valueOf(this.status.name()),
                this.viewCount,
                this.createdAt,
                this.updatedAt
        );
    }

    // 도메인 모델에서 변환
    public static PostEntity fromDomain(com.example.board.board.domain.model.Post post) {
        return new PostEntity(
                post.getPostId().getValue(),
                post.getBoardId().getValue(),
                post.getTitle().getValue(),
                post.getContent().getValue(),
                post.getAuthorId().getValue(),
                PostStatus.valueOf(post.getStatus().name()),
                post.getViewCount(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }

    // JPA Entity용 PostStatus enum
    public enum PostStatus {
        DRAFT, PUBLISHED, DELETED
    }

    // Getter/Setter 메서드들
    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }

    public String getBoardId() { return boardId; }
    public void setBoardId(String boardId) { this.boardId = boardId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }

    public PostStatus getStatus() { return status; }
    public void setStatus(PostStatus status) { this.status = status; }

    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}