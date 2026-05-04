package com.example.service;

import com.example.common.BusinessException;
import com.example.config.HuaxinProperties;
import com.example.dto.LoginRequest;
import com.example.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 后台登录业务（当前阶段：硬编码账号）
 */
@Service
public class AuthService {

    private final HuaxinProperties properties;

    @Autowired
    public AuthService(HuaxinProperties properties) {
        this.properties = properties;
    }

    /** 登录：账号密码与配置一致即返回固定 token */
    public LoginResponse login(LoginRequest req) {
        if (req == null || req.getUsername() == null || req.getPassword() == null) {
            throw new BusinessException("账号或密码不能为空");
        }
        HuaxinProperties.Admin admin = properties.getAdmin();
        if (!admin.getUsername().equals(req.getUsername())
                || !admin.getPassword().equals(req.getPassword())) {
            throw new BusinessException(401, "账号或密码错误");
        }
        return new LoginResponse(admin.getToken(), admin.getUsername());
    }
}
