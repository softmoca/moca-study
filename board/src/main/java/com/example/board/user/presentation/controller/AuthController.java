package com.example.board.user.presentation.controller;

import com.example.board.user.application.dto.*;
import com.example.board.user.application.service.AuthenticationService;
import com.example.board.user.infrastructure.security.CustomUserDetails;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authenticationService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        RefreshTokenResponse response = authenticationService.refreshTokens(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody(required = false) RefreshTokenRequest request) {
        String refreshToken = request != null ? request.getRefreshToken() : null;
        authenticationService.logout(refreshToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout/all")
    public ResponseEntity<Void> logoutAllDevices(@AuthenticationPrincipal CustomUserDetails userDetails) {
        authenticationService.logoutAllDevices(userDetails.getUserId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserResponse response = UserResponse.from(userDetails.getUser());
        return ResponseEntity.ok(response);
    }
}