package com.example.board.board.infrastructure.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "boards")
public class BoardEntity {

    @Id
    private String boardId;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Boolean active;

    // 기본 생성자
    protected BoardEntity() {}

    // 생성자
    public BoardEntity(String boardId, String name, String description, String createdBy,
                       LocalDateTime createdAt, LocalDateTime updatedAt, Boolean active) {
        this.boardId = boardId;
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.active = active;
    }

    // 도메인 모델로 변환
    public com.example.board.board.domain.model.Board toDomain() {
        return new com.example.board.board.domain.model.Board(
                com.example.board.board.domain.model.BoardId.of(this.boardId),
                this.name,
                this.description,
                com.example.board.user.domain.model.UserId.of(this.createdBy),
                this.createdAt,
                this.updatedAt,
                this.active
        );
    }

    // 도메인 모델에서 변환
    public static BoardEntity fromDomain(com.example.board.board.domain.model.Board board) {
        return new BoardEntity(
                board.getBoardId().getValue(),
                board.getName(),
                board.getDescription(),
                board.getCreatedBy().getValue(),
                board.getCreatedAt(),
                board.getUpdatedAt(),
                board.isActive()
        );
    }

    // Getter/Setter 메서드들
    public String getBoardId() { return boardId; }
    public void setBoardId(String boardId) { this.boardId = boardId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}