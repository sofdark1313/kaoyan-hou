package com.example.springboot_new.ai.pojo;

import lombok.Data;

@Data
public class LoginResponse {
    private Integer accountId;
    private String accountIdentity;
    private String phoneNumber;
    private String token;
    
    public LoginResponse(Integer accountId, String accountIdentity, String phoneNumber, String token) {
        this.accountId = accountId;
        this.accountIdentity = accountIdentity;
        this.phoneNumber = phoneNumber;
        this.token = token;
    }
}