package com.example.board.user.application.dto;

import com.example.board.user.domain.model.AccessToken;
import java.time.LocalDateTime;

public record LoginResponse(
        String accessToken,
        LocalDateTime accessTokenExpiresAt,
        String tokenType,
        UserResponse user
) {
    public static LoginResponse from(AccessToken accessToken, UserResponse user) {
        return new LoginResponse(
                accessToken.getValue(),
                accessToken.getExpiresAt(),
                "Bearer",
                user
        );
    }
}