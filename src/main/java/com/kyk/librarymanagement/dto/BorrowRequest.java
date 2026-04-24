package com.kyk.librarymanagement.dto;

public class BorrowRequest {
    
    private Long userId;
    
    public BorrowRequest() {
    }
    
    public BorrowRequest(Long userId) {
        this.userId = userId;
    }
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
