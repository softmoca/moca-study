package com.example.board.board.application.dto;

import com.example.board.board.domain.model.Post;
import com.example.board.board.domain.model.PostStatus;

import java.time.LocalDateTime;

public class PostResponse {
    private final String postId;
    private final String boardId;
    private final String title;
    private final String content;
    private final String authorId;
    private final String authorName; // 작성자명은 별도로 조회 필요
    private final PostStatus status;
    private final int viewCount;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public PostResponse(String postId, String boardId, String title, String content,
                        String authorId, String authorName, PostStatus status, int viewCount,
                        LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.postId = postId;
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.authorName = authorName;
        this.status = status;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

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

    // Getter 메서드들
    public String getPostId() { return postId; }
    public String getBoardId() { return boardId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getAuthorId() { return authorId; }
    public String getAuthorName() { return authorName; }
    public PostStatus getStatus() { return status; }
    public int getViewCount() { return viewCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}