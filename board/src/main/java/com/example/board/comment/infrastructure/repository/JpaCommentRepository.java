package com.example.board.comment.infrastructure.repository;

import com.example.board.comment.domain.model.*;
import com.example.board.comment.domain.repository.CommentRepository;
import com.example.board.comment.infrastructure.entity.CommentEntity;
import com.example.board.board.domain.model.PostId;
import com.example.board.user.domain.model.UserId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

interface JpaCommentEntityRepository extends JpaRepository<CommentEntity, String> {
    List<CommentEntity> findByPostIdOrderByCreatedAtAsc(String postId);
    List<CommentEntity> findByPostIdAndStatusOrderByCreatedAtAsc(String postId, CommentEntity.CommentStatus status);
    List<CommentEntity> findByParentCommentIdOrderByCreatedAtAsc(String parentCommentId);
    List<CommentEntity> findByAuthorIdOrderByCreatedAtDesc(String authorId);
    long countByPostId(String postId);
    long countByAuthorId(String authorId);
}

@Repository
public class JpaCommentRepository implements CommentRepository {

    private final JpaCommentEntityRepository jpaRepository;

    public JpaCommentRepository(JpaCommentEntityRepository jpaRepository) {
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
        return jpaRepository.findById(commentId.getValue())
                .map(CommentEntity::toDomain);
    }

    @Override
    public List<Comment> findByPostId(PostId postId) {
        return jpaRepository.findByPostIdOrderByCreatedAtAsc(postId.getValue()).stream()
                .map(CommentEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Comment> findByPostIdAndStatus(PostId postId, CommentStatus status) {
        CommentEntity.CommentStatus entityStatus = CommentEntity.CommentStatus.valueOf(status.name());
        return jpaRepository.findByPostIdAndStatusOrderByCreatedAtAsc(postId.getValue(), entityStatus).stream()
                .map(CommentEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Comment> findByParentCommentId(CommentId parentCommentId) {
        return jpaRepository.findByParentCommentIdOrderByCreatedAtAsc(parentCommentId.getValue()).stream()
                .map(CommentEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Comment> findByAuthorId(UserId authorId) {
        return jpaRepository.findByAuthorIdOrderByCreatedAtDesc(authorId.getValue()).stream()
                .map(CommentEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long countByPostId(PostId postId) {
        return jpaRepository.countByPostId(postId.getValue());
    }

    @Override
    public long countByAuthorId(UserId authorId) {
        return jpaRepository.countByAuthorId(authorId.getValue());
    }

    @Override
    public void deleteById(CommentId commentId) {
        jpaRepository.deleteById(commentId.getValue());
    }
}