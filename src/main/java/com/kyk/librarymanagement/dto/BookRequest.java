package com.kyk.librarymanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BookRequest {
    
    @NotBlank(message = "title不能为空")
    @Size(min = 1, max = 100, message = "title长度必须在1-100字符之间")
    private String title;
    
    @NotBlank(message = "author不能为空")
    @Size(min = 1, max = 50, message = "author长度必须在1-50字符之间")
    private String author;
    
    public BookRequest() {
    }
    
    public BookRequest(String title, String author) {
        this.title = title;
        this.author = author;
    }
    
    // Getters and Setters
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
}
