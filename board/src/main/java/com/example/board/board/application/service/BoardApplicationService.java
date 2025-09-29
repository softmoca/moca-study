package com.example.board.board.application.service;

import com.example.board.board.application.dto.BoardCreateRequest;
import com.example.board.board.application.dto.BoardResponse;
import com.example.board.board.application.usecase.CreateBoardUseCase;
import com.example.board.board.domain.Board;
import com.example.board.board.repository.BoardRepository;
import com.example.board.board.repository.PostRepository;  // 추가
import com.example.board.board.domain.service.BoardDomainService;
import com.example.board.user.domain.User;
import com.example.board.user.repository.UserRepository;
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
    private final PostRepository postRepository;  // 추가
    private final UserRepository userRepository;
    private final BoardDomainService boardDomainService;

    @Override
    @Transactional
    public BoardResponse createBoard(BoardCreateRequest request, String userId) {
        try {
            Long userIdLong = Long.parseLong(userId);
            User creator = userRepository.findById(userIdLong)
                    .orElseThrow(() -> new EntityNotFoundException("User", userId));

            if (!creator.isAdmin()) {
                throw new BusinessException("게시판 생성 권한이 없습니다. 관리자만 게시판을 생성할 수 있습니다.");
            }

            boardDomainService.validateBoardCreation(request.getName());

            Board board = new Board(request.getName(), request.getDescription(), userIdLong);
            Board savedBoard = boardRepository.save(board);

            return BoardResponse.from(savedBoard, 0L);

        } catch (NumberFormatException e) {
            throw new BusinessException("잘못된 사용자 ID 형식입니다");
        } catch (IllegalArgumentException e) {
            throw new BusinessException("게시판 생성 실패: " + e.getMessage());
        }
    }

    public List<BoardResponse> getAllBoards() {
        List<Board> boards = boardRepository.findAllActive();
        return boards.stream()
                .map(board -> {
                    long postCount = postRepository.countByBoardId(board.getId());  // 이제 작동!
                    return BoardResponse.from(board, postCount);
                })
                .collect(Collectors.toList());
    }

    public BoardResponse getBoard(String boardId) {
        try {
            Long id = Long.parseLong(boardId);
            Board board = boardRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Board", boardId));

            long postCount = postRepository.countByBoardId(board.getId());  // 이제 작동!
            return BoardResponse.from(board, postCount);
        } catch (NumberFormatException e) {
            throw new BusinessException("잘못된 게시판 ID 형식입니다");
        }
    }
}