package com.example.board.board.application.dto;

import com.example.board.board.domain.model.Board;

import java.time.LocalDateTime;

public class BoardResponse {
    private final String boardId;
    private final String name;
    private final String description;
    private final String createdBy;
    private final LocalDateTime createdAt;
    private final boolean active;
    private final long postCount; // 게시글 수는 별도로 조회 필요

    public BoardResponse(String boardId, String name, String description,
                         String createdBy, LocalDateTime createdAt, boolean active, long postCount) {
        this.boardId = boardId;
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.active = active;
        this.postCount = postCount;
    }

    public static BoardResponse from(Board board, long postCount) {
        return new BoardResponse(
                board.getBoardId().getValue(),
                board.getName(),
                board.getDescription(),
                board.getCreatedBy().getValue(),
                board.getCreatedAt(),
                board.isActive(),
                postCount
        );
    }

    // Getter 메서드들
    public String getBoardId() { return boardId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getCreatedBy() { return createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isActive() { return active; }
    public long getPostCount() { return postCount; }
}