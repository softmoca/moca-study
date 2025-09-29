package com.example.board.user.presentation.controller;

import com.example.board.user.application.dto.UserCreateRequest;
import com.example.board.user.application.dto.UserResponse;
import com.example.board.user.application.service.UserApplicationService;
import com.example.board.user.presentation.api.UserApi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController implements UserApi {

    private final UserApplicationService userApplicationService;

    public UserController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        UserResponse response = userApplicationService.createUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {
        UserResponse response = userApplicationService.findById(userId.toString());
        return ResponseEntity.ok(response);
    }
}