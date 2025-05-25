package com.example.springboot_new.ai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class MilvusPythonService {

    private static final Logger log = LoggerFactory.getLogger(MilvusPythonService.class);

    public List<String> callPythonScript(String question,  String collectionName) {
        try {
            // 使用命令行参数方式传递 question
            ProcessBuilder pb = new ProcessBuilder(
                    "/home/constina/anaconda3/envs/llm_linux/bin/python",
                    "/home/constina/IdeaProject/springboot_new/src/main/java/com/example/springboot_new/ai/Rag/search.py",
                    question,
                    collectionName
            );

            Process process = pb.start();

            // 捕获标准输出
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            // 捕获错误输出（关键！）
            BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream())
            );

            String line;
            StringBuilder result = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                result.append(line);
                log.info("Python stdout: {}", line); // 打印每一行输出
            }

            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                log.error("Python stderr: {}", errorLine); // 打印错误日志
            }

            boolean exited = process.waitFor(10, TimeUnit.SECONDS);
            if (!exited) {
                process.destroyForcibly();
                log.warn("Python script execution timed out");
                return Collections.emptyList();
            }

            int exitCode = process.exitValue();
            if (exitCode == 0 && result.length() > 0) {
                ObjectMapper mapper = new ObjectMapper();
                return Arrays.asList(mapper.readValue(result.toString(), String[].class));
            } else {
                log.warn("Python script returned error or empty result: {}", result);
                return Collections.emptyList();
            }
        } catch (Exception e) {
            log.error("执行 Python 脚本失败", e);
            return Collections.emptyList();
        }
    }
}
