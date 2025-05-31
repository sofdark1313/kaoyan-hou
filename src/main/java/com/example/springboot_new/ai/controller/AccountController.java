package com.example.springboot_new.ai.controller;

import com.example.springboot_new.ai.pojo.AccountInfo;
import com.example.springboot_new.ai.pojo.ResponseMessage;
import com.example.springboot_new.ai.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    
    @Autowired
    private AccountService accountService;
    
    @GetMapping("{accountId}")
    public ResponseMessage<AccountInfo> getAccountInfo(@PathVariable Integer accountId) {
        try {
            AccountInfo accountInfo = accountService.getAccountById(accountId);
            if (accountInfo == null) {
                return new ResponseMessage<>(0, "账户不存在", null);
            }
            return new ResponseMessage<>(1, "查询成功", accountInfo);
        } catch (Exception e) {
            return new ResponseMessage<>(0, e.getMessage(), null);
        }
    }
}