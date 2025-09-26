package com.example.board.board.infrastructure.repository;

import com.example.board.board.infrastructure.entity.PostEntity;
import com.example.board.board.infrastructure.entity.PostEntityStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

interface PostJpaRepository extends JpaRepository<PostEntity, String> {



    Optional<PostEntity> findByPublicId(String publicId);

    // Board의 publicId로 Post들을 찾는 메서드들
    List<PostEntity> findByBoardPublicIdOrderByCreatedAtDesc(String boardPublicId);
    List<PostEntity> findByBoardPublicIdAndStatusOrderByCreatedAtDesc(String boardPublicId, PostEntityStatus status);

    // Author의 publicId로 Post들을 찾는 메서드
    List<PostEntity> findByAuthorPublicIdOrderByCreatedAtDesc(String authorPublicId);




    @Query("SELECT p FROM PostEntity p WHERE p.status = 'PUBLISHED' ORDER BY p.createdAt DESC")
    List<PostEntity> findRecentPublishedPosts(PageRequest pageRequest);

    @Query("SELECT p FROM PostEntity p WHERE p.status = 'PUBLISHED' ORDER BY p.viewCount DESC, p.createdAt DESC")
    List<PostEntity> findPopularPublishedPosts(PageRequest pageRequest);

    long countByBoardPublicId(String boardPublicId);
    long countByAuthorPublicId(String authorPublicId);
}


