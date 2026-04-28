package com.kyk.librarymanagement.dto;

public class AdminLoginResponse {
    
    private Long adminId;
    private String adminName;
    private String adminPhone;
    private Integer userRole;
    private String token;
    
    public AdminLoginResponse() {
    }
    
    public AdminLoginResponse(Long adminId, String adminName, String adminPhone, Integer userRole, String token) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.adminPhone = adminPhone;
        this.userRole = userRole;
        this.token = token;
    }
    
    public Long getAdminId() {
        return adminId;
    }
    
    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }
    
    public String getAdminName() {
        return adminName;
    }
    
    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }
    
    public String getAdminPhone() {
        return adminPhone;
    }
    
    public void setAdminPhone(String adminPhone) {
        this.adminPhone = adminPhone;
    }
    
    public Integer getUserRole() {
        return userRole;
    }
    
    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
}
