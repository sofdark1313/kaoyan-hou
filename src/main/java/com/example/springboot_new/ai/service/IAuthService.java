package com.example.springboot_new.ai.service;

import com.example.springboot_new.ai.pojo.*;

public interface IAuthService {
    LoginResponse login(LoginRequest request);
    RegisterResponse register(RegisterRequest request);
    void changePassword(String token, PasswordChangeRequest request);
    void deactivateAccount(String token, DeactivateRequest request);
    
    // 新增方法：找回账号密码
    RecoveryResponse recoverAccount(RecoveryRequest request);
    
    // 新增方法：用户充值升级
    UpgradeResponse upgradeAccount(String token, UpgradeRequest request);
}