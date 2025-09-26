package com.example.board.comment.domain.service;

import com.example.board.comment.domain.model.Comment;
import com.example.board.comment.domain.model.CommentId;
import com.example.board.comment.domain.repository.CommentRepository;
import com.example.board.board.domain.model.Post;
import com.example.board.board.domain.model.PostId;
import com.example.board.board.domain.repository.PostRepository;
import com.example.board.user.domain.model.UserId;
import com.example.board.common.exception.BusinessException;
import com.example.board.common.exception.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentDomainService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;


    public void validateCommentCreation(PostId postId, UserId authorId) {
        // 게시글 존재 여부 및 댓글 작성 가능 상태 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post", postId.getValue()));

        if (!post.canView()) {
            throw new BusinessException("Cannot comment on this post");
        }

        if (post.isDeleted()) {
            throw new BusinessException("Cannot comment on deleted post");
        }
    }

    public void validateReplyCreation(CommentId parentCommentId, PostId postId) {
        // 부모 댓글 존재 여부 확인
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment", parentCommentId.getValue()));

        // 같은 게시글의 댓글인지 확인
        if (!parentComment.getPostId().equals(postId)) {
            throw new BusinessException("Parent comment does not belong to the same post");
        }

        // 삭제된 댓글에는 대댓글 작성 불가
        if (parentComment.isDeleted()) {
            throw new BusinessException("Cannot reply to deleted comment");
        }

        // 대댓글의 대댓글은 불가 (2depth만 허용)
        if (parentComment.isReply()) {
            throw new BusinessException("Cannot reply to a reply comment");
        }
    }

    public void validateCommentEdit(Comment comment, UserId userId, boolean isAdmin) {
        // 삭제된 댓글 확인
        if (comment.isDeleted()) {
            throw new BusinessException("Cannot edit deleted comment");
        }

        // 작성자 또는 관리자만 수정 가능
        if (!comment.isAuthor(userId) && !isAdmin) {
            throw new BusinessException("Only author or admin can edit this comment");
        }

        // 수정 가능한 상태인지 확인
        if (!comment.canEdit()) {
            throw new BusinessException("Comment cannot be edited in current status");
        }
    }

    public void validateCommentDeletion(Comment comment, UserId userId, boolean isAdmin) {
        // 이미 삭제된 댓글 확인
        if (comment.isDeleted()) {
            throw new BusinessException("Comment is already deleted");
        }

        // 작성자 또는 관리자만 삭제 가능
        if (!comment.isAuthor(userId) && !isAdmin) {
            throw new BusinessException("Only author or admin can delete this comment");
        }
    }
}
