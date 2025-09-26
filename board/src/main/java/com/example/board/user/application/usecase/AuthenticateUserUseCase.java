package com.example.board.user.application.usecase;

import com.example.board.user.application.dto.LoginRequest;
import com.example.board.user.application.dto.LoginResponse;

public interface AuthenticateUserUseCase {
    LoginResponse authenticate(LoginRequest request);
}
