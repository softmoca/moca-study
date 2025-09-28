package com.example.board.board.presentation.controller;

import com.example.board.board.application.dto.BoardCreateRequest;
import com.example.board.board.application.dto.BoardResponse;
import com.example.board.board.application.service.BoardApplicationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardApplicationService boardApplicationService;

    public BoardController(BoardApplicationService boardApplicationService) {
        this.boardApplicationService = boardApplicationService;
    }

    @PostMapping
    public ResponseEntity<BoardResponse> createBoard(
            @Valid @RequestBody BoardCreateRequest request,
            @RequestHeader("X-User-Id") String userId) {
        BoardResponse response = boardApplicationService.createBoard(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BoardResponse>> getAllBoards() {
        List<BoardResponse> responses = boardApplicationService.getAllBoards();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponse> getBoard(@PathVariable String boardId) {
        BoardResponse response = boardApplicationService.getBoard(boardId);
        return ResponseEntity.ok(response);
    }
}