package com.example.board.board.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardCreateRequest {

    @NotBlank(message = "게시판 이름은 필수입니다")
    @Size(min = 1, max = 100, message = "게시판 이름은 1자 이상 100자 이하여야 합니다")
    private String name;

    @Size(max = 1000, message = "게시판 설명은 1000자 이하여야 합니다")
    private String description;
}