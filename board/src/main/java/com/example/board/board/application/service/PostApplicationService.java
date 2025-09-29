package com.example.board.board.application.service;

import com.example.board.board.application.dto.*;
import com.example.board.board.application.usecase.*;
import com.example.board.board.domain.Post;
import com.example.board.board.domain.PostStatus;
import com.example.board.board.repository.PostRepository;
import com.example.board.board.service.PostDomainService;
import com.example.board.comment.repository.CommentRepository;
import com.example.board.user.domain.User;
import com.example.board.user.repository.UserRepository;
import com.example.board.common.exception.BusinessException;
import com.example.board.common.exception.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostApplicationService implements CreatePostUseCase, UpdatePostUseCase, GetPostListUseCase {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostDomainService postDomainService;
    private final CommentRepository commentRepository;  // 추가!

    @Override
    public PostResponse createPost(PostCreateRequest request, String authorId) {
        try {
            // ID 변환
            Long boardId = Long.parseLong(request.getBoardId());
            Long authorIdLong = Long.parseLong(authorId);

            // 도메인 서비스를 통한 유효성 검증
            postDomainService.validatePostCreation(boardId, authorIdLong);

            // 게시글 생성
            Post post = new Post(boardId, request.getTitle(), request.getContent(), authorIdLong);

            // 저장
            Post savedPost = postRepository.save(post);

            // 작성자 정보 조회
            User author = userRepository.findById(authorIdLong)
                    .orElseThrow(() -> new EntityNotFoundException("User", authorId));

            return PostResponse.from(savedPost, author.getUsername());

        } catch (NumberFormatException e) {
            throw new BusinessException("잘못된 ID 형식입니다");
        } catch (IllegalArgumentException e) {
            throw new BusinessException("게시글 생성 실패: " + e.getMessage());
        }
    }

    @Override
    public PostResponse updatePost(String postId, PostUpdateRequest request, String userId) {
        try {
            // ID 변환
            Long postIdLong = Long.parseLong(postId);
            Long userIdLong = Long.parseLong(userId);

            // 게시글 조회
            Post post = postRepository.findById(postIdLong)
                    .orElseThrow(() -> new EntityNotFoundException("Post", postId));

            // 사용자 조회
            User user = userRepository.findById(userIdLong)
                    .orElseThrow(() -> new EntityNotFoundException("User", userId));

            // 도메인 서비스를 통한 수정 권한 검증
            postDomainService.validatePostEdit(post, userIdLong, user.isAdmin());

            // 게시글 수정
            post.updateTitle(request.getTitle());
            post.updateContent(request.getContent());

            // 저장
            Post savedPost = postRepository.save(post);

            return PostResponse.from(savedPost, user.getUsername());

        } catch (NumberFormatException e) {
            throw new BusinessException("잘못된 ID 형식입니다");
        } catch (IllegalArgumentException e) {
            throw new BusinessException("게시글 수정 실패: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(String postId, String viewerId) {
        try {
            // ID 변환
            Long postIdLong = Long.parseLong(postId);
            Long viewerIdLong = viewerId != null ? Long.parseLong(viewerId) : null;

            // 게시글 조회
            Post post = postRepository.findById(postIdLong)
                    .orElseThrow(() -> new EntityNotFoundException("Post", postId));

            // 조회자 정보
            User viewer = viewerIdLong != null ?
                    userRepository.findById(viewerIdLong).orElse(null) : null;

            // 조회 권한 확인
            boolean canView = postDomainService.canViewPost(post, viewerIdLong,
                    viewer != null && viewer.isAdmin());

            if (!canView) {
                throw new BusinessException("게시글 조회 권한이 없습니다");
            }

            // 조회수 증가 (작성자가 아닌 경우만)
            boolean shouldIncreaseViewCount = (viewerIdLong == null || !post.isAuthor(viewerIdLong));
            if (shouldIncreaseViewCount) {
                increaseViewCountAsync(postIdLong);
            }

            // 작성자 정보 조회
            User author = userRepository.findById(post.getAuthorId())
                    .orElseThrow(() -> new EntityNotFoundException("User", post.getAuthorId().toString()));

            return PostResponse.from(post, author.getUsername());

        } catch (NumberFormatException e) {
            throw new BusinessException("잘못된 ID 형식입니다");
        }
    }

    /**
     * 조회수 증가 (별도 트랜잭션)
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void increaseViewCountAsync(Long postId) {
        try {
            int updatedRows = postRepository.incrementViewCount(postId);
            if (updatedRows == 0) {
                System.err.println("No post found to update view count for postId: " + postId);
            }
        } catch (Exception e) {
            System.err.println("Failed to increase view count for post: " + postId + ", error: " + e.getMessage());
        }
    }

    public void deletePost(String postId, String userId) {
        try {
            // ID 변환
            Long postIdLong = Long.parseLong(postId);
            Long userIdLong = Long.parseLong(userId);

            // 게시글 조회
            Post post = postRepository.findById(postIdLong)
                    .orElseThrow(() -> new EntityNotFoundException("Post", postId));

            // 사용자 조회
            User user = userRepository.findById(userIdLong)
                    .orElseThrow(() -> new EntityNotFoundException("User", userId));

            // 도메인 서비스를 통한 삭제 권한 검증
            postDomainService.validatePostDeletion(post, userIdLong, user.isAdmin());

            // 게시글 삭제 (소프트 삭제)
            post.delete();
            postRepository.save(post);

        } catch (NumberFormatException e) {
            throw new BusinessException("잘못된 ID 형식입니다");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostListResponse> getPostsByBoard(String boardId, int page, int size) {
        try {
            Long boardIdLong = Long.parseLong(boardId);

            List<Post> posts = postRepository.findByBoardIdAndStatusOrderByCreatedAtDesc(
                    boardIdLong, PostStatus.PUBLISHED);

            return posts.stream()
                    .skip((long) page * size)
                    .limit(size)
                    .map(this::toPostListResponse)
                    .collect(Collectors.toList());

        } catch (NumberFormatException e) {
            throw new BusinessException("잘못된 게시판 ID 형식입니다");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostListResponse> getRecentPosts(int limit) {
        PageRequest pageRequest = PageRequest.of(0, limit);
        List<Post> posts = postRepository.findRecentPublishedPosts(pageRequest);

        return posts.stream()
                .map(this::toPostListResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostListResponse> getPopularPosts(int limit) {
        PageRequest pageRequest = PageRequest.of(0, limit);
        List<Post> posts = postRepository.findPopularPublishedPosts(pageRequest);

        return posts.stream()
                .map(this::toPostListResponse)
                .collect(Collectors.toList());
    }



    private PostListResponse toPostListResponse(Post post) {
        // 작성자 정보 조회
        User author = userRepository.findById(post.getAuthorId())
                .orElse(null);
        String authorName = author != null ? author.getUsername() : "Unknown";

        // 댓글 수 조회 - 이제 작동!
        int commentCount = (int) commentRepository.countByPostId(post.getId());

        return new PostListResponse(
                post.getId(),
                post.getTitle(),
                authorName,
                post.getViewCount(),
                commentCount,
                post.getCreatedAt()
        );
    }

}