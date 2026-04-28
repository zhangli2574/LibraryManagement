package com.kyk.librarymanagement.interceptor;

import com.kyk.librarymanagement.exception.AuthenticationException;
import com.kyk.librarymanagement.repository.AdminRepository;
import com.kyk.librarymanagement.repository.UserRepository;
import com.kyk.librarymanagement.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 认证拦截器
 * 校验请求头中的 Token，保护需要认证的接口
 * 每次请求都会验证用户/管理员是否存在，如果被删除则拒绝访问
 */
@Component
public class JwtAuthenticationInterceptor implements HandlerInterceptor {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AdminRepository adminRepository;
    
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
            
            // Token 有效，获取用户ID和用户类型
            Long userId = jwtUtil.getUserIdFromToken(token);
            String userType = jwtUtil.getUserTypeFromToken(token);
            
            // 根据用户类型校验对应的表
            if ("USER".equals(userType)) {
                // 普通用户：检查 users 表
                if (!userRepository.existsById(userId)) {
                    throw new AuthenticationException("用户不存在或已被删除");
                }
            } else if ("ADMIN".equals(userType)) {
                // 管理员：检查 admins 表
                if (!adminRepository.existsById(userId)) {
                    throw new AuthenticationException("管理员不存在或已被删除");
                }
            } else {
                // 未知类型
                throw new AuthenticationException("无效的用户类型");
            }
            
            // 将用户信息存入 request 属性中供后续使用
            request.setAttribute("userId", userId);
            
            return true;
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationException("Token验证失败");
        }
    }
}
