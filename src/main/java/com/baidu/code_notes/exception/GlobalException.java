package com.baidu.code_notes.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author lxh
 * @date 2022/3/6 20:48
 */
@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String exception(Exception exception) {
        exception.printStackTrace();
        System.out.println(exception.getMessage());
        return exception.getMessage();
    }
}
