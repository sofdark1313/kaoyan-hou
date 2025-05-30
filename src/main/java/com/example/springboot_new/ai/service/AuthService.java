package com.example.springboot_new.ai.service;

import com.example.springboot_new.ai.mapper.AccountPasswordMapper;
import com.example.springboot_new.ai.pojo.*;
import com.example.springboot_new.ai.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        String phoneMask = maskPhone(user.getPhoneNumber());
        return new LoginResponse(user.getAccountId(), user.getAccountIdentity(), phoneMask, token);
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
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAccountIdentity("普通用户"); // 默认普通用户
        user.setMessageContent("");
        
        mapper.insert(user);
        String token = jwtUtil.generateToken(user.getAccountId(), user.getAccount());
        String phoneMask = maskPhone(user.getPhoneNumber());
        
        return new RegisterResponse(user.getAccountId(), phoneMask, token);
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
    
    // 实现找回账号密码方法
    @Override
    public RecoveryResponse recoverAccount(RecoveryRequest request) {
        // 验证验证码
        if (!"wc666".equals(request.getVerifyCode())) {
            throw new RuntimeException("验证码错误");
        }
        
        // 根据手机号查找账户
        AccountPassword user = mapper.findByPhoneNumber(request.getPhoneNumber());
        if (user == null) {
            throw new RuntimeException("该手机号未注册");
        }
        
        // 更新密码
        user.setPassword(request.getNewPassword());
        mapper.updatePassword(user);
        
        // 返回账号信息和脱敏手机号
        String phoneMask = maskPhone(user.getPhoneNumber());
        return new RecoveryResponse(user.getAccount(), phoneMask);
    }
    
    // 实现用户充值升级方法
    @Override
    public UpgradeResponse upgradeAccount(String token, UpgradeRequest request) {
        // 验证验证码
        if (!"wc233".equals(request.getVerifyCode())) {
            throw new RuntimeException("验证码错误");
        }
        
        // 验证token并获取用户信息
        String account = jwtUtil.extractAccount(token.replace("Bearer ", ""));
        AccountPassword user = mapper.findByAccount(account);
        
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 验证账户ID是否匹配
        if (!user.getAccountId().equals(request.getAccountId())) {
            throw new RuntimeException("账户ID不匹配");
        }
        
        // 更新用户身份为VIP用户
        user.setAccountIdentity("VIP用户");
        mapper.updateAccountIdentity(user);
        
        // 计算VIP到期时间（当前时间加一年）
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        Date expireDate = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String expireTime = sdf.format(expireDate);
        
        return new UpgradeResponse(expireTime);
    }
    
    private String maskPhone(String phone) {
        if (phone == null || phone.length() != 11) return "";
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }
}