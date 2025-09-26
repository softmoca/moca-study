package com.example.board.comment.domain.model;

public enum CommentStatus {
    ACTIVE("활성"),
    DELETED("삭제됨");

    private final String description;

    CommentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}