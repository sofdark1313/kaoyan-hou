package com.example.springboot_new.ai;

import com.example.springboot_new.ai.pojo.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.logging.Logger;

@RestControllerAdvice
public class GlobalExceptionHandlerAdvice {
    Logger log = Logger.getLogger(GlobalExceptionHandlerAdvice.class.getName());
    @ExceptionHandler({Exception.class})//什么异常的统一处理
    public ResponseMessage handleException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        log.info(e.getMessage());
        return new ResponseMessage(500,"error",null);
    }
}

