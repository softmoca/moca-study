package com.example.board.comment.infrastructure.entity;

public enum CommentEntityStatus {
    ACTIVE("활성"),
    DELETED("삭제됨");

    private final String description;

    CommentEntityStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}