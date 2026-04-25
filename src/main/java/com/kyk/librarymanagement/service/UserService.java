package com.kyk.librarymanagement.service;

import com.kyk.librarymanagement.dto.LoginRequest;
import com.kyk.librarymanagement.dto.LoginResponse;
import com.kyk.librarymanagement.dto.UserRequest;
import com.kyk.librarymanagement.entity.User;
import com.kyk.librarymanagement.exception.BusinessException;
import com.kyk.librarymanagement.exception.ResourceNotFoundException;
import com.kyk.librarymanagement.repository.UserRepository;
import com.kyk.librarymanagement.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
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
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在，ID: " + id));
        userRepository.delete(user);
    }
    
    // 更新用户
    @CacheEvict(value = "users", key = "#id")
    public User updateUser(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在，ID: " + id));
        
        user.setName(userRequest.getName());
        user.setPhone(userRequest.getPhone());
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
        
        // 生成 JWT Token
        String token = jwtUtil.generateToken(user.getId(), user.getPhone());
        
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
