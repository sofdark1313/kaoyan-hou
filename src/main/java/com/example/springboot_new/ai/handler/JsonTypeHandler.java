package com.example.springboot_new.ai.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@MappedTypes(List.class)
public class JsonTypeHandler extends BaseTypeHandler<List<Map<String, String>>> {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Map<String, String>> parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, toJson(parameter));
    }

    @Override
    public List<Map<String, String>> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return fromJson(rs.getString(columnName));
    }

    @Override
    public List<Map<String, String>> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return fromJson(rs.getString(columnIndex));
    }

    @Override
    public List<Map<String, String>> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return fromJson(cs.getString(columnIndex));
    }

    private String toJson(List<Map<String, String>> params) throws SQLException {
        try {
            return MAPPER.writeValueAsString(params);
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    private List<Map<String, String>> fromJson(String json) throws SQLException {
        try {
            if (json == null) {
                return null;
            }
            return MAPPER.readValue(json, new TypeReference<List<Map<String, String>>>() {});
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }
}