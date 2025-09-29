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

@Tag(name = "💬 댓글", description = "댓글 관리 관련 API")
public interface CommentApi {

    @Operation(
            summary = "댓글 생성",
            security = @SecurityRequirement(name = "bearerAuth")
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
                                      "commentId": 1,
                                      "postId": 1,
                                      "content": "좋은 글 감사합니다!",
                                      "authorId": 1,
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
                                      "commentId": 2,
                                      "postId": 1,
                                      "content": "저도 동감합니다!",
                                      "authorId": 2,
                                      "authorName": "jane",
                                      "status": "ACTIVE",
                                      "parentCommentId": 1,
                                      "createdAt": "2024-01-01T10:05:00",
                                      "updatedAt": "2024-01-01T10:05:00",
                                      "replies": []
                                    }
                                    """
                                    )
                            }
                    )
            )
    })
    ResponseEntity<CommentResponse> createComment(
            @Valid @RequestBody CommentCreateRequest request,
            @Parameter(hidden = true) @CurrentUser String userId
    );

    @Operation(
            summary = "댓글 수정",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    ResponseEntity<CommentResponse> updateComment(
            @Parameter(description = "수정할 댓글 ID", required = true, example = "1")
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequest request,
            @Parameter(hidden = true) @CurrentUser String userId
    );

    @Operation(
            summary = "댓글 삭제",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    ResponseEntity<Void> deleteComment(
            @Parameter(description = "삭제할 댓글 ID", required = true, example = "1")
            @PathVariable Long commentId,
            @Parameter(hidden = true) @CurrentUser String userId
    );

    @Operation(summary = "게시글별 댓글 목록 조회")
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
                        "commentId": 1,
                        "postId": 1,
                        "content": "좋은 글 감사합니다!",
                        "authorId": 1,
                        "authorName": "johndoe",
                        "status": "ACTIVE",
                        "parentCommentId": null,
                        "createdAt": "2024-01-01T10:00:00",
                        "updatedAt": "2024-01-01T10:00:00",
                        "replies": [
                          {
                            "commentId": 2,
                            "postId": 1,
                            "content": "저도 동감합니다!",
                            "authorId": 2,
                            "authorName": "jane",
                            "status": "ACTIVE",
                            "parentCommentId": 1,
                            "createdAt": "2024-01-01T10:05:00",
                            "updatedAt": "2024-01-01T10:05:00",
                            "replies": []
                          }
                        ]
                      }
                    ]
                    """
                            )
                    )
            )
    })
    ResponseEntity<List<CommentResponse>> getCommentsByPost(
            @Parameter(description = "댓글을 조회할 게시글 ID", required = true, example = "1")
            @PathVariable Long postId
    );
}