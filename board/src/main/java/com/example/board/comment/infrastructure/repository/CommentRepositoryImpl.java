package com.example.board.comment.infrastructure.repository;

import com.example.board.comment.domain.model.*;
import com.example.board.comment.domain.repository.CommentRepository;
import com.example.board.comment.infrastructure.entity.CommentEntity;
import com.example.board.comment.infrastructure.entity.CommentEntityStatus;
import com.example.board.board.domain.model.PostId;
import com.example.board.user.domain.model.UserId;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentJpaRepository jpaRepository;

    public CommentRepositoryImpl(CommentJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Comment save(Comment comment) {
        CommentEntity entity = CommentEntity.fromDomain(comment);
        CommentEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Comment> findById(CommentId commentId) {
        // CommentId(UUID)로 조회 - publicId 컬럼에서 찾음
        return jpaRepository.findByPublicId(commentId.getValue())
                .map(CommentEntity::toDomain);
    }

    @Override
    public List<Comment> findByPostId(PostId postId) {
        // Post의 publicId로 댓글들을 찾음
        return jpaRepository.findByPostPublicIdOrderByCreatedAtAsc(postId.getValue()).stream()
                .map(CommentEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Comment> findByPostIdAndStatus(PostId postId, CommentStatus status) {
        CommentEntityStatus entityStatus = CommentEntityStatus.valueOf(status.name());
        return jpaRepository.findByPostPublicIdAndStatusOrderByCreatedAtAsc(postId.getValue(), entityStatus).stream()
                .map(CommentEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Comment> findByParentCommentId(CommentId parentCommentId) {
        // 부모 댓글의 publicId로 대댓글들을 찾음
        return jpaRepository.findByParentCommentPublicIdOrderByCreatedAtAsc(parentCommentId.getValue()).stream()
                .map(CommentEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Comment> findByAuthorId(UserId authorId) {
        // Author의 publicId로 댓글들을 찾음
        return jpaRepository.findByAuthorPublicIdOrderByCreatedAtDesc(authorId.getValue()).stream()
                .map(CommentEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long countByPostId(PostId postId) {
        return jpaRepository.countByPostPublicId(postId.getValue());
    }

    @Override
    public long countByAuthorId(UserId authorId) {
        return jpaRepository.countByAuthorPublicId(authorId.getValue());
    }

    @Override
    public void deleteById(CommentId commentId) {
        // publicId로 삭제하려면 먼저 엔티티를 찾아서 삭제해야 함
        jpaRepository.findByPublicId(commentId.getValue())
                .ifPresent(jpaRepository::delete);
    }
}