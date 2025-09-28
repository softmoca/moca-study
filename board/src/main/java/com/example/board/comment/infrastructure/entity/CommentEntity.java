package com.example.board.comment.infrastructure.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // DB 최적화용 Auto Increment

    @Column(name = "public_id", unique = true, nullable = false)
    private String publicId;  // 외부 노출용 UUID (CommentId의 값)

    @Column(name = "post_public_id", nullable = false)
    private String postPublicId;  // PostId의 값

    @Column(name = "author_public_id", nullable = false)
    private String authorPublicId;  // UserId의 값

    @Column(nullable = false, length = 1000)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommentEntityStatus status;

    @Column(name = "parent_comment_public_id")
    private String parentCommentPublicId;  // 부모 CommentId의 값

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 생성자
    public CommentEntity(String publicId, String postPublicId, String authorPublicId,
                         String content, CommentEntityStatus status, String parentCommentPublicId,
                         LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.publicId = publicId;
        this.postPublicId = postPublicId;
        this.authorPublicId = authorPublicId;
        this.content = content;
        this.status = status;
        this.parentCommentPublicId = parentCommentPublicId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 도메인 모델로 변환 - publicId 사용
    public com.example.board.comment.domain.model.Comment toDomain() {
        return new com.example.board.comment.domain.model.Comment(
                com.example.board.comment.domain.model.CommentId.of(this.publicId),
                com.example.board.board.domain.model.PostId.of(this.postPublicId),
                com.example.board.user.domain.model.UserId.of(this.authorPublicId),
                com.example.board.comment.domain.model.CommentContent.of(this.content),
                mapToDomainStatus(this.status),
                this.parentCommentPublicId != null ?
                        com.example.board.comment.domain.model.CommentId.of(this.parentCommentPublicId) : null,
                this.createdAt,
                this.updatedAt
        );
    }

    // 도메인 모델에서 변환 - ID 값들을 publicId로 설정
    public static CommentEntity fromDomain(com.example.board.comment.domain.model.Comment comment) {
        return new CommentEntity(
                comment.getCommentId().getValue(),  // CommentId의 값을 publicId로
                comment.getPostId().getValue(),     // PostId의 값을 postPublicId로
                comment.getAuthorId().getValue(),   // UserId의 값을 authorPublicId로
                comment.getContent().getValue(),
                mapToEntityStatus(comment.getStatus()),
                comment.getParentCommentId() != null ?
                        comment.getParentCommentId().getValue() : null,  // 부모 CommentId의 값
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }

    // 상태 매핑 메서드들
    private static com.example.board.comment.domain.model.CommentStatus mapToDomainStatus(CommentEntityStatus entityStatus) {
        return switch (entityStatus) {
            case ACTIVE -> com.example.board.comment.domain.model.CommentStatus.ACTIVE;
            case DELETED -> com.example.board.comment.domain.model.CommentStatus.DELETED;
        };
    }

    private static CommentEntityStatus mapToEntityStatus(com.example.board.comment.domain.model.CommentStatus domainStatus) {
        return switch (domainStatus) {
            case ACTIVE -> CommentEntityStatus.ACTIVE;
            case DELETED -> CommentEntityStatus.DELETED;
        };
    }
}