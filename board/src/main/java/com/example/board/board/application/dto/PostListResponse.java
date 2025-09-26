package com.example.board.board.application.dto;

import java.time.LocalDateTime;

public class PostListResponse {
    private final String postId;
    private final String title;
    private final String authorName;
    private final int viewCount;
    private final int commentCount; // 댓글 수는 별도로 조회 필요
    private final LocalDateTime createdAt;

    public PostListResponse(String postId, String title, String authorName,
                            int viewCount, int commentCount, LocalDateTime createdAt) {
        this.postId = postId;
        this.title = title;
        this.authorName = authorName;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
    }

    // Getter 메서드들
    public String getPostId() { return postId; }
    public String getTitle() { return title; }
    public String getAuthorName() { return authorName; }
    public int getViewCount() { return viewCount; }
    public int getCommentCount() { return commentCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}