package com.example.board.board.infrastructure.repository;

import com.example.board.board.domain.model.Board;
import com.example.board.board.domain.model.BoardId;
import com.example.board.board.domain.repository.BoardRepository;
import com.example.board.board.infrastructure.entity.BoardEntity;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class BoardRepositoryImpl implements BoardRepository {

    private final BoardJpaRepository jpaRepository;

    public BoardRepositoryImpl(BoardJpaRepository jpaRepository) {
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
        // BoardId(UUID)로 조회 - publicId 컬럼에서 찾음
        return jpaRepository.findByPublicId(boardId.getValue())
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
        // publicId로 삭제하려면 먼저 엔티티를 찾아서 삭제해야 함
        jpaRepository.findByPublicId(boardId.getValue())
                .ifPresent(jpaRepository::delete);
    }
}