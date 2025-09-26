package com.example.board.board.infrastructure.entity;

import com.example.board.board.domain.model.*;
import com.example.board.user.domain.model.UserId;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;


@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // DB 최적화용

    @Column(name = "public_id", unique = true, nullable = false)
    private String publicId;  // 외부 노출용 (PostId의 값)

    @Column(name = "board_public_id", nullable = false)
    private String boardPublicId;  // BoardId의 값

    @Column(name = "author_public_id", nullable = false)
    private String authorPublicId; // UserId의 값

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 10000)
    @Lob
    private String content;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostEntityStatus status;

    @Column(nullable = false)
    private Integer viewCount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;


    // 생성자
    public PostEntity(String publicId, String boardPublicId, String title,
                      String content, String authorPublicId, PostEntityStatus status,
                      Integer viewCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.publicId = publicId;
        this.boardPublicId = boardPublicId;
        this.title = title;
        this.content = content;
        this.authorPublicId = authorPublicId;
        this.status = status;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 도메인 모델로 변환
    public Post toDomain() {
        return new Post(
                PostId.of(this.publicId),
                BoardId.of(this.boardPublicId),
                Title.of(this.title),
                Content.of(this.content),
                UserId.of(this.authorPublicId),
                mapToDomainStatus(this.status),
                this.viewCount,
                this.createdAt,
                this.updatedAt
        );
    }

    // 도메인 모델에서 변환
    public static PostEntity fromDomain(Post post) {
        return new PostEntity(
                post.getPostId().getValue(),
                post.getBoardId().getValue(),
                post.getTitle().getValue(),
                post.getContent().getValue(),
                post.getAuthorId().getValue(),
                mapToEntityStatus(post.getStatus()),
                post.getViewCount(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }


    // 상태 매핑 메서드들
    private static PostStatus mapToDomainStatus(PostEntityStatus entityStatus) {
        return switch (entityStatus) {
            case DRAFT -> PostStatus.DRAFT;
            case PUBLISHED -> PostStatus.PUBLISHED;
            case DELETED -> PostStatus.DELETED;
        };
    }

    private static PostEntityStatus mapToEntityStatus(PostStatus domainStatus) {
        return switch (domainStatus) {
            case DRAFT -> PostEntityStatus.DRAFT;
            case PUBLISHED -> PostEntityStatus.PUBLISHED;
            case DELETED -> PostEntityStatus.DELETED;
        };
    }
}