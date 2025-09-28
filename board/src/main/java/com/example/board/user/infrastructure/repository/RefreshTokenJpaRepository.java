package com.example.board.user.infrastructure.repository;

import com.example.board.user.infrastructure.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByTokenValue(String tokenValue);

    Optional<RefreshTokenEntity> findByUserPublicId(String userPublicId);

    void deleteByTokenValue(String tokenValue);

    void deleteByUserPublicId(String userPublicId);

    @Modifying
    @Query("DELETE FROM RefreshTokenEntity r WHERE r.expiresAt < :currentTime")
    void deleteExpiredTokens(@Param("currentTime") LocalDateTime currentTime);

    boolean existsByTokenValue(String tokenValue);

    boolean existsByUserPublicId(String userPublicId);
}