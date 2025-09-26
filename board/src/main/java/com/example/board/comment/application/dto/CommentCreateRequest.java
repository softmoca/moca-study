package com.example.board.comment.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CommentCreateRequest {

    @NotNull(message = "게시글 ID는 필수입니다")
    @NotBlank(message = "게시글 ID는 필수입니다")
    private String postId;

    @NotBlank(message = "댓글 내용은 필수입니다")
    @Size(min = 1, max = 1000, message = "댓글 내용은 1자 이상 1000자 이하여야 합니다")
    private String content;

    private String parentCommentId; // 대댓글인 경우

    // 기본 생성자
    public CommentCreateRequest() {}

    // 생성자
    public CommentCreateRequest(String postId, String content, String parentCommentId) {
        this.postId = postId;
        this.content = content;
        this.parentCommentId = parentCommentId;
    }

    // Getter/Setter
    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getParentCommentId() { return parentCommentId; }
    public void setParentCommentId(String parentCommentId) { this.parentCommentId = parentCommentId; }
}
