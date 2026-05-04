package com.example.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Web MVC 配置：
 * <ul>
 *   <li>CORS 跨域：放行 Vue 前端与小程序</li>
 *   <li>静态资源映射：把上传目录暴露为 /upload/**</li>
 *   <li>注册管理后台 token 拦截器</li>
 * </ul>
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebMvcConfig.class);

    private final HuaxinProperties properties;
    private final AuthInterceptor authInterceptor;

    @Autowired
    public WebMvcConfig(HuaxinProperties properties, AuthInterceptor authInterceptor) {
        this.properties = properties;
        this.authInterceptor = authInterceptor;
    }

    /** 启动时确保上传目录存在 */
    @PostConstruct
    public void ensureUploadDir() throws IOException {
        Path dir = Paths.get(properties.getUploadDir()).toAbsolutePath().normalize();
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        log.info("上传目录：{}", dir);
    }

    /** 跨域配置：开发期放行所有来源 */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("X-Auth-Token")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /** 把 upload 目录映射成 HTTP 可访问的静态资源 */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String absDir = Paths.get(properties.getUploadDir()).toAbsolutePath().normalize().toString();
        // Windows 下需要前缀 file:/// + 末尾的分隔符
        String location = "file:" + absDir + File.separator;
        registry.addResourceHandler(properties.getUploadUrlPrefix() + "/**")
                .addResourceLocations(location);
    }

    /** 注册管理后台 token 拦截器：拦截 /api/admin/** 但放行登录接口 */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/admin/**")
                .excludePathPatterns("/api/admin/login");
    }
}
