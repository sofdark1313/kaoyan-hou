package com.example.springboot_new.ai.controller;

import com.example.springboot_new.ai.pojo.ChatHistory;
import com.example.springboot_new.ai.service.ChatHistoryService;
import com.example.springboot_new.ai.pojo.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.List;
import java.util.HashMap; // 添加这行

@RestController
@RequestMapping("/api/chat/history")
public class ChatHistoryController {
    
    @Autowired
    private ChatHistoryService chatHistoryService;
    
    @GetMapping
    public ResponseMessage<Map<String, Object>> getChatHistory(@RequestParam Integer accountId, @RequestParam Integer dialogId) {
        try {
            ChatHistory chatHistory = chatHistoryService.getChatHistory(accountId, dialogId);
            Map<String, Object> data = new HashMap<>();
            data.put("accountId", chatHistory.getAccountId());
            data.put("dialogId", chatHistory.getDialogId());
            data.put("conversation", chatHistory.getConversation());
            return new ResponseMessage<>(1, "获取成功", data);
        } catch (Exception e) {
            return new ResponseMessage<>(0, e.getMessage(), null);
        }
    }
    
    @PostMapping
    public ResponseMessage<Map<String, Object>> saveChatHistory(@RequestBody ChatHistory chatHistory) {
        try {
            chatHistoryService.saveChatHistory(chatHistory);
            Map<String, Object> data = new HashMap<>();
            data.put("accountId", chatHistory.getAccountId());
            data.put("dialogId", chatHistory.getDialogId());
            return new ResponseMessage<>(1, "保存成功", data);
        } catch (Exception e) {
            return new ResponseMessage<>(0, e.getMessage(), null);
        }
    }
    
    @DeleteMapping
    public ResponseMessage<Void> deleteChatHistory(@RequestParam Integer accountId, @RequestParam Integer dialogId) {
        try {
            chatHistoryService.deleteChatHistory(accountId, dialogId);
            return new ResponseMessage<>(1, "删除成功", null);
        } catch (Exception e) {
            return new ResponseMessage<>(0, e.getMessage(), null);
        }
    }
}