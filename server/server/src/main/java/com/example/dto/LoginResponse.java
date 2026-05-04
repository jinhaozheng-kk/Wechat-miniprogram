package com.example.dto;

/**
 * 登录响应体
 */
public class LoginResponse {

    /** 登录后用于鉴权的 token，前端放在请求头 X-Auth-Token */
    private String token;
    /** 显示用的用户名 */
    private String username;

    public LoginResponse() {}

    public LoginResponse(String token, String username) {
        this.token = token;
        this.username = username;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
