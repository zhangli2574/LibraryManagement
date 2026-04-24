package com.kyk.librarymanagement.repository;

import com.kyk.librarymanagement.entity.BorrowRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    // 分页查询（用于接口返回）
    Page<BorrowRecord> findByBookIdOrderByBorrowTimeDesc(Long bookId, Pageable pageable);
    
    // 不分页查询（用于归还时校验）
    List<BorrowRecord> findByBookIdOrderByBorrowTimeDesc(Long bookId);
    
    // 查询热门图书（借阅次数最多的前 N 本，只统计已归还的记录）
    @Query(value = "SELECT b.id, b.title, COUNT(br.id) as borrow_count " +
                   "FROM borrow_records br " +
                   "JOIN books b ON br.book_id = b.id " +
                   "WHERE br.return_time IS NOT NULL " +
                   "GROUP BY b.id, b.title " +
                   "ORDER BY borrow_count DESC " +
                   "LIMIT :limit",
           nativeQuery = true)
    List<Object[]> findTopBorrowedBooks(@Param("limit") int limit);
}
