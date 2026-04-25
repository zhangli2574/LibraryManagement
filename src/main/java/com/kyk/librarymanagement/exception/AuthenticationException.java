package com.kyk.librarymanagement.exception;

/**
 * 认证异常
 * 当 Token 无效、过期或缺失时抛出
 */
public class AuthenticationException extends RuntimeException {
    
    public AuthenticationException(String message) {
        super(message);
    }
    
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
