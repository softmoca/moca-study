// PostController.java - 게시글 컨트롤러
package com.example.board.board.presentation.controller;

import com.example.board.board.application.dto.*;
import com.example.board.board.application.service.PostApplicationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostApplicationService postApplicationService;

    public PostController(PostApplicationService postApplicationService) {
        this.postApplicationService = postApplicationService;
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @Valid @RequestBody PostCreateRequest request,
            @RequestHeader("X-User-Id") String userId) { // 실제로는 JWT에서 추출
        PostResponse response = postApplicationService.createPost(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(
            @PathVariable String postId,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        PostResponse response = postApplicationService.getPost(postId, userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable String postId,
            @Valid @RequestBody PostUpdateRequest request,
            @RequestHeader("X-User-Id") String userId) {
        PostResponse response = postApplicationService.updatePost(postId, request, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable String postId,
            @RequestHeader("X-User-Id") String userId) {
        postApplicationService.deletePost(postId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PostListResponse>> getPostsByBoard(
            @RequestParam String boardId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<PostListResponse> responses = postApplicationService.getPostsByBoard(boardId, page, size);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<PostListResponse>> getRecentPosts(
            @RequestParam(defaultValue = "10") int limit) {
        List<PostListResponse> responses = postApplicationService.getRecentPosts(limit);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<PostListResponse>> getPopularPosts(
            @RequestParam(defaultValue = "10") int limit) {
        List<PostListResponse> responses = postApplicationService.getPopularPosts(limit);
        return ResponseEntity.ok(responses);
    }
}