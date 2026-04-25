package com.kyk.librarymanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("图书馆管理系统 API")
                        .version("1.0")
                        .description("个人图书管理系统的 RESTful API 文档，包含图书管理、用户管理、借阅记录等功能\n\n" +
                                "**认证说明：**\n" +
                                "- 除登录和注册接口外，其他接口都需要在请求头中携带 JWT Token\n" +
                                "- 点击右上方 'Authorize' 按钮，输入 Token 格式：`Bearer <your-token>`\n" +
                                "- Token 有效期为 7 天")
                        .contact(new Contact()
                                .name("开发团队")
                                .email("dev@example.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("本地开发环境")))
                // 添加 JWT Bearer Token 认证
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("请输入 JWT Token，格式：Bearer <token>。例如：Bearer eyJhbGciOiJIUzI1NiJ9...")));
    }
}
