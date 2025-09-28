package com.example.board.user.application.usecase;

public interface LogoutUseCase {
    void logout(String refreshToken);
    void logoutAllDevices(String userId);
}