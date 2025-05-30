package com.example.springboot_new.ai.pojo;

import lombok.Data;

@Data
public class RegisterResponse {
    private Integer accountId;
    private String phoneNumber;
    private String token;
    
    public RegisterResponse(Integer accountId, String phoneNumber, String token) {
        this.accountId = accountId;
        this.phoneNumber = phoneNumber;
        this.token = token;
    }
}