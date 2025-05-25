package com.example.springboot_new.ai.service;

import com.example.springboot_new.ai.pojo.*;

public interface IAuthService {
    LoginResponse login(LoginRequest request);
    RegisterResponse register(RegisterRequest request);
    void changePassword(String token, PasswordChangeRequest request);
    void deactivateAccount(String token, DeactivateRequest request);
}