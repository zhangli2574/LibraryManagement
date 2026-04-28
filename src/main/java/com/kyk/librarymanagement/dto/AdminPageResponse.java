package com.kyk.librarymanagement.dto;

import java.time.LocalDateTime;

public class AdminPageResponse {
    
    private Long id;
    private String adminName;
    private String adminPhone;
    private Integer userRole; // 1-超级管理员, 2-普通管理员
    private LocalDateTime createdAt;
    
    public AdminPageResponse() {
    }
    
    public AdminPageResponse(Long id, String adminName, String adminPhone, Integer userRole, LocalDateTime createdAt) {
        this.id = id;
        this.adminName = adminName;
        this.adminPhone = adminPhone;
        this.userRole = userRole;
        this.createdAt = createdAt;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
