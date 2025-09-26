package com.example.board.comment.infrastructure.repository;

import com.example.board.comment.infrastructure.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface CommentJpaRepository extends JpaRepository<CommentEntity, String> {
    List<CommentEntity> findByPostIdOrderByCreatedAtAsc(String postId);

    List<CommentEntity> findByPostIdAndStatusOrderByCreatedAtAsc(String postId, CommentEntity.CommentStatus status);

    List<CommentEntity> findByParentCommentIdOrderByCreatedAtAsc(String parentCommentId);

    List<CommentEntity> findByAuthorIdOrderByCreatedAtDesc(String authorId);

    long countByPostId(String postId);

    long countByAuthorId(String authorId);
}
