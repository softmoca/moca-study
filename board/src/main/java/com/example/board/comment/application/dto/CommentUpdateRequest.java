package com.example.board.comment.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CommentUpdateRequest {

    @NotBlank(message = "댓글 내용은 필수입니다")
    @Size(min = 1, max = 1000, message = "댓글 내용은 1자 이상 1000자 이하여야 합니다")
    private String content;

    // 기본 생성자
    public CommentUpdateRequest() {}

    // 생성자
    public CommentUpdateRequest(String content) {
        this.content = content;
    }

    // Getter/Setter
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}