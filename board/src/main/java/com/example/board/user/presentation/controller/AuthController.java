
package com.example.board.user.presentation.controller;

import com.example.board.user.application.dto.LoginRequest;
import com.example.board.user.application.dto.LoginResponse;
import com.example.board.user.application.service.AuthenticationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authenticationService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        // JWT 기반에서는 클라이언트에서 토큰을 삭제하면 됨
        // 서버에서 블랙리스트 관리가 필요한 경우 별도 구현
        return ResponseEntity.ok().build();
    }
}