package com.example.springboot_new.ai.service;

import com.example.springboot_new.ai.pojo.ChatReply;
import com.example.springboot_new.ai.service.MilvusPythonService;
import jakarta.persistence.NamedNativeQuery;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;
import java.util.regex.Matcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Service
public class DeepSeekService {
    private final MilvusPythonService queryService;
    public DeepSeekService(MilvusPythonService queryService) {
        this.queryService = queryService;
    }

    private static final String PROMPT_PREFIX = "1.你是一个将自然语言转化成SQL查询语句的助手，你只能写查询相关语句，不能够写插入，修改，更新语句。请只返回如下JSON对象和后续对用户的文字建议，不要输出任何解释、代码块或多余内容：\\n\\n{\\n  \"sql\": \"SELECT * FROM (SELECT *,ROW_NUMBER() OVER (PARTITION BY 学位授予门类 ORDER BY 序号) AS rn FROM 全国专业信息) t WHERE rn = 1\",\\n  \"params\": []\\n}\\n;" +
            "2.你面向的是有考研意向的本科生，你需要理解他们的话语到底是什么需求，推断他们可能需要的数据是什么，为了确保生成的查询语句正确，你要尽可能确保你所查询的表的属性列全部返回，除非用户明确要求了只能返回哪些属性列" +
            "3.目前，数据库中一共有五个表，分别是'25考408的院校专业','2023年初试分数线','2024年初试分数线','2025年初试分数线','各院校计算机科学与技术专业的初试_复试_加试内容'。你需要根据用户的需求对这些表格进行灵活多样的查询，包括单表查询，多表查询等\n"+
            "4.查询语句要使用limit限制规模，最多返回100组数据,返回的数据需要按照2025年初试分数线从小到大排序,这是硬性要求，所以你的任何查询都要关联到2025年初试分数线表,除非没法关联;" +
            "5.你需要非常明确的知道某个表的某个属性的列名的确定名称，必须完全基于我的提示词，不能够进行任何的推断假设" +
            "25考408的院校专业表的各个属性列如下:" +
            "学校 varchar(255)," +
            "学院 varchar(255)," +
            "专业代码 varchar(255), ##教育部规定的某专业的唯一标识" +
            "专业名称 varchar(255)," +
            "学习方式 varchar(255) ##包括全日制、非全日制;" +
            "2023年初始分数线表的各个属性列如下:" +
            "学校名称 varchar(255)," +
            "院系名称 varchar(255)," +
            "专业代码 varchar(255), ##教育部规定的某专业的唯一标识" +
            "专业名称 varchar(255)," +
            "总分 int, ##总分最低过线要求" +
            "政治 int, ##政治这门课的分数最低要求（满分100分）" +
            "外语 int, ##外语这门课的分数最低要求（满分100分）" +
            "专业课1 int, ##第一门专业课的分数最低要求（满分150分）" +
            "专业课2 int, ##第二门专业课的分数最低要求（满分150分）" +
            "2024年初始分数线表的各个属性列如下:" +
            "学校名称 varchar(255)," +
            "院系名称 varchar(255)," +
            "专业代码 varchar(255), ##教育部规定的某专业的唯一标识" +
            "专业名称 varchar(255)," +
            "总分 int, ##总分最低过线要求" +
            "政治 int, ##政治这门课的分数最低要求（满分100分）" +
            "外语 int, ##外语这门课的分数最低要求（满分100分）" +
            "专业课1 int, ##第一门专业课的分数最低要求（满分150分）" +
            "专业课2 int, ##第二门专业课的分数最低要求（满分150分）" +
            "2025年初始分数线表的各个属性列如下:" +
            "学校名称 varchar(255)," +
            "院系名称 varchar(255)," +
            "专业代码 varchar(255), ##教育部规定的某专业的唯一标识" +
            "专业名称 varchar(255)," +
            "总分 int, ##总分最低过线要求" +
            "政治 int, ##政治这门课的分数最低要求（满分100分）" +
            "外语 int, ##外语这门课的分数最低要求（满分100分）" +
            "专业课1 int, ##第一门专业课的分数最低要求（满分150分）" +
            "专业课2 int, ##第二门专业课的分数最低要求（满分150分）" +
            "各院校计算机科学与技术的初试_复试_加试内容表的各个属性列如下:" +
            "学校名 varchar(255)," +
            "初试科目 varchar(255), ##示例：①101 思想政治理论、②201 英语一、③301 数学一、④906 计算机专业基础" +
            "复试科目 varchar(255), ##示例：数据结构、专业基础知识、领域知识" +
            "同等学力加试科目 varchar(255) ##是指的没有本科学历但是有同等学力（如专科）的学生需要额外考核的科目；" +
            "高校基本数据表的各个属性列如下:" +
            "省份 varchar(255), ##示例：江苏、浙江" +
            "大学名称 varchar(255)," +
            "公办/民办 varchar(255), ##这一列的值为1表示公办，为0表示民办," +
            "985 tinyint(1), ##这一列的值为1表示是985，为0表示不是985," +
            "211 tinyint(1), ##这一列的值为1表示是211，为0表示不是211," +
            "双一流 tinyint(1), ##这一列的值为1表示是双一流，为0表示不是双一流," +
            "城市 varchar(255), ##示例：北京市、上海市" +
            "从属单位 varchar(255) ##示例：教育部、工信部、浙江省教育厅、浙江省" +
            "6.如果难以推断用户的诉求或者用户需要的查询超出了我的表能够支持的范围，你一律生成' select_experience_create.py * from 高校基本数据 where 1>2 '这个查询语句" +
            "7.我的表的列名是纯汉字，你不要加引号" +
            "8.你每生成一次json格式的查询语句，都要在后面紧跟对用户的文字建议，如果用户的查询意义不明，你可以引导用户应该如何请求，如果用户的查询很明确，你就说明一下你返回的内容是什么;" +
            "9.某些表的某些属性列名是数字，为了防止歧义，要写成`985`的样式";
    private static final String PROMPT_PREFIX_Judge = "请根据用户输入的请求判断用户是需要备考建议还是择校建议，还是两者都有，还是两者都没有，根据具体情况返回相应的数字，返回结果只能是单个数字，不能包含其它任何内容:" +
            "1.如果用户什么都不需要，就返回'0';" +
            "2.如果用户需要备考建议不需要择校建议，就返回'1';" +
            "3.如果用户需要择校建议不需要备考建议，就返回'2';" +
            "4.如果用户都需要，就返回'3';";
    private static final String PROMPT_PREFIX_Final = "根据用户输入信息和数据库查询结果，生成专业的回复，在向用户推荐院校和专业时，需要充分利用知识库中检索到的相关知识，并模仿知识库中的推荐逻辑，并设法进一步了解用户的具体需求，根据用户的具体需求给出更细化后的答案";

    @Value("${deepseek.api.key}")
    private String apiKey;

    @Value("${deepseek.api.base-url}")
    private String baseUrl;

    public String chat(List<Map<String, String>> messages) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = baseUrl + "/chat/completions";

            // 构造请求体
            Map<String, Object> body = new HashMap<>();
            body.put("model", "deepseek-chat");

            List<Map<String, String>> apiMessages = new ArrayList<>();

            // 添加系统提示词
            Map<String, String> systemMsg = new HashMap<>();
            systemMsg.put("role", "system");
            systemMsg.put("content", PROMPT_PREFIX);
            apiMessages.add(systemMsg);

            // 添加历史消息和当前消息
            apiMessages.addAll(messages);

            body.put("messages", apiMessages);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

            Map respBody = response.getBody();
            if (respBody != null && respBody.containsKey("choices")) {
                List choices = (List) respBody.get("choices");
                if (!choices.isEmpty()) {
                    Map choice = (Map) choices.get(0);
                    Map message = (Map) choice.get("message");
                    return (String) message.get("content");
                }
            }
            return "未获取到AI回复内容";
        } catch (Exception e) {
            log.error("调用DeepSeek API出错", e);
            return "抱歉，处理您的请求时出现错误：" + e.getMessage();
        }
    }

    public String chat_judge(List<Map<String, String>> messages) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = baseUrl + "/chat/completions";

            // 构造请求体
            Map<String, Object> body = new HashMap<>();
            body.put("model", "deepseek-chat");

            List<Map<String, String>> apiMessages = new ArrayList<>();

            // 添加系统提示词
            Map<String, String> systemMsg = new HashMap<>();
            systemMsg.put("role", "system");
            systemMsg.put("content", PROMPT_PREFIX_Judge);
            apiMessages.add(systemMsg);

            // 添加历史消息和当前消息，确保每条消息都有role字段
            for (Map<String, String> msg : messages) {
                if (!msg.containsKey("role")) {
                    msg.put("role", "user");  // 如果没有role字段，默认为user
                }
                apiMessages.add(msg);
            }

            body.put("messages", apiMessages);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

            Map respBody = response.getBody();
            if (respBody != null && respBody.containsKey("choices")) {
                List choices = (List) respBody.get("choices");
                if (!choices.isEmpty()) {
                    Map choice = (Map) choices.get(0);
                    Map message = (Map) choice.get("message");
                    return (String) message.get("content");
                }
            }
            return "-1";
        } catch (Exception e) {
            log.error("调用DeepSeek API出错", e);
            return "-2";
        }
    }

    public ChatReply chatAndQuery(List<Map<String, String>> messages) {
        String aiResult = chat(messages);
        String aiJudge = chat_judge(messages);
        List<String> queryList1 = new ArrayList<>();
        List<String> queryList2 = new ArrayList<>();

        // 获取最后一条用户消息
        String lastUserMessage = "";
        for (int i = messages.size() - 1; i >= 0; i--) {
            Map<String, String> msg = messages.get(i);
            if ("user".equals(msg.get("role"))) {
                lastUserMessage = msg.get("content");
                break;
            }
        }

        switch (aiJudge) {
            case "0":
                System.out.println("没有需要处理的信息");
                break;
            case "1":
                queryList1 = queryService.callPythonScript(lastUserMessage,"prepare_experience");
                System.out.println(queryList1);
                break;
            case "2":
                queryList2 = queryService.callPythonScript(lastUserMessage,"select_experience");
                System.out.println(queryList2);
                break;
            case "3":
                queryList1 = queryService.callPythonScript(lastUserMessage,"prepare_experience");
                queryList2 = queryService.callPythonScript(lastUserMessage,"select_experience");
                System.out.println(queryList1);
                System.out.println(queryList2);
                break;
            default:
                break;
        }

        log.info("AI原始回复内容: {}", aiResult);

        try {
            // 清理AI回复内容，提取第一个JSON对象
            String cleaned = aiResult.replaceAll("(?s).*?\\{", "{").replaceAll("}.*", "}");
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(cleaned);
            String sql = node.get("sql").asText();
            log.info("AI生成的SQL语句: {}", sql);

            JsonNode paramsNode = node.get("params");
            List<Object> params = new ArrayList<>();
            if (paramsNode != null && paramsNode.isArray()) {
                for (JsonNode p : paramsNode) {
                    params.add(p.isTextual() ? p.asText() : p.toString());
                }
            }

            // 调用数据库查询接口
            RestTemplate restTemplate = new RestTemplate();
            String queryUrl = "http://localhost:8088/user/api/score/dynamic";
            Map<String, Object> queryBody = new HashMap<>();
            queryBody.put("sql", sql);
            queryBody.put("params", params);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(queryBody, headers);

            ResponseEntity<Object> response = restTemplate.postForEntity(queryUrl, entity, Object.class);

            // 封装为 ChatReply 返回
            ChatReply chatReply = new ChatReply();
            chatReply.setMessage(messages);
            chatReply.setResult1(response.getBody());
            chatReply.setResult2(queryList1);
            chatReply.setResult3(queryList2);

            return chatReply;

        } catch (Exception e) {
            log.error("AI SQL解析或数据库查询出错", e);
            ChatReply chatReply = new ChatReply();
            chatReply.setMessage(messages);
            chatReply.setResult1("");
            return chatReply;
        }
    }

    public String chatAndFinal(List<Map<String, String>> messages) {
        ChatReply chatReply = chatAndQuery(messages);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String chatReplyJson = objectMapper.writeValueAsString(chatReply);

            // 获取最后一条用户消息
            String lastUserMessage = "";
            for (int i = messages.size() - 1; i >= 0; i--) {
                Map<String, String> msg = messages.get(i);
                if ("user".equals(msg.get("role"))) {
                    lastUserMessage = msg.get("content");
                    break;
                }
            }

            String finalPrompt = "以下是用户的请求和数据库查询结果：\n" + chatReplyJson + "\n请根据以上信息生成专业回复。";

            RestTemplate restTemplate = new RestTemplate();
            String url = baseUrl + "/chat/completions";

            // 构造请求体
            Map<String, Object> body = new HashMap<>();
            body.put("model", "deepseek-chat");

            List<Map<String, String>> apiMessages = new ArrayList<>();

            // 添加系统提示词
            Map<String, String> systemMsg = new HashMap<>();
            systemMsg.put("role", "system");
            systemMsg.put("content", PROMPT_PREFIX_Final);
            apiMessages.add(systemMsg);

            // 添加历史消息
            apiMessages.addAll(messages);

            // 添加最终提示
            Map<String, String> finalMsg = new HashMap<>();
            finalMsg.put("role", "user");
            finalMsg.put("content", finalPrompt);
            apiMessages.add(finalMsg);

            body.put("messages", apiMessages);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

            if (response.getBody() != null && response.getBody().containsKey("choices")) {
                List<?> choices = (List<?>) response.getBody().get("choices");
                if (!choices.isEmpty()) {
                    Map<?, ?> choice = (Map<?, ?>) choices.get(0);
                    Map<?, ?> message = (Map<?, ?>) choice.get("message");
                    return (String) message.get("content");
                }
            }

            return "未获取到AI回复内容";
        } catch (Exception e) {
            log.error("调用DeepSeek API出错", e);
            return "抱歉，处理您的请求时出现错误：" + e.getMessage();
        }
    }

}