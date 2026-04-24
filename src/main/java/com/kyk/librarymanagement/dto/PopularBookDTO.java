package com.kyk.librarymanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "热门图书统计")
public class PopularBookDTO {
    
    @Schema(description = "图书ID")
    private Long id;
    
    @Schema(description = "图书标题")
    private String title;
    
    @Schema(description = "借阅次数")
    private Integer borrowCount;
    
    public PopularBookDTO() {
    }
    
    public PopularBookDTO(Long id, String title, Integer borrowCount) {
        this.id = id;
        this.title = title;
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
    
    public Integer getBorrowCount() {
        return borrowCount;
    }
    
    public void setBorrowCount(Integer borrowCount) {
        this.borrowCount = borrowCount;
    }
}
