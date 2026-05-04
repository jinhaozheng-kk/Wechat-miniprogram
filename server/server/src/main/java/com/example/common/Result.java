package com.example.common;

/**
 * 统一接口返回结构
 * <p>
 * 前端约定：
 * - code = 0     表示成功
 * - code != 0    表示业务/系统错误，message 给出可读信息
 * - data         成功时的业务数据，失败时为 null
 */
public class Result<T> {

    /** 业务状态码 */
    private int code;
    /** 提示信息 */
    private String message;
    /** 业务数据 */
    private T data;

    public Result() {}

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /** 成功返回（无数据） */
    public static <T> Result<T> ok() {
        return new Result<>(0, "ok", null);
    }

    /** 成功返回（带数据） */
    public static <T> Result<T> ok(T data) {
        return new Result<>(0, "ok", data);
    }

    /** 失败返回 */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    /** 失败返回（默认 500） */
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
