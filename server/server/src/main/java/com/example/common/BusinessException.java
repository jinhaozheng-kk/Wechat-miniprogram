package com.example.common;

/**
 * 业务异常：用于在 Service 层抛出可向前端展示的错误
 */
public class BusinessException extends RuntimeException {

    /** 业务状态码 */
    private final int code;

    public BusinessException(String message) {
        this(400, message);
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
