package com.example.board.board.application.dto;

import com.example.board.board.domain.model.Board;
import java.time.LocalDateTime;

public record BoardResponse(
        String boardId,
        String name,
        String description,
        String createdBy,
        LocalDateTime createdAt,
        boolean active,
        long postCount
) {
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
}