package com.example.springboot_new.ai.pojo;

public class DynamicQueryDTO {
    private String sql;      // SQL语句（仅允许SELECT）
    private Object[] params; // 参数列表（防止SQL注入）

    public DynamicQueryDTO(String sql, Object[] params) {
        this.sql = sql;
        this.params = params;
    }
    public DynamicQueryDTO() {
        this.sql = "";
        this.params = new Object[0];
    }
    public String getSql() {
        return sql;
    }
    public void setSql(String sql) {
        this.sql = sql;
    }
    public Object[] getParams() {
        return params;
    }
    public void setParams(Object[] params) {
        this.params = params;
    }
}
