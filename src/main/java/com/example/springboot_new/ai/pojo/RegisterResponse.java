package com.example.springboot_new.ai.pojo;

import lombok.Data;

@Data
public class RegisterResponse {
    private Integer accountId;
    private String token;
    
    public RegisterResponse(Integer accountId, String token) {
        this.accountId = accountId;
        this.token = token;
    }
}