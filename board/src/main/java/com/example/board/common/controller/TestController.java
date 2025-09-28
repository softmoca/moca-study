package com.example.board.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@Tag(name = "테스트", description = "Swagger 테스트용 API")
public class TestController {

    @GetMapping("/hello")
    @Operation(summary = "Hello World", description = "간단한 테스트 API")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello, Swagger!");
    }

    @GetMapping("/health")
    @Operation(summary = "Health Check", description = "서버 상태 확인")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}