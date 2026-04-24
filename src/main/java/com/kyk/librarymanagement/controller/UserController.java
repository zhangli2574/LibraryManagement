package com.kyk.librarymanagement.controller;

import com.kyk.librarymanagement.dto.ApiResponse;
import com.kyk.librarymanagement.dto.LoginRequest;
import com.kyk.librarymanagement.dto.LoginResponse;
import com.kyk.librarymanagement.dto.UserRequest;
import com.kyk.librarymanagement.entity.User;
import com.kyk.librarymanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户管理", description = "用户的注册、登录、查询等操作")
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    // 用户登录
    @Operation(summary = "用户登录", description = "使用手机号和密码登录，返回 JWT Token")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = userService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.success("登录成功", response));
    }
    
    // 创建用户
    @Operation(summary = "创建用户", description = "注册新用户，密码会自动加密存储")
    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@Valid @RequestBody UserRequest userRequest) {
        User user = userService.createUser(userRequest);
        return ResponseEntity.ok(ApiResponse.success("用户创建成功", user));
    }
    
    // 删除用户
    @Operation(summary = "删除用户", description = "根据ID删除指定用户")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @Parameter(description = "用户ID", required = true, example = "1") 
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("用户删除成功", null));
    }
    
    // 更新用户
    @Operation(summary = "更新用户", description = "根据ID更新用户信息")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(
            @Parameter(description = "用户ID", required = true, example = "1") 
            @PathVariable Long id, 
            @Valid @RequestBody UserRequest userRequest) {
        User user = userService.updateUser(id, userRequest);
        return ResponseEntity.ok(ApiResponse.success("用户更新成功", user));
    }
    
    // 查询所有用户
    @Operation(summary = "查询所有用户", description = "获取系统中所有用户列表")
    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }
    
    // 查询单个用户
    @Operation(summary = "查询单个用户", description = "根据ID查询用户详情")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(
            @Parameter(description = "用户ID", required = true, example = "1") 
            @PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }
}
