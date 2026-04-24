package com.kyk.librarymanagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    
    @Value("${jwt.secret:mySecretKeyForLibraryManagementSystem2024}")
    private String secret;
    
    @Value("${jwt.expiration:86400000}")
    private Long expiration; // 默认24小时（毫秒）
    
    public String getSecret() {
        return secret;
    }
    
    public Long getExpiration() {
        return expiration;
    }
}
