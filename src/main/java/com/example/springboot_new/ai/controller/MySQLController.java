package com.example.springboot_new.ai.controller;

import com.example.springboot_new.ai.pojo.ResponseMessage;
import com.example.springboot_new.ai.pojo.DynamicQueryDTO;
import com.example.springboot_new.ai.service.MySQLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("user/api/score")
public class MySQLController {

    @Autowired
    private MySQLService service;

    @PostMapping("/dynamic")
    public ResponseMessage<List<Map<String, Object>>> dynamicFieldQuery(@RequestBody DynamicQueryDTO dto) {
        List<Map<String, Object>> result = service.executeDynamicMap(dto);
        return ResponseMessage.success(result);
    }

}