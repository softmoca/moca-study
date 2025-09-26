package com.example.board.comment.domain.repository;

import com.example.board.comment.domain.model.Comment;
import com.example.board.comment.domain.model.CommentId;
import com.example.board.comment.domain.model.CommentStatus;
import com.example.board.board.domain.model.PostId;
import com.example.board.user.domain.model.UserId;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);
    Optional<Comment> findById(CommentId commentId);
    List<Comment> findByPostId(PostId postId);
    List<Comment> findByPostIdAndStatus(PostId postId, CommentStatus status);
    List<Comment> findByParentCommentId(CommentId parentCommentId);
    List<Comment> findByAuthorId(UserId authorId);
    long countByPostId(PostId postId);
    long countByAuthorId(UserId authorId);
    void deleteById(CommentId commentId);
}
