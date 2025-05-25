package com.example.springboot_new.ai.service;

import com.example.springboot_new.ai.mapper.AccountPasswordMapper;
import com.example.springboot_new.ai.pojo.*;
import com.example.springboot_new.ai.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {
    
    @Autowired
    private AccountPasswordMapper mapper;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    public LoginResponse login(LoginRequest request) {
        AccountPassword user = mapper.findByAccount(request.getAccount());
        
        if (user == null) {
            throw new RuntimeException("账号或密码错误");
        }
        
        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("账号或密码错误");
        }
        
        String token = jwtUtil.generateToken(user.getAccountId(), user.getAccount());
        return new LoginResponse(user.getAccountId(), user.getAccountIdentity(), token);
    }
    
    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (mapper.countByAccount(request.getAccount()) > 0) {
            throw new RuntimeException("账号已存在");
        }
        
        AccountPassword user = new AccountPassword();
        user.setAccount(request.getAccount());
        user.setPassword(request.getPassword());
        user.setAccountName(request.getAccountName());
        user.setAccountIdentity("普通用户"); // 默认普通用户
        
        mapper.insert(user);
        String token = jwtUtil.generateToken(user.getAccountId(), user.getAccount());
        
        return new RegisterResponse(user.getAccountId(), token);
    }
    
    @Override
    public void changePassword(String token, PasswordChangeRequest request) {
        String account = jwtUtil.extractAccount(token.replace("Bearer ", ""));
        AccountPassword user = mapper.findByAccount(account);
        
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        if (!user.getPassword().equals(request.getOldPassword())) {
            throw new RuntimeException("原密码错误");
        }
        
        user.setPassword(request.getNewPassword());
        mapper.updatePassword(user);
    }
    
    @Override
    public void deactivateAccount(String token, DeactivateRequest request) {
        String account = jwtUtil.extractAccount(token.replace("Bearer ", ""));
        AccountPassword user = mapper.findByAccount(account);
        
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("密码验证失败");
        }
        
        mapper.deleteById(user.getAccountId());
    }
}