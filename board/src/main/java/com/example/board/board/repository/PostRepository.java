package com.example.board.board.repository;

import com.example.board.board.domain.Post;
import com.example.board.board.domain.PostStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 게시판별 조회
    List<Post> findByBoardIdOrderByCreatedAtDesc(Long boardId);

    List<Post> findByBoardIdAndStatusOrderByCreatedAtDesc(Long boardId, PostStatus status);

    // 작성자별 조회
    List<Post> findByAuthorIdOrderByCreatedAtDesc(Long authorId);

    // 최신 게시글 조회
    @Query("SELECT p FROM Post p WHERE p.status = 'PUBLISHED' ORDER BY p.createdAt DESC")
    List<Post> findRecentPublishedPosts(Pageable pageable);

    // 인기 게시글 조회
    @Query("SELECT p FROM Post p WHERE p.status = 'PUBLISHED' ORDER BY p.viewCount DESC, p.createdAt DESC")
    List<Post> findPopularPublishedPosts(Pageable pageable);

    // 카운트
    long countByBoardId(Long boardId);

    long countByAuthorId(Long authorId);

    // 조회수 증가 (벌크 연산)
    @Modifying
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
    int incrementViewCount(@Param("postId") Long postId);
}