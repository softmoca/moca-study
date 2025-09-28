package com.example.board.user.infrastructure.repository;

import com.example.board.user.domain.model.RefreshToken;
import com.example.board.user.domain.model.UserId;
import com.example.board.user.domain.repository.RefreshTokenRepository;
import com.example.board.user.infrastructure.entity.RefreshTokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository jpaRepository;

    @Override
    @Transactional
    public RefreshToken save(RefreshToken refreshToken) {
        RefreshTokenEntity entity = RefreshTokenEntity.fromDomain(refreshToken);
        RefreshTokenEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RefreshToken> findByValue(String tokenValue) {
        return jpaRepository.findByTokenValue(tokenValue)
                .map(RefreshTokenEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RefreshToken> findByUserId(UserId userId) {
        return jpaRepository.findByUserPublicId(userId.getValue())
                .map(RefreshTokenEntity::toDomain);
    }

    @Override
    @Transactional
    public void deleteByValue(String tokenValue) {
        jpaRepository.deleteByTokenValue(tokenValue);
    }

    @Override
    @Transactional
    public void deleteByUserId(UserId userId) {
        jpaRepository.deleteByUserPublicId(userId.getValue());
    }

    @Override
    @Transactional
    public void deleteExpiredTokens() {
        jpaRepository.deleteExpiredTokens(LocalDateTime.now());
    }
}