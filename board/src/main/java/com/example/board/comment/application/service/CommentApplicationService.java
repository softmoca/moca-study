package com.example.board.comment.application.service;

import com.example.board.comment.application.dto.*;
import com.example.board.comment.application.usecase.*;
import com.example.board.comment.domain.Comment;
import com.example.board.comment.domain.CommentStatus;
import com.example.board.comment.repository.CommentRepository;
import com.example.board.comment.service.CommentDomainService;
import com.example.board.user.domain.User;
import com.example.board.user.repository.UserRepository;
import com.example.board.common.exception.BusinessException;
import com.example.board.common.exception.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentApplicationService implements CreateCommentUseCase, UpdateCommentUseCase {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentDomainService commentDomainService;

    @Override
    public CommentResponse createComment(CommentCreateRequest request, String authorId) {
        try {
            // ID 변환
            Long postId = Long.parseLong(request.getPostId());
            Long authorIdLong = Long.parseLong(authorId);

            // 도메인 서비스를 통한 유효성 검증
            commentDomainService.validateCommentCreation(postId, authorIdLong);

            Comment comment;
            if (request.getParentCommentId() != null) {
                // 대댓글 생성
                Long parentCommentId = Long.parseLong(request.getParentCommentId());
                commentDomainService.validateReplyCreation(parentCommentId, postId);
                comment = new Comment(postId, authorIdLong, request.getContent(), parentCommentId);
            } else {
                // 최상위 댓글 생성
                comment = new Comment(postId, authorIdLong, request.getContent());
            }

            // 저장
            Comment savedComment = commentRepository.save(comment);

            // 작성자 정보 조회
            User author = userRepository.findById(authorIdLong)
                    .orElseThrow(() -> new EntityNotFoundException("User", authorId));

            return CommentResponse.from(savedComment, author.getUsername(), List.of());

        } catch (NumberFormatException e) {
            throw new BusinessException("잘못된 ID 형식입니다");
        } catch (IllegalArgumentException e) {
            throw new BusinessException("댓글 생성 실패: " + e.getMessage());
        }
    }

    @Override
    public CommentResponse updateComment(String commentId, CommentUpdateRequest request, String userId) {
        try {
            // ID 변환
            Long commentIdLong = Long.parseLong(commentId);
            Long userIdLong = Long.parseLong(userId);

            // 댓글 조회
            Comment comment = commentRepository.findById(commentIdLong)
                    .orElseThrow(() -> new EntityNotFoundException("Comment", commentId));

            // 사용자 조회
            User user = userRepository.findById(userIdLong)
                    .orElseThrow(() -> new EntityNotFoundException("User", userId));

            // 도메인 서비스를 통한 수정 권한 검증
            commentDomainService.validateCommentEdit(comment, userIdLong, user.isAdmin());

            // 댓글 수정
            comment.updateContent(request.getContent());

            // 저장
            Comment savedComment = commentRepository.save(comment);

            return CommentResponse.from(savedComment, user.getUsername(), List.of());

        } catch (NumberFormatException e) {
            throw new BusinessException("잘못된 ID 형식입니다");
        } catch (IllegalArgumentException e) {
            throw new BusinessException("댓글 수정 실패: " + e.getMessage());
        }
    }

    public void deleteComment(String commentId, String userId) {
        try {
            // ID 변환
            Long commentIdLong = Long.parseLong(commentId);
            Long userIdLong = Long.parseLong(userId);

            // 댓글 조회
            Comment comment = commentRepository.findById(commentIdLong)
                    .orElseThrow(() -> new EntityNotFoundException("Comment", commentId));

            // 사용자 조회
            User user = userRepository.findById(userIdLong)
                    .orElseThrow(() -> new EntityNotFoundException("User", userId));

            // 도메인 서비스를 통한 삭제 권한 검증
            commentDomainService.validateCommentDeletion(comment, userIdLong, user.isAdmin());

            // 댓글 삭제 (소프트 삭제)
            comment.delete();
            commentRepository.save(comment);

        } catch (NumberFormatException e) {
            throw new BusinessException("잘못된 ID 형식입니다");
        }
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPost(String postId) {
        try {
            Long postIdLong = Long.parseLong(postId);

            List<Comment> comments = commentRepository.findByPostIdAndStatusOrderByCreatedAtAsc(
                    postIdLong, CommentStatus.ACTIVE);

            // 최상위 댓글과 대댓글 분리
            Map<Boolean, List<Comment>> commentsByLevel = comments.stream()
                    .collect(Collectors.groupingBy(Comment::isTopLevel));

            List<Comment> topLevelComments = commentsByLevel.getOrDefault(true, List.of());
            List<Comment> replies = commentsByLevel.getOrDefault(false, List.of());

            // 대댓글을 부모 댓글 ID별로 그룹화
            Map<Long, List<Comment>> repliesByParent = replies.stream()
                    .collect(Collectors.groupingBy(Comment::getParentCommentId));

            // 최상위 댓글과 해당 대댓글들을 함께 반환
            return topLevelComments.stream()
                    .map(comment -> {
                        String authorName = getAuthorName(comment.getAuthorId());
                        List<CommentResponse> commentReplies = repliesByParent
                                .getOrDefault(comment.getId(), List.of())
                                .stream()
                                .map(reply -> {
                                    String replyAuthorName = getAuthorName(reply.getAuthorId());
                                    return CommentResponse.from(reply, replyAuthorName, List.of());
                                })
                                .collect(Collectors.toList());

                        return CommentResponse.from(comment, authorName, commentReplies);
                    })
                    .collect(Collectors.toList());

        } catch (NumberFormatException e) {
            throw new BusinessException("잘못된 게시글 ID 형식입니다");
        }
    }

    private String getAuthorName(Long authorId) {
        return userRepository.findById(authorId)
                .map(User::getUsername)
                .orElse("Unknown");
    }
}