package com.example.springboot_new.ai.controller;

import com.example.springboot_new.ai.pojo.ChatReply;
import com.example.springboot_new.ai.service.DeepSeekService;
import com.example.springboot_new.ai.pojo.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.springboot_new.ai.pojo.ChatRequest;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class DeepSeekController {

    private final DeepSeekService deepSeekService;

    @PostMapping("/chat")
    public ResponseMessage<String> chat(@RequestBody ChatRequest request) {
        String result = deepSeekService.chat(request.getMessage());
        return ResponseMessage.success(result);
    }

    @PostMapping("/chat-query")
    public ResponseMessage<ChatReply> chatAndQuery(@RequestBody ChatRequest request) {
        ChatReply result = deepSeekService.chatAndQuery(request.getMessage());
        return ResponseMessage.success(result);
    }

    @PostMapping("/chat-final")
    public ResponseMessage<String> chatAndFinal(@RequestBody ChatRequest request) {
        String result = deepSeekService.chatAndFinal(request.getMessage());
        return ResponseMessage.success(result);
    }
}