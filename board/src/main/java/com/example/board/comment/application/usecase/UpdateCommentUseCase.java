package com.example.board.comment.application.usecase;

import com.example.board.comment.application.dto.CommentUpdateRequest;
import com.example.board.comment.application.dto.CommentResponse;

public interface UpdateCommentUseCase {
    CommentResponse updateComment(String commentId, CommentUpdateRequest request, String userId);
}