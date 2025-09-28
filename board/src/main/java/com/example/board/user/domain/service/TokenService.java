package com.example.board.user.domain.service;

import com.example.board.user.domain.model.*;

public interface TokenService {
    AccessToken generateAccessToken(User user);
    boolean validateAccessToken(String tokenValue);
    UserId extractUserIdFromAccessToken(String tokenValue);
}