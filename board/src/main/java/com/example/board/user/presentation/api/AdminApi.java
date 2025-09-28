package com.example.board.user.presentation.api;

import com.example.board.user.application.dto.UserResponse;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "🔧 관리자",
        description = """
        관리자 전용 API입니다.
        
        **접근 권한:**
        - ADMIN 역할이 필요합니다
        - 모든 API에 JWT 토큰 인증 필요
        
        **주요 기능:**
        - 사용자 정보 조회 (모든 사용자)
        - 시스템 관리 기능
        
        **참고:**
        - 일반 사용자는 접근할 수 없습니다
        - 관리자 권한 확인 후 API 실행
        """
)
@PreAuthorize("hasRole('ADMIN')")
public interface AdminApi {

    @Operation(
            summary = "사용자 정보 조회 (관리자용)",
            description = """
            관리자가 모든 사용자의 정보를 조회할 수 있습니다.
            
            **권한:**
            - ADMIN 역할 필요
            - 본인이 아닌 다른 사용자 정보도 조회 가능
            
            **용도:**
            - 사용자 관리
            - 문의 처리
            - 시스템 모니터링
            
            **주의:**
            - 비밀번호는 반환되지 않습니다
            - 비활성화된 사용자도 조회 가능합니다
            """,
            security = @SecurityRequirement(name = "bearerAuth"),
            tags = {"🔧 관리자"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "사용자 조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = UserResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "활성 사용자",
                                            value = """
                            {
                              "userId": "user-123e4567-e89b-12d3-a456-426614174000",
                              "email": "user@example.com",
                              "username": "johndoe",
                              "role": "USER",
                              "createdAt": "2024-01-01T10:00:00",
                              "active": true
                            }
                            """
                                    ),
                                    @ExampleObject(
                                            name = "비활성 사용자",
                                            value = """
                            {
                              "userId": "user-456e7890-e89b-12d3-a456-426614174001",
                              "email": "inactive@example.com",
                              "username": "inactiveuser",
                              "role": "USER",
                              "createdAt": "2024-01-01T09:00:00",
                              "active": false
                            }
                            """
                                    ),
                                    @ExampleObject(
                                            name = "다른 관리자",
                                            value = """
                            {
                              "userId": "admin-789e0123-e89b-12d3-a456-426614174002",
                              "email": "admin2@example.com",
                              "username": "admin2",
                              "role": "ADMIN",
                              "createdAt": "2024-01-01T08:00:00",
                              "active": true
                            }
                            """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "관리자 권한 없음",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "권한 없음",
                                    value = """
                        {
                          "code": "ACCESS_DENIED",
                          "message": "Access Denied"
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
                          "message": "User not found with id: user-123e4567-e89b-12d3-a456-426614174000"
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자"
            )
    })
    ResponseEntity<UserResponse> getUser(
            @Parameter(
                    description = "조회할 사용자 ID",
                    required = true,
                    example = "user-123e4567-e89b-12d3-a456-426614174000",
                    schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable String userId
    );

    // 향후 확장 가능한 관리자 기능들을 위한 주석
    /*
    @Operation(summary = "전체 사용자 목록 조회")
    ResponseEntity<List<UserResponse>> getAllUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(required = false) String search
    );

    @Operation(summary = "사용자 활성화/비활성화")
    ResponseEntity<UserResponse> toggleUserStatus(
        @PathVariable String userId,
        @RequestBody UserStatusUpdateRequest request
    );

    @Operation(summary = "사용자 역할 변경")
    ResponseEntity<UserResponse> changeUserRole(
        @PathVariable String userId,
        @RequestBody UserRoleUpdateRequest request
    );

    @Operation(summary = "시스템 통계 조회")
    ResponseEntity<SystemStatsResponse> getSystemStats();

    @Operation(summary = "게시글 통계 조회")
    ResponseEntity<PostStatsResponse> getPostStats(
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate
    );
    */
}