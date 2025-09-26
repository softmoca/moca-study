package com.example.board.comment.application.usecase;

import com.example.board.comment.application.dto.CommentCreateRequest;
import com.example.board.comment.application.dto.CommentResponse;

public interface CreateCommentUseCase {
    CommentResponse createComment(CommentCreateRequest request, String authorId);
}
