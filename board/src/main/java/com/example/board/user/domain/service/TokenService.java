package com.example.board.user.domain.service;

import com.example.board.user.domain.model.*;

public interface TokenService {
    TokenPair generateTokenPair(User user);
    AccessToken generateAccessToken(User user);
    RefreshToken generateRefreshToken(User user);
    boolean validateAccessToken(String tokenValue);
    boolean validateRefreshToken(String tokenValue);
    UserId extractUserIdFromAccessToken(String tokenValue);
    UserId extractUserIdFromRefreshToken(String tokenValue);
    TokenPair refreshTokens(String refreshTokenValue);
}