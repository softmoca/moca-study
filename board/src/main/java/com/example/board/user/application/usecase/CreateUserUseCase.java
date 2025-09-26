package com.example.board.user.application.usecase;

import com.example.board.user.application.dto.UserCreateRequest;
import com.example.board.user.application.dto.UserResponse;

public interface CreateUserUseCase {
    UserResponse createUser(UserCreateRequest request);
}