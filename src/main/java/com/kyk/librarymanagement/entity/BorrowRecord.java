package com.kyk.librarymanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "borrow_records")
@Schema(description = "借阅记录")
public class BorrowRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "记录ID")
    private Long id;
    
    @Column(name = "book_id", nullable = false)
    @Schema(description = "图书ID")
    private Long bookId;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"password", "createdAt"})
    @Schema(description = "借阅用户")
    private User user;
    
    @Column(name = "borrow_time", nullable = false)
    private LocalDateTime borrowTime;
    
    @Column(name = "return_time")
    private LocalDateTime returnTime;
    
    @PrePersist
    protected void onCreate() {
        if (borrowTime == null) {
            borrowTime = LocalDateTime.now();
        }
    }
    
    public BorrowRecord() {
    }
    
    public BorrowRecord(Long bookId, User user) {
        this.bookId = bookId;
        this.user = user;
        this.borrowTime = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getBookId() {
        return bookId;
    }
    
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public LocalDateTime getBorrowTime() {
        return borrowTime;
    }
    
    public void setBorrowTime(LocalDateTime borrowTime) {
        this.borrowTime = borrowTime;
    }
    
    public LocalDateTime getReturnTime() {
        return returnTime;
    }
    
    public void setReturnTime(LocalDateTime returnTime) {
        this.returnTime = returnTime;
    }
}
