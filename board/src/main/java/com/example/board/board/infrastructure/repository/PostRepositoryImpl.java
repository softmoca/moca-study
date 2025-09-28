package com.example.board.board.infrastructure.repository;

import com.example.board.board.domain.model.*;
import com.example.board.board.domain.repository.PostRepository;
import com.example.board.board.infrastructure.entity.PostEntity;
import com.example.board.board.infrastructure.entity.PostEntityStatus;
import com.example.board.user.domain.model.UserId;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final PostJpaRepository jpaRepository;

    public PostRepositoryImpl(PostJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Post save(Post post) {
        PostEntity entity = PostEntity.fromDomain(post);
        PostEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Post> findById(PostId postId) {
        // PostId(UUID)로 조회 - publicId 컬럼에서 찾음
        return jpaRepository.findByPublicId(postId.getValue())
                .map(PostEntity::toDomain);
    }

    @Override
    public List<Post> findByBoardId(BoardId boardId) {
        return jpaRepository.findByBoardPublicIdOrderByCreatedAtDesc(boardId.getValue()).stream()
                .map(PostEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> findByBoardIdAndStatus(BoardId boardId, PostStatus status) {
        PostEntityStatus entityStatus = PostEntityStatus.valueOf(status.name());
        return jpaRepository.findByBoardPublicIdAndStatusOrderByCreatedAtDesc(boardId.getValue(), entityStatus).stream()
                .map(PostEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> findByAuthorId(UserId authorId) {
        return jpaRepository.findByAuthorPublicIdOrderByCreatedAtDesc(authorId.getValue()).stream()
                .map(PostEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> findRecentPosts(int limit) {
        PageRequest pageRequest = PageRequest.of(0, limit);
        return jpaRepository.findRecentPublishedPosts(pageRequest).stream()
                .map(PostEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> findPopularPosts(int limit) {
        PageRequest pageRequest = PageRequest.of(0, limit);
        return jpaRepository.findPopularPublishedPosts(pageRequest).stream()
                .map(PostEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(PostId postId) {
        // publicId로 삭제하려면 먼저 엔티티를 찾아서 삭제해야 함
        jpaRepository.findByPublicId(postId.getValue())
                .ifPresent(jpaRepository::delete);
    }

    @Override
    public long countByBoardId(BoardId boardId) {
        return jpaRepository.countByBoardPublicId(boardId.getValue());
    }

    @Override
    public long countByAuthorId(UserId authorId) {
        return jpaRepository.countByAuthorPublicId(authorId.getValue());
    }
}