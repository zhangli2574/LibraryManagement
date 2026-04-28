package com.kyk.librarymanagement.controller;

import com.kyk.librarymanagement.dto.ApiResponse;
import com.kyk.librarymanagement.dto.BookRequest;
import com.kyk.librarymanagement.dto.BorrowRequest;
import com.kyk.librarymanagement.dto.BorrowerInfoDTO;
import com.kyk.librarymanagement.dto.PageResponse;
import com.kyk.librarymanagement.dto.PopularBookDTO;
import com.kyk.librarymanagement.entity.Book;
import com.kyk.librarymanagement.entity.BorrowRecord;
import com.kyk.librarymanagement.service.BookService;
import com.kyk.librarymanagement.service.BorrowRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "图书管理", description = "图书的增删改查、借阅、归还等操作")
@RestController
@RequestMapping("/api/books")
public class BookController {
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private BorrowRecordService borrowRecordService;
    
    // 创建图书
    @Operation(summary = "创建图书", description = "添加一本新图书到系统")
    @PostMapping
    public ResponseEntity<ApiResponse<Book>> createBook(@Valid @RequestBody BookRequest bookRequest) {
        Book book = new Book(bookRequest.getTitle(), bookRequest.getAuthor());
        Book createdBook = bookService.createBook(book);
        return ResponseEntity.ok(ApiResponse.success("图书创建成功", createdBook));
    }
    
    // 删除图书
    @Operation(summary = "删除图书", description = "根据ID删除指定图书")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(
            @Parameter(description = "图书ID", required = true, example = "1") 
            @PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok(ApiResponse.success("图书删除成功", null));
    }
    
    // 批量删除图书
    @Operation(summary = "批量删除图书", description = "批量删除多个图书")
    @DeleteMapping("/batch")
    public ResponseEntity<ApiResponse<Void>> batchDeleteBooks(@RequestBody List<Long> ids) {
        bookService.batchDeleteBooks(ids);
        return ResponseEntity.ok(ApiResponse.success("批量删除成功", null));
    }
    
    // 更新图书
    @Operation(summary = "更新图书", description = "根据ID更新图书信息")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Book>> updateBook(
            @Parameter(description = "图书ID", required = true, example = "1") 
            @PathVariable Long id, 
            @Valid @RequestBody BookRequest bookRequest) {
        Book bookDetails = new Book();
        bookDetails.setTitle(bookRequest.getTitle());
        bookDetails.setAuthor(bookRequest.getAuthor());
        Book updatedBook = bookService.updateBook(id, bookDetails);
        return ResponseEntity.ok(ApiResponse.success("图书更新成功", updatedBook));
    }
    
    // 查询所有图书（分页）
    @Operation(summary = "查询所有图书", description = "分页查询图书列表")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<Book>>> getAllBooks(
            @Parameter(description = "页码，从0开始", example = "0") 
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量", example = "10") 
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> bookPage = bookService.getAllBooks(pageable);
        
        PageResponse<Book> response = new PageResponse<>(
                bookPage.getContent(),
                bookPage.getTotalPages(),
                bookPage.getTotalElements()
        );
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    // 查询单个图书
    @Operation(summary = "查询单个图书", description = "根据ID查询图书详情")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Book>> getBookById(
            @Parameter(description = "图书ID", required = true, example = "1") 
            @PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(ApiResponse.success(book));
    }
    
    // 借阅图书
    @Operation(summary = "借阅图书", description = "用户借阅指定图书，需要校验用户存在性和图书状态")
    @PostMapping("/{bookId}/borrow")
    public ResponseEntity<ApiResponse<Book>> borrowBook(
            @Parameter(description = "图书ID", required = true, example = "1") 
            @PathVariable Long bookId, 
            @RequestBody BorrowRequest borrowRequest) {
        Book book = bookService.borrowBook(bookId, borrowRequest);
        return ResponseEntity.ok(ApiResponse.success("借阅成功", book));
    }
    
    // 归还图书
    @Operation(summary = "归还图书", description = "用户归还指定图书，需要校验是借阅者本人")
    @PostMapping("/{bookId}/return")
    public ResponseEntity<ApiResponse<Book>> returnBook(
            @Parameter(description = "图书ID", required = true, example = "1") 
            @PathVariable Long bookId,
            @RequestBody BorrowRequest borrowRequest) {
        Book book = bookService.returnBook(bookId, borrowRequest.getUserId());
        return ResponseEntity.ok(ApiResponse.success("归还成功", book));
    }
    
    // 查询某图书的借阅记录（分页）
    @Operation(summary = "查询图书借阅记录", description = "分页查询指定图书的借阅历史记录")
    @GetMapping("/{bookId}/records")
    public ResponseEntity<ApiResponse<PageResponse<BorrowRecord>>> getBorrowRecords(
            @Parameter(description = "图书ID", required = true, example = "1") 
            @PathVariable Long bookId,
            @Parameter(description = "页码，从0开始", example = "0") 
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量", example = "10") 
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BorrowRecord> recordPage = borrowRecordService.getBorrowRecordsByBookId(bookId, pageable);
        
        PageResponse<BorrowRecord> response = new PageResponse<>(
                recordPage.getContent(),
                recordPage.getTotalPages(),
                recordPage.getTotalElements()
        );
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    // 查询热门图书统计
    @Operation(summary = "查询热门图书", description = "返回借阅次数最多的前 N 本图书，只统计已归还的记录")
    @GetMapping("/stat/popular")
    public ResponseEntity<ApiResponse<List<PopularBookDTO>>> getPopularBooks(
            @Parameter(description = "返回数量，默认10，最大100", example = "3") 
            @RequestParam(defaultValue = "10") int limit) {
        List<PopularBookDTO> popularBooks = bookService.getPopularBooks(limit);
        return ResponseEntity.ok(ApiResponse.success(popularBooks));
    }
    
    // 查询当前借阅人信息
    @Operation(summary = "查询当前借阅人信息", description = "根据图书ID查询当前正在借阅该图书的用户信息（未归还）")
    @GetMapping("/{bookId}/borrower-info")
    public ResponseEntity<ApiResponse<BorrowerInfoDTO>> getBorrowerInfo(
            @Parameter(description = "图书ID", required = true, example = "1") 
            @PathVariable Long bookId) {
        BorrowerInfoDTO borrowerInfo = borrowRecordService.getBorrowerInfoByBookId(bookId);
        return ResponseEntity.ok(ApiResponse.success(borrowerInfo));
    }
}
