package com.example.board.user.application.dto;

public class LoginResponse {
    private final String accessToken;
    private final UserResponse user;

    public LoginResponse(String accessToken, UserResponse user) {
        this.accessToken = accessToken;
        this.user = user;
    }

    public String getAccessToken() { return accessToken; }
    public UserResponse getUser() { return user; }
}