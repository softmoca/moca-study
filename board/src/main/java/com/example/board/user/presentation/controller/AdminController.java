package com.example.board.user.presentation.controller;

import com.example.board.user.application.dto.UserResponse;
import com.example.board.user.application.service.UserApplicationService;

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

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String userId) {
        UserResponse response = userApplicationService.findById(userId);
        return ResponseEntity.ok(response);
    }

    // 리프레시 토큰 관련 메서드들 제거
    // 액세스 토큰만 사용하는 stateless 방식에서는 서버에서 토큰을 무효화할 수 없음
}