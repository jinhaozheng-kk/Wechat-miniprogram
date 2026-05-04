package com.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 业务自定义配置
 * <p>
 * 对应 application.yml 中 huaxin.* 节点
 */
@Component
@ConfigurationProperties(prefix = "huaxin")
public class HuaxinProperties {

    /** 上传文件存放目录 */
    private String uploadDir;

    /** 上传文件对外访问的 URL 前缀 */
    private String uploadUrlPrefix;

    /** 后台管理员信息 */
    private Admin admin = new Admin();

    public static class Admin {
        /** 管理员用户名 */
        private String username;
        /** 管理员密码 */
        private String password;
        /** 登录后返回的固定 token */
        private String token;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }

    public String getUploadDir() { return uploadDir; }
    public void setUploadDir(String uploadDir) { this.uploadDir = uploadDir; }
    public String getUploadUrlPrefix() { return uploadUrlPrefix; }
    public void setUploadUrlPrefix(String uploadUrlPrefix) { this.uploadUrlPrefix = uploadUrlPrefix; }
    public Admin getAdmin() { return admin; }
    public void setAdmin(Admin admin) { this.admin = admin; }
}
