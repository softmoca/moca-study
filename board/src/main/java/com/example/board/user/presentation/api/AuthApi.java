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

@Tag(
        name = "🔐 인증",
        description = """
        사용자 인증 관련 API입니다.
        
        **주요 기능:**
        - 로그인
        - 현재 사용자 정보 조회
        - JWT 토큰 기반 인증
        """
)
public interface AuthApi {

    @Operation(
            summary = "로그인",
            description = """
            **성공 시:**
            - JWT 액세스 토큰 1시간짜리  반환합니다
            **실패 조건:**
            - 존재하지 않는 이메일
            - 잘못된 비밀번호
            - 비활성화된 계정
            """,
            tags = {"🔐 인증"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(
                            schema = @Schema(implementation = LoginResponse.class),
                            examples = @ExampleObject(
                                    name = "로그인 성공 예시",
                                    value = """
                        {
                          "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                          "accessTokenExpiresAt": "2024-01-01T15:00:00",
                          "tokenType": "Bearer",
                          "user": {
                            "userId": "123e4567-e89b-12d3-a456-426614174000",
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
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "로그인 실패",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "로그인 실패 예시",
                                    value = """
                        {
                          "code": "BUSINESS_ERROR",
                          "message": "이메일 또는 비밀번호가 잘못되었습니다"
                        }
                        """
                            )
                    )
            )
    })
    ResponseEntity<LoginResponse> login(
            @Parameter(
                    description = "로그인 요청 정보",
                    required = true,
                    schema = @Schema(implementation = LoginRequest.class)
            )
            @Valid @RequestBody LoginRequest request
    );



    @Operation(
            summary = "현재 사용자 정보 조회 (토큰 기반)",
            description = """
            JWT 토큰에서 추출한 사용자 정보를 반환합니다.
            """,
            security = @SecurityRequirement(name = "bearerAuth"),
            tags = {"🔐 인증"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "사용자 정보 조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = UserResponse.class),
                            examples = @ExampleObject(
                                    name = "현재 사용자 정보",
                                    value = """
                        {
                          "userId": "123e4567-e89b-12d3-a456-426614174000",
                          "email": "user@example.com",
                          "username": "johndoe",
                          "role": null,
                          "createdAt": null,
                          "active": true
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "유효하지 않은 토큰"
            )
    })
    ResponseEntity<UserResponse> getCurrentUser(
            @Parameter(hidden = true) // Swagger UI에서 숨김
            @AuthenticationPrincipal JwtUserPrincipal principal
    );

    @Operation(
            summary = "현재 사용자 정보 조회 (DB 기반)",
            description = """
            DB에서 최신 사용자 정보를 조회합니다.
            
            **특징:**
            - DB에서 최신 정보 조회
            - 모든 정보 포함 (역할, 생성일시 등)
            - 토큰 정보보다 느리지만 정확
            """,
            security = @SecurityRequirement(name = "bearerAuth"),
            tags = {"🔐 인증"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "사용자 정보 조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = UserResponse.class),
                            examples = @ExampleObject(
                                    name = "완전한 사용자 정보",
                                    value = """
                        {
                          "userId": "123e4567-e89b-12d3-a456-426614174000",
                          "email": "user@example.com",
                          "username": "johndoe",
                          "role": "USER",
                          "createdAt": "2024-01-01T10:00:00",
                          "active": true
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "유효하지 않은 토큰"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾을 수 없음"
            )
    })
    ResponseEntity<UserResponse> getCurrentUserFull(
            @Parameter(hidden = true)
            @AuthenticationPrincipal JwtUserPrincipal principal
    );
}