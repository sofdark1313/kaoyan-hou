package com.example.springboot_new.ai.pojo;

import org.springframework.http.HttpStatus;

public class ResponseMessage<T> {
    private int code;
    private String msg;
    private T data;
    public ResponseMessage(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    //接口请求成功
    public static <T>ResponseMessage<T> success(T data) {
        return new ResponseMessage<T>(HttpStatus.OK.value(), "success!", data);
    }
    public Integer getCode(){
        return code;
    }
    public String getMsg(){
        return msg;
    }
    public T getData(){
        return data;
    }
    public void setCode(int code){
        this.code = code;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public void setData(T data){
        this.data = data;
    }
}
