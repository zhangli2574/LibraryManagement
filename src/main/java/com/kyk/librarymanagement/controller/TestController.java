package com.kyk.librarymanagement.controller;

import com.kyk.librarymanagement.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {
    
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @GetMapping("/bcrypt")
    public ResponseEntity<ApiResponse<String>> generateBcrypt(@RequestParam String password) {
        String encoded = passwordEncoder.encode(password);
        return ResponseEntity.ok(ApiResponse.success(encoded));
    }
}
