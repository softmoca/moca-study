package com.example.board.user.application.dto;

public record LoginResponse(
        String accessToken,
        UserResponse user
) {
}