package com.example.springboot_new.ai.service;

import com.example.springboot_new.ai.mapper.AccountMapper;
import com.example.springboot_new.ai.pojo.AccountInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    private AccountMapper accountMapper;
    
    public AccountInfo getAccountById(Integer accountId) {
        AccountInfo accountInfo = accountMapper.getAccountById(accountId);
        if (accountInfo != null && accountInfo.getPhoneNumber() != null) {
            // 手机号脱敏处理
            String phone = accountInfo.getPhoneNumber();
            if (phone.length() == 11) {
                accountInfo.setPhoneNumber(phone.substring(0, 3) + "****" + phone.substring(7));
            }
        }
        return accountInfo;
    }
}