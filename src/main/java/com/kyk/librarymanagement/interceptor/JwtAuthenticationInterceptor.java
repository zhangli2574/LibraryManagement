package com.kyk.librarymanagement.interceptor;

import com.kyk.librarymanagement.exception.AuthenticationException;
import com.kyk.librarymanagement.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 认证拦截器
 * 校验请求头中的 Token，保护需要认证的接口
 */
@Component
public class JwtAuthenticationInterceptor implements HandlerInterceptor {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头获取 Token
        String authHeader = request.getHeader("Authorization");
        
        // 检查 Authorization 头是否存在
        if (authHeader == null || authHeader.isEmpty()) {
            throw new AuthenticationException("未提供认证Token");
        }
        
        // 提取 Token（格式：Bearer <token>）
        String token;
        if (authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        } else {
            token = authHeader;
        }
        
        // 验证 Token
        try {
            if (!jwtUtil.validateToken(token)) {
                throw new AuthenticationException("Token无效或已过期");
            }
            
            // Token 有效，将用户信息存入 request 属性中供后续使用
            Long userId = jwtUtil.getUserIdFromToken(token);
            request.setAttribute("userId", userId);
            
            return true;
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationException("Token验证失败");
        }
    }
}
