package com.example.board.board.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "게시글 수정 요청")
public class PostUpdateRequest {

    @NotBlank(message = "제목은 필수입니다")
    @Size(min = 1, max = 200, message = "제목은 1자 이상 200자 이하여야 합니다")
    @Schema(
            description = "수정할 게시글 제목",
            example = "수정된 게시글 제목입니다",
            required = true,
            minLength = 1,
            maxLength = 200
    )
    private String title;

    @NotBlank(message = "내용은 필수입니다")
    @Size(min = 1, max = 10000, message = "내용은 1자 이상 10000자 이하여야 합니다")
    @Schema(
            description = "수정할 게시글 내용",
            example = "수정된 게시글 내용입니다. 추가 정보를 포함했습니다.",
            required = true,
            minLength = 1,
            maxLength = 10000
    )
    private String content;
}
