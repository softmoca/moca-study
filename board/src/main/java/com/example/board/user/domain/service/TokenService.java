package com.example.board.user.domain.service;

import com.example.board.user.domain.User;
import com.example.board.user.domain.AccessToken;

public interface TokenService {
    AccessToken generateAccessToken(User user);
    boolean validateAccessToken(String tokenValue);
    Long extractUserIdFromAccessToken(String tokenValue);
}