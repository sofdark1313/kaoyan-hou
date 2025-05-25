package com.example.springboot_new.ai.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class MySQLRepositoryImpl implements MySQLRepository {
    @PersistenceContext
    private EntityManager entityManager;


    //  动态查询
    @Override
    public List<Map<String, Object>> executeDynamicMapQuery(String sql, Object[] params) {
        Query nativeQuery = (Query) entityManager.createNativeQuery(sql);

        String safePattern = "^\\s*SELECT\\b.*$";

        if (!sql.matches(safePattern)) {
            throw new IllegalArgumentException("非法操作");
        }

        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                nativeQuery.setParameter(i + 1, params[i]);
            }
        }

        return nativeQuery
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();
    }
}
