package com.example.board.board.presentation.api;

import com.example.board.board.application.dto.*;
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
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Tag(
        name = "📝 게시글",
        description = """
        게시글 관리 관련 API입니다.
        
        **주요 기능:**
        - 게시글 CRUD (생성, 조회, 수정, 삭제)
        - 게시글 목록 조회 (게시판별, 최신순, 인기순)
        - 조회수 관리
        
        **권한 정보:**
        - 생성: 로그인한 사용자
        - 조회: 누구나 (공개된 글만)
        - 수정/삭제: 작성자 또는 관리자
        """
)
public interface PostApi {

    @Operation(
            summary = "게시글 생성",
            description = """
            새로운 게시글을 생성합니다.
            
            **검증 규칙:**
            - 제목: 1-200자
            - 내용: 1-10,000자
            - 게시판: 존재하고 활성화된 게시판
            
            **기본 설정:**
            - 상태: PUBLISHED (게시됨)
            - 조회수: 0
            - 작성자: 현재 로그인한 사용자
            """,
            security = @SecurityRequirement(name = "bearerAuth"),
            tags = {"📝 게시글"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "게시글 생성 성공",
                    content = @Content(
                            schema = @Schema(implementation = PostResponse.class),
                            examples = @ExampleObject(
                                    name = "게시글 생성 성공",
                                    value = """
                        {
                          "postId": "post-123e4567-e89b-12d3-a456-426614174000",
                          "boardId": "board-123e4567-e89b-12d3-a456-426614174000",
                          "title": "안녕하세요, 첫 게시글입니다!",
                          "content": "게시판에 첫 번째 글을 작성해봅니다. 잘 부탁드립니다.",
                          "authorId": "user-123e4567-e89b-12d3-a456-426614174000",
                          "authorName": "johndoe",
                          "status": "PUBLISHED",
                          "viewCount": 0,
                          "createdAt": "2024-01-01T10:00:00",
                          "updatedAt": "2024-01-01T10:00:00"
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
                                            name = "비활성화된 게시판",
                                            value = """
                            {
                              "code": "BUSINESS_ERROR",
                              "message": "게시글 생성 실패: Inactive board cannot accept new posts"
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
                                "title": "제목은 필수입니다",
                                "content": "내용은 필수입니다"
                              }
                            }
                            """
                                    )
                            }
                    )
            )
    })
    ResponseEntity<PostResponse> createPost(
            @Parameter(
                    description = "게시글 생성 요청 데이터",
                    required = true,
                    schema = @Schema(implementation = PostCreateRequest.class)
            )
            @Valid @RequestBody PostCreateRequest request,

            @Parameter(hidden = true)
            @CurrentUser String userId
    );

    @Operation(
            summary = "게시글 조회",
            description = """
            게시글 ID로 게시글을 조회합니다.
            
            **조회 권한:**
            - 공개된 글(PUBLISHED): 누구나 조회 가능
            - 임시저장 글(DRAFT): 작성자 또는 관리자만
            - 삭제된 글(DELETED): 관리자만
            
            **조회수 증가:**
            - 작성자가 아닌 경우에만 조회수 증가
            """,
            security = @SecurityRequirement(name = "bearerAuth"),
            tags = {"📝 게시글"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글 조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = PostResponse.class),
                            examples = @ExampleObject(
                                    name = "게시글 조회 성공",
                                    value = """
                        {
                          "postId": "post-123e4567-e89b-12d3-a456-426614174000",
                          "boardId": "board-123e4567-e89b-12d3-a456-426614174000",
                          "title": "안녕하세요, 첫 게시글입니다!",
                          "content": "게시판에 첫 번째 글을 작성해봅니다. 잘 부탁드립니다.",
                          "authorId": "user-123e4567-e89b-12d3-a456-426614174000",
                          "authorName": "johndoe",
                          "status": "PUBLISHED",
                          "viewCount": 15,
                          "createdAt": "2024-01-01T10:00:00",
                          "updatedAt": "2024-01-01T10:00:00"
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "조회 권한 없음",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "조회 권한 없음",
                                    value = """
                        {
                          "code": "BUSINESS_ERROR",
                          "message": "게시글 조회 권한이 없습니다"
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "게시글을 찾을 수 없음"
            )
    })
    ResponseEntity<PostResponse> getPost(
            @Parameter(
                    description = "조회할 게시글 ID",
                    required = true,
                    example = "post-123e4567-e89b-12d3-a456-426614174000",
                    schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable String postId,

            @Parameter(hidden = true)
            @CurrentUser String userId
    );

    @Operation(
            summary = "게시글 수정",
            description = """
            게시글을 수정합니다.
            
            **수정 권한:**
            - 작성자 또는 관리자만 수정 가능
            - 삭제된 글은 수정 불가
            
            **수정 가능 항목:**
            - 제목, 내용만 수정 가능
            - 게시판, 작성자는 변경 불가
            """,
            security = @SecurityRequirement(name = "bearerAuth"),
            tags = {"📝 게시글"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글 수정 성공",
                    content = @Content(schema = @Schema(implementation = PostResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "수정 권한 없음",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "권한 없음",
                                    value = """
                        {
                          "code": "BUSINESS_ERROR",
                          "message": "Only author or admin can edit this post"
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "게시글을 찾을 수 없음"
            )
    })
    ResponseEntity<PostResponse> updatePost(
            @Parameter(
                    description = "수정할 게시글 ID",
                    required = true,
                    example = "post-123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable String postId,

            @Parameter(
                    description = "게시글 수정 요청 데이터",
                    required = true,
                    schema = @Schema(implementation = PostUpdateRequest.class)
            )
            @Valid @RequestBody PostUpdateRequest request,

            @Parameter(hidden = true)
            @CurrentUser String userId
    );

    @Operation(
            summary = "게시글 삭제",
            description = """
            게시글을 삭제합니다.
            
            **삭제 권한:**
            - 작성자 또는 관리자만 삭제 가능
            - 이미 삭제된 글은 삭제 불가
            
            **삭제 방식:**
            - 소프트 삭제 (DB에서 완전 삭제되지 않음)
            - 상태만 DELETED로 변경
            """,
            security = @SecurityRequirement(name = "bearerAuth"),
            tags = {"📝 게시글"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "게시글 삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "삭제 권한 없음"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "게시글을 찾을 수 없음"
            )
    })
    ResponseEntity<Void> deletePost(
            @Parameter(
                    description = "삭제할 게시글 ID",
                    required = true,
                    example = "post-123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable String postId,

            @Parameter(hidden = true)
            @CurrentUser String userId
    );

    @Operation(
            summary = "게시판별 게시글 목록 조회",
            description = """
            특정 게시판의 게시글 목록을 조회합니다.
            
            **특징:**
            - 공개된 글(PUBLISHED)만 조회
            - 생성일시 역순으로 정렬
            - 페이징 지원
            """,
            tags = {"📝 게시글"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글 목록 조회 성공",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = PostListResponse.class)),
                            examples = @ExampleObject(
                                    name = "게시글 목록",
                                    value = """
                        [
                          {
                            "postId": "post-123e4567-e89b-12d3-a456-426614174000",
                            "title": "안녕하세요, 첫 게시글입니다!",
                            "authorName": "johndoe",
                            "viewCount": 15,
                            "commentCount": 3,
                            "createdAt": "2024-01-01T10:00:00"
                          },
                          {
                            "postId": "post-456e7890-e89b-12d3-a456-426614174001",
                            "title": "두 번째 게시글",
                            "authorName": "jane",
                            "viewCount": 8,
                            "commentCount": 1,
                            "createdAt": "2024-01-01T09:30:00"
                          }
                        ]
                        """
                            )
                    )
            )
    })
    ResponseEntity<List<PostListResponse>> getPostsByBoard(
            @Parameter(
                    description = "게시판 ID",
                    required = true,
                    example = "board-123e4567-e89b-12d3-a456-426614174000"
            )
            @RequestParam String boardId,

            @Parameter(
                    description = "페이지 번호 (0부터 시작)",
                    example = "0",
                    schema = @Schema(type = "integer", minimum = "0", defaultValue = "0")
            )
            @RequestParam(defaultValue = "0") int page,

            @Parameter(
                    description = "페이지 크기",
                    example = "20",
                    schema = @Schema(type = "integer", minimum = "1", maximum = "100", defaultValue = "20")
            )
            @RequestParam(defaultValue = "20") int size
    );

    @Operation(
            summary = "최신 게시글 목록 조회",
            description = """
            모든 게시판의 최신 게시글을 조회합니다.
            
            **특징:**
            - 공개된 글(PUBLISHED)만 조회
            - 생성일시 역순으로 정렬
            - 개수 제한 가능
            """,
            tags = {"📝 게시글"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "최신 게시글 목록 조회 성공",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = PostListResponse.class))
                    )
            )
    })
    ResponseEntity<List<PostListResponse>> getRecentPosts(
            @Parameter(
                    description = "조회할 게시글 수",
                    example = "10",
                    schema = @Schema(type = "integer", minimum = "1", maximum = "100", defaultValue = "10")
            )
            @RequestParam(defaultValue = "10") int limit
    );

    @Operation(
            summary = "인기 게시글 목록 조회",
            description = """
            조회수가 높은 인기 게시글을 조회합니다.
            
            **특징:**
            - 공개된 글(PUBLISHED)만 조회
            - 조회수 역순으로 정렬
            - 동일 조회수인 경우 최신순
            """,
            tags = {"📝 게시글"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "인기 게시글 목록 조회 성공",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = PostListResponse.class))
                    )
            )
    })
    ResponseEntity<List<PostListResponse>> getPopularPosts(
            @Parameter(
                    description = "조회할 게시글 수",
                    example = "10",
                    schema = @Schema(type = "integer", minimum = "1", maximum = "100", defaultValue = "10")
            )
            @RequestParam(defaultValue = "10") int limit
    );
}