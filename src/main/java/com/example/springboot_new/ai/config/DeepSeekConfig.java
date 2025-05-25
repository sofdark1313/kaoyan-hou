package com.example.springboot_new.ai.config;

import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class DeepSeekConfig {

    @Value("${deepseek.api.key}")
    private String apiKey;

    @Value("${deepseek.api.base-url}")
    private String baseUrl;

    @Bean
    public OpenAiService openAiService() {
        return new OpenAiService(apiKey, Duration.ofSeconds(60));
    }
} 