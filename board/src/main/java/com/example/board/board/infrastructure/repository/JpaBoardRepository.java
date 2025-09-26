package com.example.board.board.infrastructure.repository;

import com.example.board.board.domain.model.Board;
import com.example.board.board.domain.model.BoardId;
import com.example.board.board.domain.repository.BoardRepository;
import com.example.board.board.infrastructure.entity.BoardEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

interface JpaBoardEntityRepository extends JpaRepository<BoardEntity, String> {
    @Query("SELECT b FROM BoardEntity b WHERE b.active = true ORDER BY b.createdAt DESC")
    List<BoardEntity> findAllActive();
    boolean existsByName(String name);
}

@Repository
public class JpaBoardRepository implements BoardRepository {

    private final JpaBoardEntityRepository jpaRepository;

    public JpaBoardRepository(JpaBoardEntityRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Board save(Board board) {
        BoardEntity entity = BoardEntity.fromDomain(board);
        BoardEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Board> findById(BoardId boardId) {
        return jpaRepository.findById(boardId.getValue())
                .map(BoardEntity::toDomain);
    }

    @Override
    public List<Board> findAllActive() {
        return jpaRepository.findAllActive().stream()
                .map(BoardEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public void deleteById(BoardId boardId) {
        jpaRepository.deleteById(boardId.getValue());
    }
}