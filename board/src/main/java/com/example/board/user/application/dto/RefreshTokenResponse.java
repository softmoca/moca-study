package com.example.board.user.application.dto;

import com.example.board.user.domain.model.TokenPair;
import java.time.LocalDateTime;

public record RefreshTokenResponse(
        String accessToken,
        String refreshToken,
        LocalDateTime accessTokenExpiresAt,
        LocalDateTime refreshTokenExpiresAt,
        String tokenType
) {
    public static RefreshTokenResponse from(TokenPair tokenPair) {
        return new RefreshTokenResponse(
                tokenPair.getAccessToken().getValue(),
                tokenPair.getRefreshToken().getValue(),
                tokenPair.getAccessToken().getExpiresAt(),
                tokenPair.getRefreshToken().getExpiresAt(),
                "Bearer"
        );
    }
}