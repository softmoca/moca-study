package com.example.board.board.application.usecase;

import com.example.board.board.application.dto.BoardCreateRequest;
import com.example.board.board.application.dto.BoardResponse;

public interface CreateBoardUseCase {
    BoardResponse createBoard(BoardCreateRequest request, String createdBy);
}