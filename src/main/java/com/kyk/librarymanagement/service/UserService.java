package com.kyk.librarymanagement.service;

import com.kyk.librarymanagement.dto.LoginRequest;
import com.kyk.librarymanagement.dto.LoginResponse;
import com.kyk.librarymanagement.dto.UserRequest;
import com.kyk.librarymanagement.entity.User;
import com.kyk.librarymanagement.exception.BusinessException;
import com.kyk.librarymanagement.exception.ResourceNotFoundException;
import com.kyk.librarymanagement.repository.BorrowRecordRepository;
import com.kyk.librarymanagement.repository.UserRepository;
import com.kyk.librarymanagement.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private BorrowRecordRepository borrowRecordRepository;
    
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    // 创建用户
    public User createUser(UserRequest userRequest) {
        // 检查手机号是否已存在
        if (userRepository.findByPhone(userRequest.getPhone()).isPresent()) {
            throw new BusinessException("手机号已注册");
        }
        
        User user = new User();
        user.setName(userRequest.getName());
        user.setPhone(userRequest.getPhone());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        
        return userRepository.save(user);
    }
    
    // 删除用户
    @Transactional
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在，ID: " + id));
        
        // 先删除该用户的所有借阅记录（解除外键约束）
        borrowRecordRepository.deleteByUserId(id);
        
        // 再删除用户
        userRepository.delete(user);
    }
    
    // 批量删除用户
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public void batchDeleteUsers(List<Long> ids) {
        List<User> users = userRepository.findAllById(ids);
        if (users.size() != ids.size()) {
            throw new ResourceNotFoundException("部分用户不存在");
        }
        
        // 先删除这些用户的所有借阅记录（解除外键约束）
        for (Long userId : ids) {
            borrowRecordRepository.deleteByUserId(userId);
        }
        
        // 再批量删除用户
        userRepository.deleteAll(users);
    }
    
    // 更新用户
    @CacheEvict(value = "users", key = "#id")
    public User updateUser(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在，ID: " + id));
        
        // 只更新非空字段
        if (userRequest.getName() != null && !userRequest.getName().isEmpty()) {
            user.setName(userRequest.getName());
        }
        if (userRequest.getPhone() != null && !userRequest.getPhone().isEmpty()) {
            user.setPhone(userRequest.getPhone());
        }
        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }
        
        return userRepository.save(user);
    }
    
    // 查询所有用户 - 不缓存，数据量可能较大
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    // 查询单个用户
    @Cacheable(value = "users", key = "#id", unless = "#result == null")
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在，ID: " + id));
    }
    
    // 用户登录 - 不缓存，每次都需要验证密码
    public LoginResponse login(LoginRequest loginRequest) {
        // 根据手机号查询用户
        User user = userRepository.findByPhone(loginRequest.getPhone())
                .orElseThrow(() -> new BusinessException("手机号或密码错误"));
        
        // 验证密码
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BusinessException("手机号或密码错误");
        }
        
        // 生成 JWT Token（添加用户类型标识）
        String token = jwtUtil.generateToken(user.getId(), user.getPhone(), "USER");
        
        // 返回登录响应
        LoginResponse response = new LoginResponse();
        response.setUserId(user.getId());
        response.setUserName(user.getName());
        response.setToken(token);
        
        return response;
    }
    
    // 校验用户是否存在
    @Cacheable(value = "users", key = "#userId", unless = "#result == null")
    public User validateUserExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在，ID: " + userId));
    }
}
