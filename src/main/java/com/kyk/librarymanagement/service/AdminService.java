package com.kyk.librarymanagement.service;

import com.kyk.librarymanagement.dto.AdminLoginRequest;
import com.kyk.librarymanagement.dto.AdminLoginResponse;
import com.kyk.librarymanagement.entity.Admin;
import com.kyk.librarymanagement.exception.BusinessException;
import com.kyk.librarymanagement.exception.ResourceNotFoundException;
import com.kyk.librarymanagement.repository.AdminRepository;
import com.kyk.librarymanagement.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    // 管理员登录
    public AdminLoginResponse login(AdminLoginRequest request) {
        Admin admin = adminRepository.findByAdminPhone(request.getAdminPhone())
                .orElseThrow(() -> new BusinessException("手机号或密码错误"));
        
        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new BusinessException("手机号或密码错误");
        }
        
        // 生成 Token（7天有效期 = 7 * 24 * 60 * 60 秒，添加用户类型标识）
        String token = jwtUtil.generateTokenWithExpiry(admin.getId(), admin.getAdminPhone(), "ADMIN", 7 * 24 * 60 * 60);
        
        AdminLoginResponse response = new AdminLoginResponse();
        response.setAdminId(admin.getId());
        response.setAdminName(admin.getAdminName());
        response.setAdminPhone(admin.getAdminPhone());
        response.setUserRole(admin.getUserRole());
        response.setToken(token);
        
        return response;
    }
    
    // 查询所有管理员（分页）
    public Page<Admin> getAllAdmins(Pageable pageable) {
        return adminRepository.findAll(pageable);
    }
    
    // 创建管理员
    public Admin createAdmin(Admin admin) {
        // 检查手机号是否已存在
        if (adminRepository.findByAdminPhone(admin.getAdminPhone()).isPresent()) {
            throw new BusinessException("手机号已注册");
        }
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }
    
    // 更新管理员
    public Admin updateAdmin(Long id, Admin adminDetails) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("管理员不存在，ID: " + id));
        
        if (adminDetails.getAdminName() != null && !adminDetails.getAdminName().isEmpty()) {
            admin.setAdminName(adminDetails.getAdminName());
        }
        if (adminDetails.getAdminPhone() != null && !adminDetails.getAdminPhone().isEmpty()) {
            admin.setAdminPhone(adminDetails.getAdminPhone());
        }
        if (adminDetails.getPassword() != null && !adminDetails.getPassword().isEmpty()) {
            admin.setPassword(passwordEncoder.encode(adminDetails.getPassword()));
        }
        if (adminDetails.getUserRole() != null) {
            admin.setUserRole(adminDetails.getUserRole());
        }
        
        return adminRepository.save(admin);
    }
    
    // 删除管理员
    public void deleteAdmin(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("管理员不存在，ID: " + id));
        adminRepository.delete(admin);
    }
    
    // 批量删除管理员
    public void batchDeleteAdmins(List<Long> ids) {
        List<Admin> admins = adminRepository.findAllById(ids);
        if (admins.size() != ids.size()) {
            throw new ResourceNotFoundException("部分管理员不存在");
        }
        adminRepository.deleteAll(admins);
    }
}
