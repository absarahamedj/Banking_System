package com.Bank.auth_service.controller;

import com.Bank.auth_service.dto.AuthResponse;
import com.Bank.auth_service.dto.LoginRequest;
import com.Bank.auth_service.dto.RegisterRequest;
import com.Bank.auth_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}