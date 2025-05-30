package com.example.springboot_new.ai.pojo;

import lombok.Data;

@Data
public class RegisterRequest {
    private String account;
    private String password;
    private String accountName;
    private String phoneNumber;
}