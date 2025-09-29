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
        description = " 게시판 관리 관련 API입니다. "
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
                          "boardId": 1,
                          "name": "자유게시판",
                          "description": "자유롭게 글을 작성할 수 있는 게시판입니다",
                          "createdBy": 1,
                          "createdAt": "2024-01-01T10:00:00",
                          "active": true,
                          "postCount": 0
                        }
                        """
                            )
                    )
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<BoardResponse> createBoard(
            @Parameter(description = "게시판 생성 요청 데이터", required = true)
            @Valid @RequestBody BoardCreateRequest request,
            @Parameter(hidden = true) @CurrentUser String userId
    );

    @Operation(summary = "전체 게시판 목록 조회")
    ResponseEntity<List<BoardResponse>> getAllBoards();

    @Operation(summary = "게시판 정보 조회")
    ResponseEntity<BoardResponse> getBoard(
            @Parameter(description = "조회할 게시판 ID", required = true, example = "1")
            @PathVariable Long boardId
    );
}