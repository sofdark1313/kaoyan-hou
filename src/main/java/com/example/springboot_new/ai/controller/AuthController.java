package com.example.springboot_new.ai.controller;

import com.example.springboot_new.ai.pojo.*;
import com.example.springboot_new.ai.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {
    
    @Autowired
    private IAuthService authService;
    
    @PostMapping("/login")
    public ResponseMessage<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return new ResponseMessage<>(1, "登录成功", response);
        } catch (Exception e) {
            return new ResponseMessage<>(0, e.getMessage(), null);
        }
    }
    
    @PostMapping("/register")
    public ResponseMessage<RegisterResponse> register(@RequestBody RegisterRequest request) {
        try {
            RegisterResponse response = authService.register(request);
            return new ResponseMessage<>(1, "注册成功", response);
        } catch (Exception e) {
            return new ResponseMessage<>(0, e.getMessage(), null);
        }
    }
    
    @PutMapping("/password")
    public ResponseMessage<Void> changePassword(
            @RequestHeader("Authorization") String token,
            @RequestBody PasswordChangeRequest request) {
        try {
            authService.changePassword(token, request);
            return new ResponseMessage<>(1, "密码修改成功", null);
        } catch (Exception e) {
            return new ResponseMessage<>(0, "修改失败", null);
        }
    }
    
    @DeleteMapping("/deactivate")
    public ResponseMessage<Void> deactivateAccount(
            @RequestHeader("Authorization") String token,
            @RequestBody DeactivateRequest request) {
        try {
            authService.deactivateAccount(token, request);
            return new ResponseMessage<>(1, "账户已注销", null);
        } catch (Exception e) {
            return new ResponseMessage<>(0, e.getMessage(), null);
        }
    }
    
    // 新增：找回账号密码接口
    @PostMapping("/recovery")
    public ResponseMessage<RecoveryResponse> recoverAccount(@RequestBody RecoveryRequest request) {
        try {
            RecoveryResponse response = authService.recoverAccount(request);
            return new ResponseMessage<>(1, "密码重置成功", response);
        } catch (Exception e) {
            return new ResponseMessage<>(0, e.getMessage(), null);
        }
    }
    
    // 新增：用户身份充值接口
    @PostMapping("/upgrade")
    public ResponseMessage<UpgradeResponse> upgradeAccount(
            @RequestHeader("Authorization") String token,
            @RequestBody UpgradeRequest request) {
        try {
            UpgradeResponse response = authService.upgradeAccount(token, request);
            return new ResponseMessage<>(1, "升级VIP成功", response);
        } catch (Exception e) {
            return new ResponseMessage<>(0, e.getMessage(), null);
        }
    }
}