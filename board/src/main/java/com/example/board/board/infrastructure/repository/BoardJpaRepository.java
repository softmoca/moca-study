package com.example.board.board.infrastructure.repository;

import com.example.board.board.infrastructure.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

interface BoardJpaRepository extends JpaRepository<BoardEntity, String> {
    // publicId로 조회하는 메서드들 추가
    Optional<BoardEntity> findByPublicId(String publicId);

    @Query("SELECT b FROM BoardEntity b WHERE b.active = true ORDER BY b.createdAt DESC")
    List<BoardEntity> findAllActive();
    boolean existsByName(String name);
}