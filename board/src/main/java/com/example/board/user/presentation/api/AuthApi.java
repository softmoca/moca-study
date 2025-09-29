package com.example.board.user.presentation.api;

import com.example.board.user.application.dto.*;
import com.example.board.user.infrastructure.security.JwtUserPrincipal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Tag(name = "🔐 인증", description = "사용자 인증 관련 API")
public interface AuthApi {

    @Operation(summary = "로그인")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(
                            schema = @Schema(implementation = LoginResponse.class),
                            examples = @ExampleObject(
                                    value = """
                        {
                          "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                          "accessTokenExpiresAt": "2024-01-01T15:00:00",
                          "tokenType": "Bearer",
                          "user": {
                            "userId": 1,
                            "email": "user@example.com",
                            "username": "johndoe",
                            "role": "USER",
                            "createdAt": "2024-01-01T10:00:00",
                            "active": true
                          }
                        }
                        """
                            )
                    )
            )
    })
    ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request);

    @Operation(
            summary = "현재 사용자 정보 조회 (토큰 기반)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    ResponseEntity<UserResponse> getCurrentUser(
            @Parameter(hidden = true) @AuthenticationPrincipal JwtUserPrincipal principal
    );

    @Operation(
            summary = "현재 사용자 정보 조회 (DB 기반)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    ResponseEntity<UserResponse> getCurrentUserFull(
            @Parameter(hidden = true) @AuthenticationPrincipal JwtUserPrincipal principal
    );
}