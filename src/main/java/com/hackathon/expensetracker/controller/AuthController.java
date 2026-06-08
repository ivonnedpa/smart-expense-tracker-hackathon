package com.hackathon.expensetracker.controller;

import com.hackathon.expensetracker.dto.JwtAuthResponse;
import com.hackathon.expensetracker.dto.LoginRequest;
import com.hackathon.expensetracker.dto.RegisterRequest;
import com.hackathon.expensetracker.dto.ApiResponse;
import com.hackathon.expensetracker.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs for user authentication and registration")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Register a new user account and receive JWT token")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        JwtAuthResponse authResponse = authService.register(registerRequest);
        ApiResponse response = new ApiResponse(true, "User registered successfully");
        response.setData(authResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Login with email and password to receive JWT token")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        JwtAuthResponse authResponse = authService.login(loginRequest);
        ApiResponse response = new ApiResponse(true, "Login successful");
        response.setData(authResponse);
        return ResponseEntity.ok(response);
    }
}
