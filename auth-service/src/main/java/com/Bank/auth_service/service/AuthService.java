package com.Bank.auth_service.service;

import com.Bank.auth_service.dto.AuthResponse;
import com.Bank.auth_service.dto.LoginRequest;
import com.Bank.auth_service.dto.RegisterRequest;

public interface AuthService {

    String register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}