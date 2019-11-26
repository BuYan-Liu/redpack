package com.zhenglong.redpack.controller;

import com.alibaba.fastjson.JSON;
import com.zhenglong.redpack.entity.Result;
import com.zhenglong.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lion
 * @描述
 * @date 2019/5/11
 */
//@ControllerAdvice
@RestController
public class ExceptionController {

    private static final String logExceptionFormat = "Exception: Code: %s Detail: %s";

    private static Logger log = LoggerFactory.getLogger(ExceptionController.class);

//    @ExceptionHandler(Exception.class)
    public Result handDefaultException(HttpServletRequest req, Exception e) {
        return exceptionFormat(500,e);
    }

    private <T extends Throwable> Result exceptionFormat(Integer code, T ex) {
        log.error(String.format(logExceptionFormat, code, ex.getMessage()));
        return ResponseUtil.error(ex.getMessage());
    }
}
