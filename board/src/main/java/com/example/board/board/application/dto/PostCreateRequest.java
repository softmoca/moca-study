package com.example.board.board.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "게시글 생성 요청")
public class PostCreateRequest {

    @NotNull(message = "게시판 ID는 필수입니다")
    @NotBlank(message = "게시판 ID는 필수입니다")
    @Schema(
            description = "게시글을 작성할 게시판 ID",
            example = "board-123e4567-e89b-12d3-a456-426614174000",
            required = true,
            format = "uuid"
    )
    private String boardId;

    @NotBlank(message = "제목은 필수입니다")
    @Size(min = 1, max = 200, message = "제목은 1자 이상 200자 이하여야 합니다")
    @Schema(
            description = "게시글 제목",
            example = "안녕하세요, 첫 게시글입니다!",
            required = true,
            minLength = 1,
            maxLength = 200
    )
    private String title;

    @NotBlank(message = "내용은 필수입니다")
    @Size(min = 1, max = 10000, message = "내용은 1자 이상 10000자 이하여야 합니다")
    @Schema(
            description = "게시글 내용",
            example = "게시판에 첫 번째 글을 작성해봅니다. 잘 부탁드립니다.",
            required = true,
            minLength = 1,
            maxLength = 10000
    )
    private String content;
}