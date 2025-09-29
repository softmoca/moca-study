package com.example.board.board.domain.repository;

import com.example.board.board.domain.model.Post;
import com.example.board.board.domain.model.PostId;
import com.example.board.board.domain.model.BoardId;
import com.example.board.board.domain.model.PostStatus;
import com.example.board.user.domain.model.UserId;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post);
    Optional<Post> findById(PostId postId);
    List<Post> findByBoardId(BoardId boardId);
    List<Post> findByBoardIdAndStatus(BoardId boardId, PostStatus status);
    List<Post> findByAuthorId(UserId authorId);
    List<Post> findRecentPosts(int limit);
    List<Post> findPopularPosts(int limit);
    void deleteById(PostId postId);
    long countByBoardId(BoardId boardId);
    long countByAuthorId(UserId authorId);

    int incrementViewCount(String postId);
}