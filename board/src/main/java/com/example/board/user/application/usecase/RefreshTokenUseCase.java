package com.example.board.user.application.usecase;

import com.example.board.user.application.dto.RefreshTokenRequest;
import com.example.board.user.application.dto.RefreshTokenResponse;

public interface RefreshTokenUseCase {
    RefreshTokenResponse refreshTokens(RefreshTokenRequest request);
}