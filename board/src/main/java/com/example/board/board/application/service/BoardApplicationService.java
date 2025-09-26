package com.example.board.board.application.service;

import com.example.board.board.application.dto.BoardResponse;
import com.example.board.board.domain.model.Board;
import com.example.board.board.domain.model.BoardId;
import com.example.board.board.domain.repository.BoardRepository;
import com.example.board.board.domain.repository.PostRepository;
import com.example.board.common.exception.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardApplicationService {

    private final BoardRepository boardRepository;
    private final PostRepository postRepository;


    public List<BoardResponse> getAllBoards() {
        List<Board> boards = boardRepository.findAllActive();
        return boards.stream()
                .map(board -> {
                    long postCount = postRepository.countByBoardId(board.getBoardId());
                    return BoardResponse.from(board, postCount);
                })
                .collect(Collectors.toList());
    }

    public BoardResponse getBoard(String boardId) {
        Board board = boardRepository.findById(BoardId.of(boardId))
                .orElseThrow(() -> new EntityNotFoundException("Board", boardId));

        long postCount = postRepository.countByBoardId(board.getBoardId());
        return BoardResponse.from(board, postCount);
    }
}