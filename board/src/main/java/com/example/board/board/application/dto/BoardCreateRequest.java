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
@Schema(description = "게시판 생성 요청")
public class BoardCreateRequest {

    @NotBlank(message = "게시판 이름은 필수입니다")
    @Size(min = 1, max = 100, message = "게시판 이름은 1자 이상 100자 이하여야 합니다")
    @Schema(
            description = "게시판 이름 (고유해야 함)",
            example = "자유게시판",
            required = true,
            minLength = 1,
            maxLength = 100
    )
    private String name;

    @Size(max = 1000, message = "게시판 설명은 1000자 이하여야 합니다")
    @Schema(
            description = "게시판 설명",
            example = "자유롭게 글을 작성할 수 있는 게시판입니다",
            maxLength = 1000
    )
    private String description;
}
