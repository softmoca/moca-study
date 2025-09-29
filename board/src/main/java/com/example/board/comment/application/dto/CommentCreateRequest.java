package com.example.board.comment.application.dto;

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
@Schema(description = "댓글 생성 요청")
public class CommentCreateRequest {

    @NotNull(message = "게시글 ID는 필수입니다")
    @NotBlank(message = "게시글 ID는 필수입니다")
    @Schema(
            description = "댓글을 작성할 게시글 ID",
            example = "1",
            required = true
    )
    private String postId;  // String으로 받아서 Long으로 변환

    @NotBlank(message = "댓글 내용은 필수입니다")
    @Size(min = 1, max = 1000, message = "댓글 내용은 1자 이상 1000자 이하여야 합니다")
    @Schema(
            description = "댓글 내용",
            example = "좋은 글 감사합니다!",
            required = true
    )
    private String content;

    @Schema(
            description = "부모 댓글 ID (대댓글인 경우에만 지정)",
            example = "1"
    )
    private String parentCommentId;  // String으로 받아서 Long으로 변환
}