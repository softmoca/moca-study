package com.example.board.user.presentation.controller;

import com.example.board.user.application.dto.*;
import com.example.board.user.application.service.AuthenticationService;
import com.example.board.user.infrastructure.security.JwtUserPrincipal;
import com.example.board.user.domain.service.TokenDomainService;

import com.example.board.user.presentation.api.AuthApi;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthenticationService authenticationService;
    private final TokenDomainService tokenDomainService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authenticationService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal JwtUserPrincipal principal) {
        UserResponse response = new UserResponse(
                principal.getUserId(),
                principal.getEmail(),
                principal.getUsername(),
                null,
                null,
                principal.isActive()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me/full")
    public ResponseEntity<UserResponse> getCurrentUserFull(@AuthenticationPrincipal JwtUserPrincipal principal) {
        var user = tokenDomainService.getUserById(principal.getUserId());
        UserResponse response = UserResponse.from(user);
        return ResponseEntity.ok(response);
    }
}