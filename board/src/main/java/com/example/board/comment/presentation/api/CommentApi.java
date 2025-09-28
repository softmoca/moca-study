package com.example.board.comment.presentation.api;

import com.example.board.comment.application.dto.CommentCreateRequest;
import com.example.board.comment.application.dto.CommentUpdateRequest;
import com.example.board.comment.application.dto.CommentResponse;
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
        name = "💬 댓글",
        description = """
        댓글 관리 관련 API입니다.
        
        **주요 기능:**
        - 댓글/대댓글 CRUD (생성, 조회, 수정, 삭제)
        - 계층형 댓글 구조 (2depth까지 지원)
        - 게시글별 댓글 목록 조회
        
        **권한 정보:**
        - 생성: 로그인한 사용자
        - 조회: 누구나 (활성 댓글만)
        - 수정/삭제: 작성자 또는 관리자
        
        **댓글 구조:**
        - 최상위 댓글 (parentCommentId: null)
        - 대댓글 (parentCommentId: 부모댓글ID)
        - 대댓글의 대댓글은 불가 (2depth 제한)
        """
)
public interface CommentApi {

    @Operation(
            summary = "댓글 생성",
            description = """
                    게시글에 댓글 또는 대댓글을 생성합니다.
                    
                    **댓글 타입:**
                    - 최상위 댓글: parentCommentId를 null 또는 생략
                    - 대댓글: parentCommentId에 부모 댓글 ID 지정
                    
                    **검증 규칙:**
                    - 내용: 1-1,000자
                    - 게시글: 존재하고 조회 가능한 게시글
                    - 부모 댓글: 존재하고 활성화된 댓글 (대댓글인 경우)
                    
                    **제한사항:**
                    - 삭제된 게시글에는 댓글 작성 불가
                    - 삭제된 댓글에는 대댓글 작성 불가
                    - 대댓글의 대댓글은 작성 불가 (2depth 제한)
                    """,
            security = @SecurityRequirement(name = "bearerAuth"),
            tags = {"💬 댓글"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "댓글 생성 성공",
                    content = @Content(
                            schema = @Schema(implementation = CommentResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "최상위 댓글 생성",
                                            value = """
                                                    {
                                                      "commentId": "comment-123e4567-e89b-12d3-a456-426614174000",
                                                      "postId": "post-123e4567-e89b-12d3-a456-426614174000",
                                                      "content": "좋은 글 감사합니다!",
                                                      "authorId": "user-123e4567-e89b-12d3-a456-426614174000",
                                                      "authorName": "johndoe",
                                                      "status": "ACTIVE",
                                                      "parentCommentId": null,
                                                      "createdAt": "2024-01-01T10:00:00",
                                                      "updatedAt": "2024-01-01T10:00:00",
                                                      "replies": []
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "대댓글 생성",
                                            value = """
                                                    {
                                                      "commentId": "comment-456e7890-e89b-12d3-a456-426614174001",
                                                      "postId": "post-123e4567-e89b-12d3-a456-426614174000",
                                                      "content": "저도 동감합니다!",
                                                      "authorId": "user-456e7890-e89b-12d3-a456-426614174001",
                                                      "authorName": "jane",
                                                      "status": "ACTIVE",
                                                      "parentCommentId": "comment-123e4567-e89b-12d3-a456-426614174000",
                                                      "createdAt": "2024-01-01T10:05:00",
                                                      "updatedAt": "2024-01-01T10:05:00",
                                                      "replies": []
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 데이터",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name = "삭제된 게시글",
                                            value = """
                                                    {
                                                      "code": "BUSINESS_ERROR",
                                                      "message": "댓글 생성 실패: Cannot comment on deleted post"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "대댓글의 대댓글 시도",
                                            value = """
                                                    {
                                                      "code": "BUSINESS_ERROR",
                                                      "message": "댓글 생성 실패: Cannot reply to a reply comment"
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
                                                        "content": "댓글 내용은 필수입니다",
                                                        "postId": "게시글 ID는 필수입니다"
                                                      }
                                                    }
                                                    """
                                    )
                            }
                    )
            )
    })
    ResponseEntity<CommentResponse> createComment(
            @Parameter(
                    description = "댓글 생성 요청 데이터",
                    required = true,
                    schema = @Schema(implementation = CommentCreateRequest.class)
            )
            @Valid @RequestBody CommentCreateRequest request,

            @Parameter(hidden = true)
            @CurrentUser String userId
    );

    @Operation(
            summary = "댓글 수정",
            description = """
                    댓글 내용을 수정합니다.
                    
                    **수정 권한:**
                    - 작성자 또는 관리자만 수정 가능
                    - 삭제된 댓글은 수정 불가
                    
                    **수정 가능 항목:**
                    - 내용만 수정 가능
                    - 게시글, 부모 댓글은 변경 불가
                    """,
            security = @SecurityRequirement(name = "bearerAuth"),
            tags = {"💬 댓글"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "댓글 수정 성공",
                    content = @Content(
                            schema = @Schema(implementation = CommentResponse.class),
                            examples = @ExampleObject(
                                    name = "댓글 수정 성공",
                                    value = """
                                            {
                                              "commentId": "comment-123e4567-e89b-12d3-a456-426614174000",
                                              "postId": "post-123e4567-e89b-12d3-a456-426614174000",
                                              "content": "수정된 댓글 내용입니다.",
                                              "authorId": "user-123e4567-e89b-12d3-a456-426614174000",
                                              "authorName": "johndoe",
                                              "status": "ACTIVE",
                                              "parentCommentId": null,
                                              "createdAt": "2024-01-01T10:00:00",
                                              "updatedAt": "2024-01-01T10:30:00",
                                              "replies": []
                                            }
                                            """
                            )
                    )
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
                                              "message": "Only author or admin can edit this comment"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "댓글을 찾을 수 없음"
            )
    })
    ResponseEntity<CommentResponse> updateComment(
            @Parameter(
                    description = "수정할 댓글 ID",
                    required = true,
                    example = "comment-123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable String commentId,

            @Parameter(
                    description = "댓글 수정 요청 데이터",
                    required = true,
                    schema = @Schema(implementation = CommentUpdateRequest.class)
            )
            @Valid @RequestBody CommentUpdateRequest request,

            @Parameter(hidden = true)
            @CurrentUser String userId
    );

    @Operation(
            summary = "댓글 삭제",
            description = """
                    댓글을 삭제합니다.
                    
                    **삭제 권한:**
                    - 작성자 또는 관리자만 삭제 가능
                    - 이미 삭제된 댓글은 삭제 불가
                    
                    **삭제 방식:**
                    - 소프트 삭제 (DB에서 완전 삭제되지 않음)
                    - 상태만 DELETED로 변경
                    
                    **대댓글 처리:**
                    - 대댓글이 있는 댓글도 삭제 가능
                    - 부모 댓글 삭제 시 대댓글은 유지됨
                    """,
            security = @SecurityRequirement(name = "bearerAuth"),
            tags = {"💬 댓글"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "댓글 삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "삭제 권한 없음"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "댓글을 찾을 수 없음"
            )
    })
    ResponseEntity<Void> deleteComment(
            @Parameter(
                    description = "삭제할 댓글 ID",
                    required = true,
                    example = "comment-123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable String commentId,

            @Parameter(hidden = true)
            @CurrentUser String userId

    );

    @Operation(
            summary = "게시글별 댓글 목록 조회",
            description = """
        특정 게시글의 모든 댓글을 계층형 구조로 조회합니다.
        
        **특징:**
        - 활성화된 댓글(ACTIVE)만 조회
        - 계층형 구조로 반환 (부모-자식 관계 유지)
        - 생성일시 오름차순 정렬
        
        **응답 구조:**
        - 최상위 댓글들이 배열로 반환
        - 각 댓글의 replies 필드에 대댓글들 포함
        - 대댓글들도 생성일시 오름차순 정렬
        """,
            tags = {"💬 댓글"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "댓글 목록 조회 성공",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = CommentResponse.class)),
                            examples = @ExampleObject(
                                    name = "계층형 댓글 목록",
                                    value = """
                    [
                      {
                        "commentId": "comment-123e4567-e89b-12d3-a456-426614174000",
                        "postId": "post-123e4567-e89b-12d3-a456-426614174000",
                        "content": "좋은 글 감사합니다!",
                        "authorId": "user-123e4567-e89b-12d3-a456-426614174000",
                        "authorName": "johndoe",
                        "status": "ACTIVE",
                        "parentCommentId": null,
                        "createdAt": "2024-01-01T10:00:00",
                        "updatedAt": "2024-01-01T10:00:00",
                        "replies": [
                          {
                            "commentId": "comment-456e7890-e89b-12d3-a456-426614174001",
                            "postId": "post-123e4567-e89b-12d3-a456-426614174000",
                            "content": "저도 동감합니다!",
                            "authorId": "user-456e7890-e89b-12d3-a456-426614174001",
                            "authorName": "jane",
                            "status": "ACTIVE",
                            "parentCommentId": "comment-123e4567-e89b-12d3-a456-426614174000",
                            "createdAt": "2024-01-01T10:05:00",
                            "updatedAt": "2024-01-01T10:05:00",
                            "replies": []
                          }
                        ]
                      },
                      {
                        "commentId": "comment-789e0123-e89b-12d3-a456-426614174002",
                        "postId": "post-123e4567-e89b-12d3-a456-426614174000",
                        "content": "또 다른 댓글입니다.",
                        "authorId": "user-789e0123-e89b-12d3-a456-426614174002",
                        "authorName": "bob",
                        "status": "ACTIVE",
                        "parentCommentId": null,
                        "createdAt": "2024-01-01T10:10:00",
                        "updatedAt": "2024-01-01T10:10:00",
                        "replies": []
                      }
                    ]
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "게시글을 찾을 수 없음",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "게시글 없음",
                                    value = """
                    {
                      "code": "ENTITY_NOT_FOUND",
                      "message": "Post not found with id: post-123e4567-e89b-12d3-a456-426614174000"
                    }
                    """
                            )
                    )
            )
    })
    ResponseEntity<List<CommentResponse>> getCommentsByPost(
            @Parameter(
                    description = "댓글을 조회할 게시글 ID",
                    required = true,
                    example = "post-123e4567-e89b-12d3-a456-426614174000",
                    schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable String postId
    );
}