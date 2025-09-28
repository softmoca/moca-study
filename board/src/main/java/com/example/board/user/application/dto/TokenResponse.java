package com.example.board.user.application.dto;

import com.example.board.user.domain.model.TokenPair;
import java.time.LocalDateTime;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        LocalDateTime accessTokenExpiresAt,
        LocalDateTime refreshTokenExpiresAt,
        String tokenType,
        UserResponse user
) {
    public static TokenResponse from(TokenPair tokenPair, UserResponse user) {
        return new TokenResponse(
                tokenPair.getAccessToken().getValue(),
                tokenPair.getRefreshToken().getValue(),
                tokenPair.getAccessToken().getExpiresAt(),
                tokenPair.getRefreshToken().getExpiresAt(),
                "Bearer",
                user
        );
    }
}