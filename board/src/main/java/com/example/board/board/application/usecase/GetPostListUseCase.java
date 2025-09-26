package com.example.board.board.application.usecase;

import com.example.board.board.application.dto.PostListResponse;

import java.util.List;

public interface GetPostListUseCase {
    List<PostListResponse> getPostsByBoard(String boardId, int page, int size);
    List<PostListResponse> getRecentPosts(int limit);
    List<PostListResponse> getPopularPosts(int limit);
}