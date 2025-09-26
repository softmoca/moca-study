package com.example.board.board.domain.service;

import com.example.board.board.domain.model.Board;
import com.example.board.board.domain.model.Post;
import com.example.board.board.domain.model.BoardId;
import com.example.board.board.domain.repository.BoardRepository;
import com.example.board.board.domain.repository.PostRepository;
import com.example.board.user.domain.model.User;
import com.example.board.user.domain.model.UserId;
import com.example.board.common.exception.BusinessException;
import com.example.board.common.exception.EntityNotFoundException;

import org.springframework.stereotype.Service;

@Service
public class PostDomainService {

    private final BoardRepository boardRepository;
    private final PostRepository postRepository;

    public PostDomainService(BoardRepository boardRepository, PostRepository postRepository) {
        this.boardRepository = boardRepository;
        this.postRepository = postRepository;
    }

    public void validatePostCreation(BoardId boardId, UserId authorId) {
        // 게시판 존재 여부 및 활성 상태 확인
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board", boardId.getValue()));

        if (!board.canCreatePost()) {
            throw new BusinessException("Inactive board cannot accept new posts");
        }
    }

    public void validatePostEdit(Post post, UserId userId, boolean isAdmin) {
        // 삭제된 게시글 확인
        if (post.isDeleted()) {
            throw new BusinessException("Cannot edit deleted post");
        }

        // 작성자 또는 관리자만 수정 가능
        if (!post.isAuthor(userId) && !isAdmin) {
            throw new BusinessException("Only author or admin can edit this post");
        }

        // 수정 가능한 상태인지 확인
        if (!post.canEdit()) {
            throw new BusinessException("Post cannot be edited in current status");
        }
    }

    public void validatePostDeletion(Post post, UserId userId, boolean isAdmin) {
        // 이미 삭제된 게시글 확인
        if (post.isDeleted()) {
            throw new BusinessException("Post is already deleted");
        }

        // 작성자 또는 관리자만 삭제 가능
        if (!post.isAuthor(userId) && !isAdmin) {
            throw new BusinessException("Only author or admin can delete this post");
        }
    }


    public boolean canViewPost(Post post, UserId viewerId, boolean isAdmin) {
        // 관리자는 모든 게시글 조회 가능
        if (isAdmin) {
            return true;
        }

        // 작성자는 자신의 모든 게시글 조회 가능
        if (post.isAuthor(viewerId)) {
            return true;
        }

        // 작성자는 자신의 모든 게시글 조회 가능
        if (post.isAuthor(viewerId)) {
            return true;
        }

        // 일반 사용자는 게시된 글만 조회 가능
        return post.canView();
    }




    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Board name cannot be null or empty");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Board name must be less than 100 characters");
        }
        return name.trim();
    }



}






