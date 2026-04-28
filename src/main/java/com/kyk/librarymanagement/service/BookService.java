package com.kyk.librarymanagement.service;

import com.kyk.librarymanagement.dto.BorrowRequest;
import com.kyk.librarymanagement.dto.PopularBookDTO;
import com.kyk.librarymanagement.entity.Book;
import com.kyk.librarymanagement.entity.User;
import com.kyk.librarymanagement.exception.BusinessException;
import com.kyk.librarymanagement.exception.ResourceNotFoundException;
import com.kyk.librarymanagement.repository.BookRepository;
import com.kyk.librarymanagement.repository.BorrowRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private BorrowRecordService borrowRecordService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BorrowRecordRepository borrowRecordRepository;
    
    // 创建图书
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }
        
    // 删除图书
    @Transactional
    @CacheEvict(value = "books", key = "#id")
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("图书不存在，ID: " + id));
        
        // 先删除该图书的所有借阅记录（解除外键约束）
        borrowRecordRepository.deleteByBookId(id);
        
        // 再删除图书
        bookRepository.delete(book);
    }
    
    // 批量删除图书
    @Transactional
    @CacheEvict(value = "books", allEntries = true)
    public void batchDeleteBooks(List<Long> ids) {
        List<Book> books = bookRepository.findAllById(ids);
        if (books.size() != ids.size()) {
            throw new ResourceNotFoundException("部分图书不存在");
        }
        
        // 先删除这些图书的所有借阅记录（解除外键约束）
        for (Long bookId : ids) {
            borrowRecordRepository.deleteByBookId(bookId);
        }
        
        // 再批量删除图书
        bookRepository.deleteAll(books);
    }
        
    // 更新图书
    @CacheEvict(value = "books", key = "#id")
    public Book updateBook(Long id, Book bookDetails) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("图书不存在，ID: " + id));
            
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
            
        return bookRepository.save(book);
    }
        
    // 查询所有图书（分页）- 不缓存，因为数据可能频繁变化
    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }
        
    // 查询单个图书
    @Cacheable(value = "books", key = "#id", unless = "#result == null")
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("图书不存在，ID: " + id));
    }
        
    // 借阅图书
    @Transactional
    @CacheEvict(value = "books", key = "#bookId")
    public Book borrowBook(Long bookId, BorrowRequest borrowRequest) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("图书不存在，ID: " + bookId));
            
        if (book.getIsBorrowed()) {
            throw new BusinessException("图书已借出，无法借阅");
        }
            
        // 校验用户是否存在
        User user = userService.validateUserExists(borrowRequest.getUserId());
            
        // 创建借阅记录
        borrowRecordService.createBorrowRecord(bookId, user);
            
        // 更新图书状态
        book.setIsBorrowed(true);
        return bookRepository.save(book);
    }
        
    // 归还图书
    @Transactional
    @CacheEvict(value = "books", key = "#bookId")
    public Book returnBook(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("图书不存在，ID: " + bookId));
            
        if (!book.getIsBorrowed()) {
            throw new BusinessException("图书未借出，无需归还");
        }
            
        // 校验用户是否存在
        User user = userService.validateUserExists(userId);
            
        // 更新借阅记录（设置归还时间，并校验是同一用户）
        borrowRecordService.returnBook(bookId, userId);
            
        // 更新图书状态
        book.setIsBorrowed(false);
        return bookRepository.save(book);
    }
        
    // 查询热门图书（借阅次数最多的前 N 本）
    @Cacheable(value = "popularBooks", key = "#limit", unless = "#result.isEmpty()")
    public List<PopularBookDTO> getPopularBooks(int limit) {
        // 参数校验，防止过大
        if (limit <= 0 || limit > 100) {
            limit = 10; // 默认值
        }
            
        List<Object[]> results = borrowRecordRepository.findTopBorrowedBooks(limit);
            
        return results.stream()
                .map(row -> {
                    // 处理时间戳转换
                    LocalDateTime createdAt = null;
                    LocalDateTime updatedAt = null;
                    
                    if (row[4] != null) {
                        if (row[4] instanceof java.sql.Timestamp) {
                            createdAt = ((java.sql.Timestamp) row[4]).toLocalDateTime();
                        } else if (row[4] instanceof LocalDateTime) {
                            createdAt = (LocalDateTime) row[4];
                        }
                    }
                    
                    if (row[5] != null) {
                        if (row[5] instanceof java.sql.Timestamp) {
                            updatedAt = ((java.sql.Timestamp) row[5]).toLocalDateTime();
                        } else if (row[5] instanceof LocalDateTime) {
                            updatedAt = (LocalDateTime) row[5];
                        }
                    }
                    
                    return new PopularBookDTO(
                            ((Number) row[0]).longValue(),                    // id
                            (String) row[1],                                   // title
                            (String) row[2],                                   // author
                            row[3] instanceof Boolean ? (Boolean) row[3] : ((Number) row[3]).intValue() == 1,  // isBorrowed
                            createdAt,                                         // createdAt
                            updatedAt,                                         // updatedAt
                            ((Number) row[6]).intValue()                      // borrowCount
                    );
                })
                .collect(Collectors.toList());
    }
}
