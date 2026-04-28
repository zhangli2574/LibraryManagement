package com.kyk.librarymanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "热门图书统计")
public class PopularBookDTO {
    
    @Schema(description = "图书ID")
    private Long id;
    
    @Schema(description = "图书标题")
    private String title;
    
    @Schema(description = "作者")
    private String author;
    
    @Schema(description = "是否被借阅")
    private Boolean isBorrowed;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
    
    @Schema(description = "借阅次数")
    private Integer borrowCount;
    
    public PopularBookDTO() {
    }
    
    public PopularBookDTO(Long id, String title, Integer borrowCount) {
        this.id = id;
        this.title = title;
        this.borrowCount = borrowCount;
    }
    
    public PopularBookDTO(Long id, String title, String author, Boolean isBorrowed,
                          LocalDateTime createdAt, LocalDateTime updatedAt, Integer borrowCount) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isBorrowed = isBorrowed;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.borrowCount = borrowCount;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public Boolean getIsBorrowed() {
        return isBorrowed;
    }
    
    public void setIsBorrowed(Boolean isBorrowed) {
        this.isBorrowed = isBorrowed;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Integer getBorrowCount() {
        return borrowCount;
    }
    
    public void setBorrowCount(Integer borrowCount) {
        this.borrowCount = borrowCount;
    }
}
