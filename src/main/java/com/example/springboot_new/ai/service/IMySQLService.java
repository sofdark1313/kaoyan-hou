package com.example.springboot_new.ai.service;

import com.example.springboot_new.ai.pojo.DynamicQueryDTO;

import java.util.List;
import java.util.Map;

public interface IMySQLService {

    List<Map<String, Object>> executeDynamicMap(DynamicQueryDTO dto);
}
