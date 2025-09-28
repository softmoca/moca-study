package com.example.board.user.presentation.api;

import com.example.board.user.application.dto.UserCreateRequest;
import com.example.board.user.application.dto.UserResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Tag(
        name = "👤 사용자",
        description = """
        사용자 관리 관련 API입니다.
        
        **주요 기능:**
        - 회원가입 (누구나 가능)
        - 사용자 정보 조회 (본인 또는 관리자만)
        
        **참고:**
        - 회원가입은 인증 없이 가능합니다
        - 사용자 조회는 인증이 필요합니다
        """
)
public interface UserApi {

    @Operation(
            summary = "회원가입",
            description = """
            새로운 사용자를 생성합니다.
            
            **검증 규칙:**
            - 이메일: 올바른 형식, 중복 불가
            - 사용자명: 3-20자, 중복 불가
            - 비밀번호: 8-50자
            
            **기본 설정:**
            - 역할: USER (일반 사용자)
            - 상태: 활성화
            """,
            tags = {"👤 사용자"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "회원가입 성공",
                    content = @Content(
                            schema = @Schema(implementation = UserResponse.class),
                            examples = @ExampleObject(
                                    name = "회원가입 성공 예시",
                                    value = """
                        {
                          "userId": "123e4567-e89b-12d3-a456-426614174000",
                          "email": "newuser@example.com",
                          "username": "newuser",
                          "role": "USER",
                          "createdAt": "2024-01-01T10:00:00",
                          "active": true
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 데이터",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name = "이메일 중복",
                                            value = """
                            {
                              "code": "BUSINESS_ERROR",
                              "message": "사용자 생성 실패: Email already exists: user@example.com"
                            }
                            """
                                    ),
                                    @ExampleObject(
                                            name = "사용자명 중복",
                                            value = """
                            {
                              "code": "BUSINESS_ERROR", 
                              "message": "사용자 생성 실패: Username already exists: johndoe"
                            }
                            """
                                    ),
                                    @ExampleObject(
                                            name = "입력값 검증 실패",
                                            value = """
                            {
                              "code": "VALIDATION_FAILED",
                              "message": "입력값 검증에 실패했습니다",
                              "errors": {
                                "email": "올바른 이메일 형식이 아닙니다",
                                "password": "비밀번호는 8자 이상 50자 이하여야 합니다"
                              }
                            }
                            """
                                    )
                            }
                    )
            )
    })
    ResponseEntity<UserResponse> createUser(
            @Parameter(
                    description = "사용자 생성 요청 데이터",
                    required = true,
                    schema = @Schema(implementation = UserCreateRequest.class)
            )
            @Valid @RequestBody UserCreateRequest request
    );

    @Operation(
            summary = "사용자 정보 조회",
            description = """
            사용자 ID로 사용자 정보를 조회합니다.
            
            **권한:**
            - 본인의 정보는 누구나 조회 가능
            - 다른 사용자 정보는 관리자만 조회 가능
            
            **주의:**
            - 비밀번호는 반환되지 않습니다
            - 비활성화된 사용자도 조회 가능합니다
            """,
            tags = {"👤 사용자"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "사용자 조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = UserResponse.class),
                            examples = @ExampleObject(
                                    name = "사용자 정보 조회 성공",
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
                    responseCode = "404",
                    description = "사용자를 찾을 수 없음",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "사용자 없음",
                                    value = """
                        {
                          "code": "ENTITY_NOT_FOUND",
                          "message": "User not found with id: 123e4567-e89b-12d3-a456-426614174000"
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한 없음 (다른 사용자 정보 조회 시)"
            )
    })
    ResponseEntity<UserResponse> getUser(
            @Parameter(
                    description = "조회할 사용자 ID",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000",
                    schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable String userId
    );
}