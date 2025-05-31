package com.example.springboot_new.ai.pojo;

import lombok.Data;
import java.util.Map;  // 添加这行
import java.util.List; // 添加这行

@Data
public class ChatHistory {
    private Integer accountId;
    private Integer dialogId;
    private List<Map<String, String>> conversation;
}