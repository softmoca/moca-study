package com.example.board.board.infrastructure.entity;

import com.example.board.board.domain.model.Board;
import com.example.board.board.domain.model.BoardId;
import com.example.board.user.domain.model.UserId;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "boards")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // DB 최적화용 Auto Increment

    @Column(name = "public_id", unique = true, nullable = false)
    private String publicId;  // 외부 노출용 UUID (BoardId의 값)

    @Column(unique = true, nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(name = "created_by_public_id", nullable = false)
    private String createdByPublicId;  // UserId의 값

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Boolean active;

    // 생성자
    public BoardEntity(String publicId, String name, String description,
                       String createdByPublicId, LocalDateTime createdAt,
                       LocalDateTime updatedAt, Boolean active) {
        this.publicId = publicId;
        this.name = name;
        this.description = description;
        this.createdByPublicId = createdByPublicId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.active = active;
    }

    // 도메인 모델로 변환 - publicId 사용
    public Board toDomain() {
        return new Board(
                BoardId.of(this.publicId),  // publicId를 BoardId로 변환
                this.name,
                this.description,
                UserId.of(this.createdByPublicId),  // createdByPublicId를 UserId로 변환
                this.createdAt,
                this.updatedAt,
                this.active
        );
    }

    // 도메인 모델에서 변환 - ID 값들을 publicId로 설정
    public static BoardEntity fromDomain(Board board) {
        return new BoardEntity(
                board.getBoardId().getValue(),      // BoardId의 값을 publicId로
                board.getName(),
                board.getDescription(),
                board.getCreatedBy().getValue(),    // UserId의 값을 createdByPublicId로
                board.getCreatedAt(),
                board.getUpdatedAt(),
                board.isActive()
        );
    }
}