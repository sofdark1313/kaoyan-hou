package com.example.springboot_new.ai.service;

import com.example.springboot_new.ai.mapper.ChatHistoryMapper;
import com.example.springboot_new.ai.pojo.ChatHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatHistoryService {
    
    @Autowired
    private ChatHistoryMapper chatHistoryMapper;
    
    public ChatHistory getChatHistory(Integer accountId, Integer dialogId) {
        return chatHistoryMapper.findByAccountIdAndDialogId(accountId, dialogId);
    }
    
    public void saveChatHistory(ChatHistory chatHistory) {
        ChatHistory existing = chatHistoryMapper.findByAccountIdAndDialogId(chatHistory.getAccountId(), chatHistory.getDialogId());
        if (existing != null) {
            chatHistoryMapper.update(chatHistory);
        } else {
            chatHistoryMapper.insert(chatHistory);
        }
    }
    
    public void deleteChatHistory(Integer accountId, Integer dialogId) {
        chatHistoryMapper.delete(accountId, dialogId);
    }
} 