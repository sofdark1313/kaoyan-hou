package com.example.springboot_new.ai.pojo;

import lombok.Data;

@Data
public class LoginRequest {
    private String account;
    private String password;
}