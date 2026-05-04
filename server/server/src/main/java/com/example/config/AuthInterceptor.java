package com.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;

/**
 * 后台管理接口 token 校验拦截器
 * <p>
 * 拦截 /api/admin/** 下的请求（登录接口除外，由 WebMvcConfig 中显式排除），
 * 校验请求头 X-Auth-Token 是否等于配置中的固定 token。
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final HuaxinProperties properties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public AuthInterceptor(HuaxinProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = request.getHeader("X-Auth-Token");
        if (token != null && token.equals(properties.getAdmin().getToken())) {
            return true;
        }
        // 未带 token 或 token 错误：返回 401
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        Result<Void> body = Result.error(401, "未登录或登录已失效");
        response.getWriter().write(objectMapper.writeValueAsString(body));
        return false;
    }
}
