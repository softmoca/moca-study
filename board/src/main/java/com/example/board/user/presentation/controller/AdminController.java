package com.example.board.admin.presentation.controller;

import com.example.board.user.application.dto.UserResponse;
import com.example.board.user.application.service.UserApplicationService;
import com.example.board.user.domain.service.TokenDomainService;
import com.example.board.common.annotation.CurrentUser;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserApplicationService userApplicationService;
    private final TokenDomainService tokenDomainService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String userId) {
        UserResponse response = userApplicationService.findById(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/users/{userId}/revoke-tokens")
    public ResponseEntity<Void> revokeUserTokens(@PathVariable String userId) {
        tokenDomainService.revokeAllUserTokens(com.example.board.user.domain.model.UserId.of(userId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/tokens/cleanup")
    public ResponseEntity<Void> cleanupExpiredTokens() {
        tokenDomainService.cleanupExpiredTokens();
        return ResponseEntity.ok().build();
    }
}