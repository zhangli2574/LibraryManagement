package com.kyk.librarymanagement.service;

import com.kyk.librarymanagement.entity.BorrowRecord;
import com.kyk.librarymanagement.entity.User;
import com.kyk.librarymanagement.exception.BusinessException;
import com.kyk.librarymanagement.repository.BorrowRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BorrowRecordService {
    
    @Autowired
    private BorrowRecordRepository borrowRecordRepository;
    
    // 创建借阅记录
    public BorrowRecord createBorrowRecord(Long bookId, User user) {
        BorrowRecord record = new BorrowRecord(bookId, user);
        return borrowRecordRepository.save(record);
    }
    
    // 归还图书（更新最新未归还的记录，并校验是同一用户）
    public void returnBook(Long bookId, Long userId) {
        List<BorrowRecord> records = borrowRecordRepository.findByBookIdOrderByBorrowTimeDesc(bookId);
        
        // 找到最新的未归还记录
        BorrowRecord activeRecord = records.stream()
                .filter(record -> record.getReturnTime() == null)
                .findFirst()
                .orElseThrow(() -> new BusinessException("没有找到该图书的借阅记录"));
        
        // 校验是否是同一用户归还
        if (!activeRecord.getUser().getId().equals(userId)) {
            throw new BusinessException("只能由借阅者本人归还图书");
        }
        
        // 设置归还时间
        activeRecord.setReturnTime(LocalDateTime.now());
        borrowRecordRepository.save(activeRecord);
    }
    
    // 查询某图书的借阅记录（分页）
    public Page<BorrowRecord> getBorrowRecordsByBookId(Long bookId, Pageable pageable) {
        return borrowRecordRepository.findByBookIdOrderByBorrowTimeDesc(bookId, pageable);
    }
}
