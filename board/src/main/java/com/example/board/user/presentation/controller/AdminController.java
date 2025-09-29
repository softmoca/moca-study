package com.example.board.user.presentation.controller;

import com.example.board.user.application.dto.UserResponse;
import com.example.board.user.application.service.UserApplicationService;
import com.example.board.user.presentation.api.AdminApi;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController implements AdminApi {

    private final UserApplicationService userApplicationService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {
        UserResponse response = userApplicationService.findById(userId.toString());
        return ResponseEntity.ok(response);
    }
}