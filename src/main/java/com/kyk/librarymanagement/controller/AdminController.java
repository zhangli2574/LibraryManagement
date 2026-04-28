package com.kyk.librarymanagement.controller;

import com.kyk.librarymanagement.dto.AdminLoginRequest;
import com.kyk.librarymanagement.dto.AdminLoginResponse;
import com.kyk.librarymanagement.dto.AdminPageResponse;
import com.kyk.librarymanagement.dto.ApiResponse;
import com.kyk.librarymanagement.dto.PageResponse;
import com.kyk.librarymanagement.entity.Admin;
import com.kyk.librarymanagement.service.AdminService;
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
import java.util.stream.Collectors;

@Tag(name = "管理员管理", description = "管理员登录和管理员信息管理")
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    @Operation(summary = "管理员登录", description = "管理员通过手机号和密码登录，返回 JWT Token（7天有效期）")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AdminLoginResponse>> login(@Valid @RequestBody AdminLoginRequest request) {
        AdminLoginResponse response = adminService.login(request);
        return ResponseEntity.ok(ApiResponse.success("登录成功", response));
    }
    
    @Operation(summary = "查询所有管理员", description = "分页查询管理员列表")
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<PageResponse<AdminPageResponse>>> getAllAdmins(
            @Parameter(description = "页码，从0开始", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Admin> adminPage = adminService.getAllAdmins(pageable);
        
        List<AdminPageResponse> content = adminPage.getContent().stream()
                .map(admin -> new AdminPageResponse(
                        admin.getId(),
                        admin.getAdminName(),
                        admin.getAdminPhone(),
                        admin.getUserRole(),
                        admin.getCreatedAt()
                ))
                .collect(Collectors.toList());
        
        PageResponse<AdminPageResponse> response = new PageResponse<>(
                content,
                adminPage.getTotalPages(),
                adminPage.getTotalElements()
        );
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @Operation(summary = "创建管理员", description = "创建新的管理员账号")
    @PostMapping
    public ResponseEntity<ApiResponse<Admin>> createAdmin(@Valid @RequestBody Admin admin) {
        Admin createdAdmin = adminService.createAdmin(admin);
        return ResponseEntity.ok(ApiResponse.success("创建成功", createdAdmin));
    }
    
    @Operation(summary = "更新管理员", description = "更新管理员信息")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Admin>> updateAdmin(
            @Parameter(description = "管理员ID", required = true)
            @PathVariable Long id,
            @RequestBody Admin adminDetails) {
        Admin updatedAdmin = adminService.updateAdmin(id, adminDetails);
        return ResponseEntity.ok(ApiResponse.success("更新成功", updatedAdmin));
    }
    
    @Operation(summary = "删除管理员", description = "删除指定管理员")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAdmin(
            @Parameter(description = "管理员ID", required = true)
            @PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.ok(ApiResponse.success("删除成功", null));
    }
    
    @Operation(summary = "批量删除管理员", description = "批量删除多个管理员")
    @DeleteMapping("/batch")
    public ResponseEntity<ApiResponse<Void>> batchDeleteAdmins(@RequestBody List<Long> ids) {
        adminService.batchDeleteAdmins(ids);
        return ResponseEntity.ok(ApiResponse.success("批量删除成功", null));
    }
}
