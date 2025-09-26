package com.example.board.board.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PostCreateRequest {

    @NotNull(message = "게시판 ID는 필수입니다")
    @NotBlank(message = "게시판 ID는 필수입니다")
    private String boardId;

    @NotBlank(message = "제목은 필수입니다")
    @Size(min = 1, max = 200, message = "제목은 1자 이상 200자 이하여야 합니다")
    private String title;

    @NotBlank(message = "내용은 필수입니다")
    @Size(min = 1, max = 10000, message = "내용은 1자 이상 10000자 이하여야 합니다")
    private String content;

    // 기본 생성자
    public PostCreateRequest() {}

    // 생성자
    public PostCreateRequest(String boardId, String title, String content) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
    }

    // Getter/Setter
    public String getBoardId() { return boardId; }
    public void setBoardId(String boardId) { this.boardId = boardId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}