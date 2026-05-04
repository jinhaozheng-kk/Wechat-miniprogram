package com.example.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理：所有 Controller 抛出的异常会被这里统一包装成 Result 返回
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** 业务异常：按业务码原样返回 */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusiness(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    /** 兜底异常：500 */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleOthers(Exception e) {
        log.error("服务器内部错误", e);
        return Result.error(500, "服务器内部错误：" + e.getMessage());
    }
}
