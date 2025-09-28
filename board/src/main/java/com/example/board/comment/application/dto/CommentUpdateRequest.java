package com.example.board.comment.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "댓글 수정 요청")
public class CommentUpdateRequest {

    @NotBlank(message = "댓글 내용은 필수입니다")
    @Size(min = 1, max = 1000, message = "댓글 내용은 1자 이상 1000자 이하여야 합니다")
    @Schema(
            description = "수정할 댓글 내용",
            example = "수정된 댓글 내용입니다.",
            required = true,
            minLength = 1,
            maxLength = 1000
    )
    private String content;
}