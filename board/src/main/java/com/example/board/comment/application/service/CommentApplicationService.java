package com.example.board.comment.application.service;

import com.example.board.comment.application.dto.*;
import com.example.board.comment.application.usecase.*;
import com.example.board.comment.domain.model.*;
import com.example.board.comment.domain.repository.CommentRepository;
import com.example.board.comment.domain.service.CommentDomainService;
import com.example.board.board.domain.model.PostId;
import com.example.board.user.domain.model.User;
import com.example.board.user.domain.model.UserId;
import com.example.board.user.domain.repository.UserRepository;
import com.example.board.common.exception.BusinessException;
import com.example.board.common.exception.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentApplicationService implements CreateCommentUseCase, UpdateCommentUseCase {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentDomainService commentDomainService;

    public CommentApplicationService(CommentRepository commentRepository,
                                     UserRepository userRepository,
                                     CommentDomainService commentDomainService) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.commentDomainService = commentDomainService;
    }

    @Override
    public CommentResponse createComment(CommentCreateRequest request, String authorId) {
        try {
            // 도메인 객체로 변환
            PostId postId = PostId.of(request.getPostId());
            UserId userIdObj = UserId.of(authorId);
            CommentContent content = CommentContent.of(request.getContent());

            // 도메인 서비스를 통한 유효성 검증
            commentDomainService.validateCommentCreation(postId, userIdObj);

            Comment comment;
            if (request.getParentCommentId() != null) {
                // 대댓글 생성
                CommentId parentCommentId = CommentId.of(request.getParentCommentId());
                commentDomainService.validateReplyCreation(parentCommentId, postId);
                comment = new Comment(postId, userIdObj, content, parentCommentId);
            } else {
                // 최상위 댓글 생성
                comment = new Comment(postId, userIdObj, content);
            }

            // 저장
            Comment savedComment = commentRepository.save(comment);

            // 작성자 정보 조회
            User author = userRepository.findById(userIdObj)
                    .orElseThrow(() -> new EntityNotFoundException("User", authorId));

            return CommentResponse.from(savedComment, author.getUsername(), List.of());

        } catch (IllegalArgumentException e) {
            throw new BusinessException("댓글 생성 실패: " + e.getMessage());
        }
    }

    @Override
    public CommentResponse updateComment(String commentId, CommentUpdateRequest request, String userId) {
        try {
            // 댓글 조회
            Comment comment = commentRepository.findById(CommentId.of(commentId))
                    .orElseThrow(() -> new EntityNotFoundException("Comment", commentId));

            // 사용자 조회
            UserId userIdObj = UserId.of(userId);
            User user = userRepository.findById(userIdObj)
                    .orElseThrow(() -> new EntityNotFoundException("User", userId));

            // 도메인 서비스를 통한 수정 권한 검증
            commentDomainService.validateCommentEdit(comment, userIdObj, user.isAdmin());

            // 댓글 수정
            comment.updateContent(CommentContent.of(request.getContent()));

            // 저장
            Comment savedComment = commentRepository.save(comment);

            return CommentResponse.from(savedComment, user.getUsername(), List.of());

        } catch (IllegalArgumentException e) {
            throw new BusinessException("댓글 수정 실패: " + e.getMessage());
        }
    }

    public void deleteComment(String commentId, String userId) {
        // 댓글 조회
        Comment comment = commentRepository.findById(CommentId.of(commentId))
                .orElseThrow(() -> new EntityNotFoundException("Comment", commentId));

        // 사용자 조회
        UserId userIdObj = UserId.of(userId);
        User user = userRepository.findById(userIdObj)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));

        // 도메인 서비스를 통한 삭제 권한 검증
        commentDomainService.validateCommentDeletion(comment, userIdObj, user.isAdmin());

        // 댓글 삭제 (소프트 삭제)
        comment.delete();
        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPost(String postId) {
        PostId postIdObj = PostId.of(postId);
        List<Comment> comments = commentRepository.findByPostIdAndStatus(postIdObj, CommentStatus.ACTIVE);

        // 최상위 댓글과 대댓글 분리
        Map<Boolean, List<Comment>> commentsByLevel = comments.stream()
                .collect(Collectors.groupingBy(Comment::isTopLevel));

        List<Comment> topLevelComments = commentsByLevel.getOrDefault(true, List.of());
        List<Comment> replies = commentsByLevel.getOrDefault(false, List.of());

        // 대댓글을 부모 댓글 ID별로 그룹화
        Map<String, List<Comment>> repliesByParent = replies.stream()
                .collect(Collectors.groupingBy(comment -> comment.getParentCommentId().getValue()));

        // 최상위 댓글과 해당 대댓글들을 함께 반환
        return topLevelComments.stream()
                .map(comment -> {
                    String authorName = getAuthorName(comment.getAuthorId());
                    List<CommentResponse> commentReplies = repliesByParent
                            .getOrDefault(comment.getCommentId().getValue(), List.of())
                            .stream()
                            .map(reply -> {
                                String replyAuthorName = getAuthorName(reply.getAuthorId());
                                return CommentResponse.from(reply, replyAuthorName, List.of());
                            })
                            .collect(Collectors.toList());

                    return CommentResponse.from(comment, authorName, commentReplies);
                })
                .collect(Collectors.toList());
    }

    private String getAuthorName(UserId authorId) {
        return userRepository.findById(authorId)
                .map(User::getUsername)
                .orElse("Unknown");
    }
}