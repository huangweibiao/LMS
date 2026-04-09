package com.lms.service;

import com.lms.common.BusinessException;
import com.lms.common.JwtUtil;
import com.lms.dto.request.LoginRequest;
import com.lms.dto.request.RegisterRequest;
import com.lms.dto.response.LoginResponse;
import com.lms.dto.response.UserResponse;
import com.lms.entity.SysUser;
import com.lms.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 认证服务
 * 处理用户登录、注册、令牌生成等
 */
@Service
public class AuthService {
    
    /** 用户Repository */
    private final UserRepository userRepository;
    
    /** 密码编码器 */
    private final PasswordEncoder passwordEncoder;
    
    /** JWT工具类 */
    private final JwtUtil jwtUtil;
    
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    
    /**
     * 用户登录
     * @param request 登录请求
     * @return 登录响应（包含token和用户信息）
     */
    public LoginResponse login(LoginRequest request) {
        // 查询用户
        SysUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException(401, "用户名或密码错误"));

        // 验证密码
        boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        System.out.println("=== LOGIN DEBUG ===");
        System.out.println("Username: " + request.getUsername());
        System.out.println("Input password: " + request.getPassword());
        System.out.println("Stored hash: " + user.getPassword());
        System.out.println("Matches: " + matches);
        System.out.println("====================");

        if (!matches) {
            throw new BusinessException(401, "用户名或密码错误");
        }

        // 检查用户状态
        if (user.getStatus() != 1) {
            throw new BusinessException(403, "用户已被禁用");
        }

        // 生成JWT令牌
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        // 更新最后登录时间
        user.setLastLoginTime(new Date());
        userRepository.save(user);

        // 返回登录响应
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUser(convertToResponse(user));

        return response;
    }
    
    /**
     * 用户注册
     * @param request 注册请求
     * @return 注册后的用户信息
     */
    @Transactional
    public UserResponse register(RegisterRequest request) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException(400, "用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(400, "邮箱已被使用");
        }
        
        // 创建用户实体
        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRealName(request.getRealName());
        user.setEmail(request.getEmail());
        user.setMobile(request.getMobile());
        user.setRole(SysUser.UserRole.STUDENT);
        user.setStatus(1);
        
        // 保存用户
        user = userRepository.save(user);
        
        return convertToResponse(user);
    }
    
    /**
     * 获取当前登录用户信息
     * @param userId 用户ID
     * @return 用户响应
     */
    public UserResponse getCurrentUser(Long userId) {
        SysUser user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));
        return convertToResponse(user);
    }
    
    /**
     * 实体转换为响应DTO
     */
    private UserResponse convertToResponse(SysUser user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setRealName(user.getRealName());
        response.setEmail(user.getEmail());
        response.setMobile(user.getMobile());
        response.setAvatar(user.getAvatar());
        response.setRole(user.getRole().name());
        response.setStatus(user.getStatus());
        response.setLastLoginTime(user.getLastLoginTime());
        response.setCreateTime(user.getCreateTime());
        return response;
    }
}