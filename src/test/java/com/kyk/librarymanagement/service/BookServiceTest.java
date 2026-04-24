package com.kyk.librarymanagement.service;

import com.kyk.librarymanagement.dto.BorrowRequest;
import com.kyk.librarymanagement.entity.Book;
import com.kyk.librarymanagement.entity.User;
import com.kyk.librarymanagement.exception.BusinessException;
import com.kyk.librarymanagement.exception.ResourceNotFoundException;
import com.kyk.librarymanagement.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookService 单元测试")
class BookServiceTest {
    
    @Mock
    private BookRepository bookRepository;
    
    @Mock
    private BorrowRecordService borrowRecordService;
    
    @Mock
    private UserService userService;
    
    @InjectMocks
    private BookService bookService;
    
    private Book testBook;
    private User testUser;
    private BorrowRequest borrowRequest;
    
    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testBook = new Book("测试图书", "测试作者");
        testBook.setId(1L);
        testBook.setIsBorrowed(false);
        
        testUser = new User("张三", "13800138000", "password123");
        testUser.setId(1L);
        
        borrowRequest = new BorrowRequest();
        borrowRequest.setUserId(1L);
    }
    
    // ==================== 创建图书测试 ====================
    
    @Test
    @DisplayName("创建图书 - 成功")
    void testCreateBook_Success() {
        // Given
        Book newBook = new Book("新书", "新作者");
        when(bookRepository.save(any(Book.class))).thenReturn(newBook);
        
        // When
        Book result = bookService.createBook(newBook);
        
        // Then
        assertNotNull(result);
        assertEquals("新书", result.getTitle());
        assertEquals("新作者", result.getAuthor());
        verify(bookRepository, times(1)).save(newBook);
    }
    
    // ==================== 删除图书测试 ====================
    
    @Test
    @DisplayName("删除图书 - 成功")
    void testDeleteBook_Success() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        
        // When
        bookService.deleteBook(1L);
        
        // Then
        verify(bookRepository, times(1)).delete(testBook);
    }
    
    @Test
    @DisplayName("删除图书 - 图书不存在")
    void testDeleteBook_BookNotFound() {
        // Given
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.deleteBook(999L);
        });
        
        verify(bookRepository, never()).delete(any(Book.class));
    }
    
    // ==================== 更新图书测试 ====================
    
    @Test
    @DisplayName("更新图书 - 成功")
    void testUpdateBook_Success() {
        // Given
        Book updatedDetails = new Book("更新后的标题", "更新后的作者");
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        Book result = bookService.updateBook(1L, updatedDetails);
        
        // Then
        assertNotNull(result);
        assertEquals("更新后的标题", result.getTitle());
        assertEquals("更新后的作者", result.getAuthor());
        verify(bookRepository, times(1)).save(testBook);
    }
    
    @Test
    @DisplayName("更新图书 - 图书不存在")
    void testUpdateBook_BookNotFound() {
        // Given
        Book updatedDetails = new Book("新标题", "新作者");
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.updateBook(999L, updatedDetails);
        });
        
        verify(bookRepository, never()).save(any(Book.class));
    }
    
    // ==================== 查询所有图书测试 ====================
    
    @Test
    @DisplayName("查询所有图书 - 返回空列表")
    void testGetAllBooks_EmptyList() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(bookRepository.findAll(pageable)).thenReturn(emptyPage);
        
        // When
        Page<Book> result = bookService.getAllBooks(pageable);
        
        // Then
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
    }
    
    @Test
    @DisplayName("查询所有图书 - 返回正常列表")
    void testGetAllBooks_Success() {
        // Given
        List<Book> books = Arrays.asList(
            new Book("图书1", "作者1"),
            new Book("图书2", "作者2")
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        
        // When
        Page<Book> result = bookService.getAllBooks(pageable);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalElements());
    }
    
    // ==================== 查询单个图书测试 ====================
    
    @Test
    @DisplayName("查询单个图书 - 成功")
    void testGetBookById_Success() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        
        // When
        Book result = bookService.getBookById(1L);
        
        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("测试图书", result.getTitle());
    }
    
    @Test
    @DisplayName("查询单个图书 - 图书不存在")
    void testGetBookById_BookNotFound() {
        // Given
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.getBookById(999L);
        });
    }
    
    // ==================== 借阅图书测试 ====================
    
    @Test
    @DisplayName("借阅图书 - 成功")
    void testBorrowBook_Success() {
        // Given
        testBook.setIsBorrowed(false);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(userService.validateUserExists(1L)).thenReturn(testUser);
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        Book result = bookService.borrowBook(1L, borrowRequest);
        
        // Then
        assertNotNull(result);
        assertTrue(result.getIsBorrowed());
        verify(borrowRecordService, times(1)).createBorrowRecord(1L, testUser);
        verify(bookRepository, times(1)).save(testBook);
    }
    
    @Test
    @DisplayName("借阅图书 - 图书不存在")
    void testBorrowBook_BookNotFound() {
        // Given
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.borrowBook(999L, borrowRequest);
        });
        
        verify(userService, never()).validateUserExists(anyLong());
        verify(borrowRecordService, never()).createBorrowRecord(anyLong(), any(User.class));
    }
    
    @Test
    @DisplayName("借阅图书 - 图书已借出")
    void testBorrowBook_AlreadyBorrowed() {
        // Given
        testBook.setIsBorrowed(true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        
        // When & Then
        assertThrows(BusinessException.class, () -> {
            bookService.borrowBook(1L, borrowRequest);
        });
        
        verify(userService, never()).validateUserExists(anyLong());
        verify(borrowRecordService, never()).createBorrowRecord(anyLong(), any(User.class));
    }
    
    @Test
    @DisplayName("借阅图书 - 用户不存在")
    void testBorrowBook_UserNotFound() {
        // Given
        testBook.setIsBorrowed(false);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(userService.validateUserExists(999L))
            .thenThrow(new ResourceNotFoundException("用户不存在，ID: 999"));
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            BorrowRequest request = new BorrowRequest();
            request.setUserId(999L);
            bookService.borrowBook(1L, request);
        });
        
        verify(borrowRecordService, never()).createBorrowRecord(anyLong(), any(User.class));
    }
    
    // ==================== 归还图书测试 ====================
    
    @Test
    @DisplayName("归还图书 - 成功")
    void testReturnBook_Success() {
        // Given
        testBook.setIsBorrowed(true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(userService.validateUserExists(1L)).thenReturn(testUser);
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        Book result = bookService.returnBook(1L, 1L);
        
        // Then
        assertNotNull(result);
        assertFalse(result.getIsBorrowed());
        verify(borrowRecordService, times(1)).returnBook(1L, 1L);
        verify(bookRepository, times(1)).save(testBook);
    }
    
    @Test
    @DisplayName("归还图书 - 图书不存在")
    void testReturnBook_BookNotFound() {
        // Given
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.returnBook(999L, 1L);
        });
        
        verify(userService, never()).validateUserExists(anyLong());
        verify(borrowRecordService, never()).returnBook(anyLong(), anyLong());
    }
    
    @Test
    @DisplayName("归还图书 - 图书未借出")
    void testReturnBook_NotBorrowed() {
        // Given
        testBook.setIsBorrowed(false);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        
        // When & Then
        assertThrows(BusinessException.class, () -> {
            bookService.returnBook(1L, 1L);
        });
        
        verify(userService, never()).validateUserExists(anyLong());
        verify(borrowRecordService, never()).returnBook(anyLong(), anyLong());
    }
    
    @Test
    @DisplayName("归还图书 - 用户不存在")
    void testReturnBook_UserNotFound() {
        // Given
        testBook.setIsBorrowed(true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(userService.validateUserExists(999L))
            .thenThrow(new ResourceNotFoundException("用户不存在，ID: 999"));
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.returnBook(1L, 999L);
        });
        
        verify(borrowRecordService, never()).returnBook(anyLong(), anyLong());
    }
}
