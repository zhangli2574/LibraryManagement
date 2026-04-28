package com.kyk.librarymanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "借阅人信息")
public class BorrowerInfoDTO {
    
    @Schema(description = "借阅记录ID")
    private Long borrowId;
    
    @Schema(description = "图书ID")
    private Long bookId;
    
    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "用户手机号")
    private String phone;
    
    @Schema(description = "用户姓名")
    private String name;
    
    public BorrowerInfoDTO() {
    }
    
    public BorrowerInfoDTO(Long borrowId, Long bookId, Long userId, String phone, String name) {
        this.borrowId = borrowId;
        this.bookId = bookId;
        this.userId = userId;
        this.phone = phone;
        this.name = name;
    }
    
    // Getters and Setters
    public Long getBorrowId() {
        return borrowId;
    }
    
    public void setBorrowId(Long borrowId) {
        this.borrowId = borrowId;
    }
    
    public Long getBookId() {
        return bookId;
    }
    
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
