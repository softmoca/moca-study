package com.example.board.board.application.usecase;

import com.example.board.board.application.dto.PostUpdateRequest;
import com.example.board.board.application.dto.PostResponse;

public interface UpdatePostUseCase {
    PostResponse updatePost(String postId, PostUpdateRequest request, String userId);
}