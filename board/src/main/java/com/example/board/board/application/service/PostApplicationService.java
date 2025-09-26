package com.example.board.board.application.service;

import com.example.board.board.application.dto.*;
import com.example.board.board.application.usecase.*;
import com.example.board.board.domain.model.*;
import com.example.board.board.domain.repository.PostRepository;
import com.example.board.board.domain.service.PostDomainService;
import com.example.board.user.domain.model.User;
import com.example.board.user.domain.model.UserId;
import com.example.board.user.domain.repository.UserRepository;
import com.example.board.common.exception.BusinessException;
import com.example.board.common.exception.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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


    @Override
    public PostResponse createPost(PostCreateRequest request, String authorId) {
        try {
            // 도메인 객체로 변환
            BoardId boardId = BoardId.of(request.getBoardId());
            UserId userId = UserId.of(authorId);
            Title title = Title.of(request.getTitle());
            Content content = Content.of(request.getContent());

            // 도메인 서비스를 통한 유효성 검증
            postDomainService.validatePostCreation(boardId, userId);

            // 게시글 생성
            Post post = new Post(boardId, title, content, userId);

            // 저장
            Post savedPost = postRepository.save(post);

            // 작성자 정보 조회
            User author = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User", authorId));

            return PostResponse.from(savedPost, author.getUsername());

        } catch (IllegalArgumentException e) {
            throw new BusinessException("게시글 생성 실패: " + e.getMessage());
        }
    }

    @Override
    public PostResponse updatePost(String postId, PostUpdateRequest request, String userId) {
        try {
            // 게시글 조회
            Post post = postRepository.findById(PostId.of(postId))
                    .orElseThrow(() -> new EntityNotFoundException("Post", postId));

            // 사용자 조회
            UserId userIdObj = UserId.of(userId);
            User user = userRepository.findById(userIdObj)
                    .orElseThrow(() -> new EntityNotFoundException("User", userId));

            // 도메인 서비스를 통한 수정 권한 검증
            postDomainService.validatePostEdit(post, userIdObj, user.isAdmin());

            // 게시글 수정
            post.updateTitle(Title.of(request.getTitle()));
            post.updateContent(Content.of(request.getContent()));

            // 저장
            Post savedPost = postRepository.save(post);

            return PostResponse.from(savedPost, user.getUsername());

        } catch (IllegalArgumentException e) {
            throw new BusinessException("게시글 수정 실패: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(String postId, String viewerId) {
        // 게시글 조회
        Post post = postRepository.findById(PostId.of(postId))
                .orElseThrow(() -> new EntityNotFoundException("Post", postId));

        // 조회자 정보
        UserId viewerIdObj = viewerId != null ? UserId.of(viewerId) : null;
        User viewer = viewerIdObj != null ?
                userRepository.findById(viewerIdObj).orElse(null) : null;

        // 조회 권한 확인
        boolean canView = postDomainService.canViewPost(post, viewerIdObj,
                viewer != null && viewer.isAdmin());

        if (!canView) {
            throw new BusinessException("게시글 조회 권한이 없습니다");
        }

        // 조회수 증가 (작성자가 아닌 경우만)
        if (viewerIdObj == null || !post.isAuthor(viewerIdObj)) {
            post.increaseViewCount();
            postRepository.save(post);
        }

        // 작성자 정보 조회
        User author = userRepository.findById(post.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("User", post.getAuthorId().getValue()));

        return PostResponse.from(post, author.getUsername());
    }

    public void deletePost(String postId, String userId) {
        // 게시글 조회
        Post post = postRepository.findById(PostId.of(postId))
                .orElseThrow(() -> new EntityNotFoundException("Post", postId));

        // 사용자 조회
        UserId userIdObj = UserId.of(userId);
        User user = userRepository.findById(userIdObj)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));

        // 도메인 서비스를 통한 삭제 권한 검증
        postDomainService.validatePostDeletion(post, userIdObj, user.isAdmin());

        // 게시글 삭제 (소프트 삭제)
        post.delete();
        postRepository.save(post);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostListResponse> getPostsByBoard(String boardId, int page, int size) {
        // 실제 구현에서는 Pageable을 사용하여 페이징 처리
        List<Post> posts = postRepository.findByBoardIdAndStatus(
                BoardId.of(boardId), PostStatus.PUBLISHED);

        return posts.stream()
                .map(this::toPostListResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostListResponse> getRecentPosts(int limit) {
        List<Post> posts = postRepository.findRecentPosts(limit);
        return posts.stream()
                .map(this::toPostListResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostListResponse> getPopularPosts(int limit) {
        List<Post> posts = postRepository.findPopularPosts(limit);
        return posts.stream()
                .map(this::toPostListResponse)
                .collect(Collectors.toList());
    }

    private PostListResponse toPostListResponse(Post post) {
        // 작성자 정보 조회
        User author = userRepository.findById(post.getAuthorId())
                .orElse(null);
        String authorName = author != null ? author.getUsername() : "Unknown";

        // 댓글 수 조회 (실제로는 CommentRepository 필요)
        int commentCount = 0; // TODO: 댓글 수 조회 로직 추가

        return new PostListResponse(
                post.getPostId().getValue(),
                post.getTitle().getValue(),
                authorName,
                post.getViewCount(),
                commentCount,
                post.getCreatedAt()
        );
    }
}
