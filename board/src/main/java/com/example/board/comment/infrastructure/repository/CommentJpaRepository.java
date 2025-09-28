package com.example.board.comment.infrastructure.repository;

import com.example.board.comment.infrastructure.entity.CommentEntity;
import com.example.board.comment.infrastructure.entity.CommentEntityStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {

    // publicId로 조회하는 메서드 추가
    Optional<CommentEntity> findByPublicId(String publicId);

    // Post의 publicId로 댓글들을 찾는 메서드들
    List<CommentEntity> findByPostPublicIdOrderByCreatedAtAsc(String postPublicId);

    List<CommentEntity> findByPostPublicIdAndStatusOrderByCreatedAtAsc(String postPublicId, CommentEntityStatus status);

    // 부모 댓글의 publicId로 대댓글들을 찾는 메서드
    List<CommentEntity> findByParentCommentPublicIdOrderByCreatedAtAsc(String parentCommentPublicId);

    // Author의 publicId로 댓글들을 찾는 메서드
    List<CommentEntity> findByAuthorPublicIdOrderByCreatedAtDesc(String authorPublicId);

    // 카운트 메서드들
    long countByPostPublicId(String postPublicId);
    long countByAuthorPublicId(String authorPublicId);
}