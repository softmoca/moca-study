package com.example.board.board.application.usecase;

import com.example.board.board.application.dto.PostCreateRequest;
import com.example.board.board.application.dto.PostResponse;

public interface CreatePostUseCase {
    PostResponse createPost(PostCreateRequest request, String authorId);
}
