package com.example.board.board.domain.service;

import com.example.board.board.domain.repository.BoardRepository;
import com.example.board.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardDomainService {

    private final BoardRepository boardRepository;

    public void validateBoardCreation(String name) {
        if (boardRepository.existsByName(name)) {
            throw new BusinessException("이미 존재하는 게시판 이름입니다: " + name);
        }
    }

    public boolean isBoardNameAvailable(String name) {
        return !boardRepository.existsByName(name);
    }
}