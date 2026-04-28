package com.kyk.librarymanagement.repository;

import com.kyk.librarymanagement.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    
    // 根据手机号查询管理员
    Optional<Admin> findByAdminPhone(String adminPhone);
}
