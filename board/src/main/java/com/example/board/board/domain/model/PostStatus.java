package com.example.board.board.domain.model;

public enum PostStatus {
    DRAFT("임시저장"),
    PUBLISHED("게시됨"),
    DELETED("삭제됨");

    private final String description;

    PostStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}