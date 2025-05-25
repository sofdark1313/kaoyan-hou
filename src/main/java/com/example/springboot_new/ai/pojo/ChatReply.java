package com.example.springboot_new.ai.pojo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ChatReply {
    private List<Map<String, String>> message;
    private Object result1;
    private Object result2;
    private Object result3;
}