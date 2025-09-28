package com.example.board.board.presentation.api;

import com.example.board.board.application.dto.BoardCreateRequest;
import com.example.board.board.application.dto.BoardResponse;
import com.example.board.common.annotation.CurrentUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

import jakarta.validation.Valid;
import java.util.List;

@Tag(
        name = "📁 게시판",
        description = """
        게시판 관리 관련 API입니다.
        
        **주요 기능:**
        - 게시판 생성 (관리자만)
        - 게시판 목록 조회 (누구나)
        - 게시판 정보 조회 (누구나)
        
        **권한 정보:**
        - 게시판 생성: ADMIN 권한 필요
        - 게시판 조회: 인증 불필요 (public)
        """
)
public interface BoardApi {

    @Operation(
            summary = "게시판 생성",
            description = """
            새로운 게시판을 생성합니다.
            
            **권한:**
            - 관리자(ADMIN) 권한이 필요합니다
            
            **검증 규칙:**
            - 게시판 이름: 1-100자, 중복 불가
            - 설명: 1000자 이하 (선택사항)
            
            **기본 설정:**
            - 상태: 활성화
            - 생성자: 현재 로그인한 관리자
            """,
            security = @SecurityRequirement(name = "bearerAuth"),
            tags = {"📁 게시판"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "게시판 생성 성공",
                    content = @Content(
                            schema = @Schema(implementation = BoardResponse.class),
                            examples = @ExampleObject(
                                    name = "게시판 생성 성공 예시",
                                    value = """
                        {
                          "boardId": "board-123e4567-e89b-12d3-a456-426614174000",
                          "name": "자유게시판",
                          "description": "자유롭게 글을 작성할 수 있는 게시판입니다",
                          "createdBy": "admin-123e4567-e89b-12d3-a456-426614174000",
                          "createdAt": "2024-01-01T10:00:00",
                          "active": true,
                          "postCount": 0
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
                                            name = "게시판 이름 중복",
                                            value = """
                            {
                              "code": "BUSINESS_ERROR",
                              "message": "게시판 생성 실패: 이미 존재하는 게시판 이름입니다: 자유게시판"
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
                                "name": "게시판 이름은 필수입니다"
                              }
                            }
                            """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한 없음 (관리자 권한 필요)",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "권한 없음",
                                    value = """
                        {
                          "code": "BUSINESS_ERROR",
                          "message": "게시판 생성 권한이 없습니다. 관리자만 게시판을 생성할 수 있습니다."
                        }
                        """
                            )
                    )
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<BoardResponse> createBoard(
            @Parameter(
                    description = "게시판 생성 요청 데이터",
                    required = true,
                    schema = @Schema(implementation = BoardCreateRequest.class)
            )
            @Valid @RequestBody BoardCreateRequest request,

            @Parameter(hidden = true) // Swagger UI에서 숨김
            @CurrentUser String userId
    );

    @Operation(
            summary = "전체 게시판 목록 조회",
            description = """
            활성화된 모든 게시판 목록을 조회합니다.
            
            **특징:**
            - 인증 없이 조회 가능
            - 활성화된 게시판만 반환
            - 생성일시 역순으로 정렬
            - 각 게시판의 게시글 수 포함
            """,
            tags = {"📁 게시판"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "게시판 목록 조회 성공",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = BoardResponse.class)),
                            examples = @ExampleObject(
                                    name = "게시판 목록 조회 성공",
                                    value = """
                        [
                          {
                            "boardId": "board-123e4567-e89b-12d3-a456-426614174000",
                            "name": "자유게시판",
                            "description": "자유롭게 글을 작성할 수 있는 게시판입니다",
                            "createdBy": "admin-123e4567-e89b-12d3-a456-426614174000",
                            "createdAt": "2024-01-01T10:00:00",
                            "active": true,
                            "postCount": 15
                          },
                          {
                            "boardId": "board-456e7890-e89b-12d3-a456-426614174001",
                            "name": "공지사항",
                            "description": "중요한 공지사항을 확인하세요",
                            "createdBy": "admin-123e4567-e89b-12d3-a456-426614174000",
                            "createdAt": "2024-01-01T09:00:00",
                            "active": true,
                            "postCount": 3
                          }
                        ]
                        """
                            )
                    )
            )
    })
    ResponseEntity<List<BoardResponse>> getAllBoards();

    @Operation(
            summary = "게시판 정보 조회",
            description = """
            특정 게시판의 상세 정보를 조회합니다.
            
            **특징:**
            - 인증 없이 조회 가능
            - 비활성화된 게시판도 조회 가능
            - 게시글 수 포함
            """,
            tags = {"📁 게시판"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "게시판 조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = BoardResponse.class),
                            examples = @ExampleObject(
                                    name = "게시판 조회 성공",
                                    value = """
                        {
                          "boardId": "board-123e4567-e89b-12d3-a456-426614174000",
                          "name": "자유게시판",
                          "description": "자유롭게 글을 작성할 수 있는 게시판입니다",
                          "createdBy": "admin-123e4567-e89b-12d3-a456-426614174000",
                          "createdAt": "2024-01-01T10:00:00",
                          "active": true,
                          "postCount": 15
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "게시판을 찾을 수 없음",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "게시판 없음",
                                    value = """
                        {
                          "code": "ENTITY_NOT_FOUND",
                          "message": "Board not found with id: board-123e4567-e89b-12d3-a456-426614174000"
                        }
                        """
                            )
                    )
            )
    })
    ResponseEntity<BoardResponse> getBoard(
            @Parameter(
                    description = "조회할 게시판 ID",
                    required = true,
                    example = "board-123e4567-e89b-12d3-a456-426614174000",
                    schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable String boardId
    );
}