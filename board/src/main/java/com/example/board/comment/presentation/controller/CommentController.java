package com.example.board.comment.presentation.controller;

import com.example.board.comment.application.dto.CommentCreateRequest;
import com.example.board.comment.application.dto.CommentUpdateRequest;
import com.example.board.comment.application.dto.CommentResponse;
import com.example.board.comment.application.service.CommentApplicationService;
import com.example.board.comment.presentation.api.CommentApi;
import com.example.board.common.annotation.CurrentUser;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController implements CommentApi {

    private final CommentApplicationService commentApplicationService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @Valid @RequestBody CommentCreateRequest request,
            @CurrentUser String userId) {
        CommentResponse response = commentApplicationService.createComment(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequest request,
            @CurrentUser String userId) {
        CommentResponse response = commentApplicationService.updateComment(
                commentId.toString(), request, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @CurrentUser String userId) {
        commentApplicationService.deleteComment(commentId.toString(), userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByPost(@PathVariable Long postId) {
        List<CommentResponse> responses = commentApplicationService.getCommentsByPost(postId.toString());
        return ResponseEntity.ok(responses);
    }
}