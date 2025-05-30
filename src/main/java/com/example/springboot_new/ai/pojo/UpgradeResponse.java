package com.example.springboot_new.ai.pojo;

import lombok.Data;

@Data
public class UpgradeResponse {
    private String expireTime;
    
    public UpgradeResponse(String expireTime) {
        this.expireTime = expireTime;
    }
}