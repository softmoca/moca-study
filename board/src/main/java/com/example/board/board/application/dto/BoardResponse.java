package com.example.board.board.application.dto;

import com.example.board.board.domain.Board;
import java.time.LocalDateTime;

public record BoardResponse(
        Long boardId,           // String → Long 변경
        String name,
        String description,
        Long createdBy,         // String → Long 변경
        LocalDateTime createdAt,
        boolean active,
        long postCount
) {
    public static BoardResponse from(Board board, long postCount) {
        return new BoardResponse(
                board.getId(),
                board.getName(),
                board.getDescription(),
                board.getCreatedBy(),
                board.getCreatedAt(),
                board.isActive(),
                postCount
        );
    }
}