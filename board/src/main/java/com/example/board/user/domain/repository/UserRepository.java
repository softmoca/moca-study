package com.example.board.user.domain.repository;

import com.example.board.user.domain.model.User;
import com.example.board.user.domain.model.UserId;
import com.example.board.user.domain.model.Email;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UserId userId);
    Optional<User> findByEmail(Email email);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(Email email);
    boolean existsByUsername(String username);
    void deleteById(UserId userId);
}
