package com.example.board.board.domain.model;

import com.example.board.user.domain.model.UserId;
import java.time.LocalDateTime;
import java.util.Objects;

public class Board {
    private final BoardId boardId;
    private final String name;
    private final String description;
    private final UserId createdBy;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean active;

    // 생성자 - 새로운 게시판 생성
    public Board(String name, String description, UserId createdBy) {
        this.boardId = BoardId.generate();
        this.name = validateName(name);
        this.description = description;
        this.createdBy = Objects.requireNonNull(createdBy);
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.active = true;
    }

    // 생성자 - 기존 게시판 복원
    public Board(BoardId boardId, String name, String description, UserId createdBy,
                 LocalDateTime createdAt, LocalDateTime updatedAt, boolean active) {
        this.boardId = Objects.requireNonNull(boardId);
        this.name = validateName(name);
        this.description = description;
        this.createdBy = Objects.requireNonNull(createdBy);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
        this.active = active;
    }

    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Board name cannot be null or empty");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Board name must be less than 100 characters");
        }
        return name.trim();
    }

    // 비즈니스 메서드
    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean canCreatePost() {
        return active;
    }

    // Getter 메서드들
    public BoardId getBoardId() { return boardId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public UserId getCreatedBy() { return createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public boolean isActive() { return active; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Board board = (Board) obj;
        return Objects.equals(boardId, board.boardId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardId);
    }
}