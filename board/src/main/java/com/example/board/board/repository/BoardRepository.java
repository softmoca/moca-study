package com.example.board.board.repository;

import com.example.board.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT b FROM Board b WHERE b.active = true ORDER BY b.createdAt DESC")
    List<Board> findAllActive();

    boolean existsByName(String name);
}