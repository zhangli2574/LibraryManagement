package com.kyk.librarymanagement.dto;

import jakarta.validation.constraints.NotBlank;

public class AdminLoginRequest {
    
    @NotBlank(message = "手机号不能为空")
    private String adminPhone;
    
    @NotBlank(message = "密码不能为空")
    private String password;
    
    public AdminLoginRequest() {
    }
    
    public AdminLoginRequest(String adminPhone, String password) {
        this.adminPhone = adminPhone;
        this.password = password;
    }
    
    public String getAdminPhone() {
        return adminPhone;
    }
    
    public void setAdminPhone(String adminPhone) {
        this.adminPhone = adminPhone;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
