package com.example.springboot_new.ai.pojo;

import lombok.Data;

@Data
public class UpgradeRequest {
    private Integer accountId;
    private String verifyCode;
    private String paymentType;
}