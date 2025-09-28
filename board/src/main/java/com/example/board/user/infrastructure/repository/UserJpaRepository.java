package com.example.board.user.infrastructure.repository;

import com.example.board.user.infrastructure.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    // publicId로 조회하는 메서드들 추가
    Optional<UserEntity> findByPublicId(String publicId);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsername(String username);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByPublicId(String publicId);
}