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

@Tag(name = "📝 게시글", description = "게시글 관리 관련 API")
public interface PostApi {

    @Operation(
            summary = "게시글 생성",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "게시글 생성 성공",
                    content = @Content(
                            schema = @Schema(implementation = PostResponse.class),
                            examples = @ExampleObject(
                                    value = """
                        {
                          "postId": 1,
                          "boardId": 1,
                          "title": "안녕하세요, 첫 게시글입니다!",
                          "content": "게시판에 첫 번째 글을 작성해봅니다.",
                          "authorId": 1,
                          "authorName": "johndoe",
                          "status": "PUBLISHED",
                          "viewCount": 0,
                          "createdAt": "2024-01-01T10:00:00",
                          "updatedAt": "2024-01-01T10:00:00"
                        }
                        """
                            )
                    )
            )
    })
    ResponseEntity<PostResponse> createPost(
            @Valid @RequestBody PostCreateRequest request,
            @Parameter(hidden = true) @CurrentUser String userId
    );

    @Operation(
            summary = "게시글 조회",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    ResponseEntity<PostResponse> getPost(
            @Parameter(description = "조회할 게시글 ID", required = true, example = "1")
            @PathVariable Long postId,
            @Parameter(hidden = true) @CurrentUser String userId
    );

    @Operation(
            summary = "게시글 수정",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    ResponseEntity<PostResponse> updatePost(
            @Parameter(description = "수정할 게시글 ID", required = true, example = "1")
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequest request,
            @Parameter(hidden = true) @CurrentUser String userId
    );

    @Operation(
            summary = "게시글 삭제",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    ResponseEntity<Void> deletePost(
            @Parameter(description = "삭제할 게시글 ID", required = true, example = "1")
            @PathVariable Long postId,
            @Parameter(hidden = true) @CurrentUser String userId
    );

    @Operation(summary = "게시판별 게시글 목록 조회")
    ResponseEntity<List<PostListResponse>> getPostsByBoard(
            @Parameter(description = "게시판 ID", required = true, example = "1")
            @RequestParam Long boardId,

            @Parameter(description = "페이지 번호", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "페이지 크기", example = "20")
            @RequestParam(defaultValue = "20") int size
    );

    @Operation(summary = "최신 게시글 목록 조회")
    ResponseEntity<List<PostListResponse>> getRecentPosts(
            @Parameter(description = "조회할 게시글 수", example = "10")
            @RequestParam(defaultValue = "10") int limit
    );

    @Operation(summary = "인기 게시글 목록 조회")
    ResponseEntity<List<PostListResponse>> getPopularPosts(
            @Parameter(description = "조회할 게시글 수", example = "10")
            @RequestParam(defaultValue = "10") int limit
    );
}