package com.kyk.librarymanagement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "admins")
public class Admin {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "admin_name", nullable = false, length = 50)
    private String adminName;
    
    @Column(name = "admin_phone", nullable = false, unique = true, length = 20)
    private String adminPhone;
    
    @Column(name = "password", nullable = false, length = 255)
    private String password;
    
    @Column(name = "user_role", nullable = false)
    private Integer userRole; // 1-超级管理员, 2-普通管理员
    
    @Column(name = "created_at", columnDefinition = "DATETIME")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
    
    public Admin() {
    }
    
    public Admin(String adminName, String adminPhone, String password, Integer userRole) {
        this.adminName = adminName;
        this.adminPhone = adminPhone;
        this.password = password;
        this.userRole = userRole;
    }
    
    // Getters and Setters
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
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
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
