package com.example.board.board.infrastructure.entity;

public enum PostEntityStatus {
    DRAFT("임시저장"),
    PUBLISHED("게시됨"),
    DELETED("삭제됨");

    private final String description;

    PostEntityStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}