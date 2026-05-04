package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 配置
 * <p>
 * 当前阶段采用最简方案：
 * <ul>
 *   <li>关闭 CSRF（前后端分离，使用 token 即可）</li>
 *   <li>放行所有请求，认证由我们自己的 {@link AuthInterceptor} 在 MVC 层完成</li>
 * </ul>
 * 后续接入完整用户体系时再细化。
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {}) // 沿用 WebMvcConfig 中的 CORS 配置
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                // 关闭默认表单登录与 HTTP Basic
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());
        return http.build();
    }
}
