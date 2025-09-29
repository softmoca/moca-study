package com.example.board.comment.repository;

import com.example.board.comment.domain.Comment;
import com.example.board.comment.domain.CommentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 게시글별 댓글 조회
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);

    List<Comment> findByPostIdAndStatusOrderByCreatedAtAsc(Long postId, CommentStatus status);

    // 부모 댓글별 대댓글 조회
    List<Comment> findByParentCommentIdOrderByCreatedAtAsc(Long parentCommentId);

    // 작성자별 댓글 조회
    List<Comment> findByAuthorIdOrderByCreatedAtDesc(Long authorId);

    // 카운트
    long countByPostId(Long postId);

    long countByAuthorId(Long authorId);
}