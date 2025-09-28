package com.example.board.user.presentation.controller;

import com.example.board.user.application.dto.*;
import com.example.board.user.application.service.AuthenticationService;
import com.example.board.user.infrastructure.security.JwtUserPrincipal;
import com.example.board.user.domain.service.TokenDomainService;
import com.example.board.user.domain.model.UserId;

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
    private final TokenDomainService tokenDomainService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authenticationService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        // 리프레시 토큰이 없으므로 클라이언트에서 토큰을 삭제하면 됨
        // 서버에서 할 일은 없음 (stateless)
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal JwtUserPrincipal principal) {
        // JWT 토큰에서 바로 응답 생성 (DB 조회 없음)
        UserResponse response = new UserResponse(
                principal.getUserId(),
                principal.getEmail(),
                principal.getUsername(),
                null, // 역할 정보는 토큰에서 추출하지 않음 (필요시 추가 가능)
                null, // 생성일시는 토큰에 없음
                principal.isActive()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me/full")
    public ResponseEntity<UserResponse> getCurrentUserFull(@AuthenticationPrincipal JwtUserPrincipal principal) {
        // 최신 사용자 정보가 필요한 경우 DB 조회
        var user = tokenDomainService.getUserById(UserId.of(principal.getUserId()));
        UserResponse response = UserResponse.from(user);
        return ResponseEntity.ok(response);
    }
}