package com.example.springboot_new.ai.service;

import com.example.springboot_new.ai.pojo.DynamicQueryDTO;
import com.example.springboot_new.ai.repository.MySQLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MySQLService implements IMySQLService {

    @Autowired
    private MySQLRepository repository;


    @Override
    public List<Map<String, Object>> executeDynamicMap(DynamicQueryDTO dto) {
        return repository.executeDynamicMapQuery(dto.getSql(), dto.getParams());
    }

}
