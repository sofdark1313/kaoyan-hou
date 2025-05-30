package com.example.springboot_new.ai.pojo;

import lombok.Data;

@Data
public class RecoveryRequest {
    private String phoneNumber;
    private String verifyCode;
    private String newPassword;
}