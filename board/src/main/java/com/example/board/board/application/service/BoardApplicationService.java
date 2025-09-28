package com.example.board.board.application.service;

import com.example.board.board.application.dto.BoardCreateRequest;
import com.example.board.board.application.dto.BoardResponse;
import com.example.board.board.application.usecase.CreateBoardUseCase;
import com.example.board.board.domain.model.Board;
import com.example.board.board.domain.model.BoardId;
import com.example.board.board.domain.repository.BoardRepository;
import com.example.board.board.domain.repository.PostRepository;
import com.example.board.board.domain.service.BoardDomainService;
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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardApplicationService implements CreateBoardUseCase {

    private final BoardRepository boardRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final BoardDomainService boardDomainService;

    @Override
    @Transactional
    public BoardResponse createBoard(BoardCreateRequest request, String createdBy) {
        try {
            // 사용자 조회 및 권한 확인
            UserId createdByUserId = UserId.of(createdBy);
            User creator = userRepository.findById(createdByUserId)
                    .orElseThrow(() -> new EntityNotFoundException("User", createdBy));

            // 관리자 권한 확인
            if (!creator.isAdmin()) {
                throw new BusinessException("게시판 생성 권한이 없습니다. 관리자만 게시판을 생성할 수 있습니다.");
            }

            // 도메인 서비스를 통한 유효성 검증
            boardDomainService.validateBoardCreation(request.getName());

            // 게시판 생성
            Board board = new Board(request.getName(), request.getDescription(), createdByUserId);

            // 저장
            Board savedBoard = boardRepository.save(board);

            return BoardResponse.from(savedBoard, 0L); // 새로 생성된 게시판이므로 postCount는 0

        } catch (IllegalArgumentException e) {
            throw new BusinessException("게시판 생성 실패: " + e.getMessage());
        }
    }

    public List<BoardResponse> getAllBoards() {
        List<Board> boards = boardRepository.findAllActive();
        return boards.stream()
                .map(board -> {
                    long postCount = postRepository.countByBoardId(board.getBoardId());
                    return BoardResponse.from(board, postCount);
                })
                .collect(Collectors.toList());
    }

    public BoardResponse getBoard(String boardId) {
        Board board = boardRepository.findById(BoardId.of(boardId))
                .orElseThrow(() -> new EntityNotFoundException("Board", boardId));

        long postCount = postRepository.countByBoardId(board.getBoardId());
        return BoardResponse.from(board, postCount);
    }
}