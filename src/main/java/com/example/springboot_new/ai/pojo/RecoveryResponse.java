package com.example.springboot_new.ai.pojo;

import lombok.Data;

@Data
public class RecoveryResponse {
    private String account;
    private String phoneMask;
    
    public RecoveryResponse(String account, String phoneMask) {
        this.account = account;
        this.phoneMask = phoneMask;
    }
}