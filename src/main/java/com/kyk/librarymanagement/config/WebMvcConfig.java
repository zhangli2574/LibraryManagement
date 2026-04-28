package com.kyk.librarymanagement.config;

import com.kyk.librarymanagement.interceptor.JwtAuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置类
 * 注册 JWT 认证拦截器
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    @Autowired
    private JwtAuthenticationInterceptor jwtAuthenticationInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthenticationInterceptor)
                .addPathPatterns("/api/**")  // 拦截所有 /api/ 开头的接口
                .excludePathPatterns(
                        "/api/users/login",      // 放行登录接口
                        "/api/users/register",   // 放行注册接口
                        "/api/admin/login",      // 放行管理员登录接口
                        "/swagger-ui/**",        // 放行 Swagger UI
                        "/swagger-ui.html",      // 放行 Swagger UI 主页
                        "/v3/api-docs/**",       // 放行 API 文档
                        "/webjars/**"            // 放行 Swagger 静态资源
                );
    }
}
