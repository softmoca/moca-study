package com.example.board.board.infrastructure.repository;

import com.example.board.board.domain.model.*;
import com.example.board.board.domain.repository.PostRepository;
import com.example.board.board.infrastructure.entity.PostEntity;
import com.example.board.user.domain.model.UserId;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

interface JpaPostEntityRepository extends JpaRepository<PostEntity, String> {
    List<PostEntity> findByBoardIdOrderByCreatedAtDesc(String boardId);
    List<PostEntity> findByBoardIdAndStatusOrderByCreatedAtDesc(String boardId, PostEntity.PostStatus status);
    List<PostEntity> findByAuthorIdOrderByCreatedAtDesc(String authorId);

    @Query("SELECT p FROM PostEntity p WHERE p.status = 'PUBLISHED' ORDER BY p.createdAt DESC")
    List<PostEntity> findRecentPublishedPosts(PageRequest pageRequest);

    @Query("SELECT p FROM PostEntity p WHERE p.status = 'PUBLISHED' ORDER BY p.viewCount DESC, p.createdAt DESC")
    List<PostEntity> findPopularPublishedPosts(PageRequest pageRequest);

    long countByBoardId(String boardId);
    long countByAuthorId(String authorId);
}

@Repository
public class JpaPostRepository implements PostRepository {

    private final JpaPostEntityRepository jpaRepository;

    public JpaPostRepository(JpaPostEntityRepository jpaRepository) {
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
        return jpaRepository.findById(postId.getValue())
                .map(PostEntity::toDomain);
    }

    @Override
    public List<Post> findByBoardId(BoardId boardId) {
        return jpaRepository.findByBoardIdOrderByCreatedAtDesc(boardId.getValue()).stream()
                .map(PostEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> findByBoardIdAndStatus(BoardId boardId, PostStatus status) {
        PostEntity.PostStatus entityStatus = PostEntity.PostStatus.valueOf(status.name());
        return jpaRepository.findByBoardIdAndStatusOrderByCreatedAtDesc(boardId.getValue(), entityStatus).stream()
                .map(PostEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> findByAuthorId(UserId authorId) {
        return jpaRepository.findByAuthorIdOrderByCreatedAtDesc(authorId.getValue()).stream()
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
        jpaRepository.deleteById(postId.getValue());
    }

    @Override
    public long countByBoardId(BoardId boardId) {
        return jpaRepository.countByBoardId(boardId.getValue());
    }

    @Override
    public long countByAuthorId(UserId authorId) {
        return jpaRepository.countByAuthorId(authorId.getValue());
    }
}
