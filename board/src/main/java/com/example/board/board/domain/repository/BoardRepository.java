package com.example.board.board.domain.repository;

import com.example.board.board.domain.model.Board;
import com.example.board.board.domain.model.BoardId;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    Board save(Board board);
    Optional<Board> findById(BoardId boardId);
    List<Board> findAllActive();
    boolean existsByName(String name);
    void deleteById(BoardId boardId);
}