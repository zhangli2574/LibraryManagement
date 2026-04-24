package com.kyk.librarymanagement.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    
    @NotBlank(message = "phone不能为空")
    private String phone;
    
    @NotBlank(message = "password不能为空")
    private String password;
    
    public LoginRequest() {
    }
    
    public LoginRequest(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }
    
    // Getters and Setters
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
