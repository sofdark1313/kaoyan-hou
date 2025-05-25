package com.example.springboot_new.ai.repository;


import java.util.List;
import java.util.Map;

public interface MySQLRepository {


    // 新增方法：返回 Map 类型的结果
    List<Map<String, Object>> executeDynamicMapQuery(String sql, Object[] params);

}
