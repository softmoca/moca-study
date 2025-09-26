package com.example.board.user.application.dto;


import lombok.*;

@Getter
@RequiredArgsConstructor
public class LoginResponse {
    private final String accessToken;
    private final UserResponse user;
}