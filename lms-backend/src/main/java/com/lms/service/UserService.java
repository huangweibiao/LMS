package com.lms.service;

import com.lms.common.BusinessException;
import com.lms.common.PageUtil;
import com.lms.dto.request.UserRequest;
import com.lms.dto.response.UserResponse;
import com.lms.entity.SysUser;
import com.lms.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务
 * 处理用户CRUD、角色绑定等
 */
@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    /**
     * 分页查询用户
     */
    public PageUtil<UserResponse> getUsers(Integer page, Integer size, String keyword) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<SysUser> userPage;
        
        if (keyword != null && !keyword.isEmpty()) {
            // 简单实现，实际可用QueryDSL或Specification
            userPage = userRepository.findAll(pageRequest);
        } else {
            userPage = userRepository.findAll(pageRequest);
        }
        
        List<UserResponse> list = userPage.getContent().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        
        return new PageUtil<>(list, userPage.getTotalElements(), page, size);
    }
    
    /**
     * 根据ID查询用户
     */
    public UserResponse getUserById(Long id) {
        SysUser user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));
        return convertToResponse(user);
    }
    
    /**
     * 创建用户
     */
    @Transactional
    public UserResponse createUser(UserRequest request) {
        // 检查用户名是否存在
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException(400, "用户名已存在");
        }
        
        // 检查邮箱是否存在
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(400, "邮箱已被使用");
        }
        
        // 创建用户
        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRealName(request.getRealName());
        user.setEmail(request.getEmail());
        user.setMobile(request.getMobile());
        user.setAvatar(request.getAvatar());
        user.setRole(SysUser.UserRole.valueOf(request.getRole()));
        user.setStatus(request.getStatus() != null ? request.getStatus() : 1);
        
        user = userRepository.save(user);
        return convertToResponse(user);
    }
    
    /**
     * 更新用户
     */
    @Transactional
    public UserResponse updateUser(Long id, UserRequest request) {
        SysUser user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));
        
        user.setRealName(request.getRealName());
        user.setEmail(request.getEmail());
        user.setMobile(request.getMobile());
        user.setAvatar(request.getAvatar());
        
        if (request.getRole() != null) {
            user.setRole(SysUser.UserRole.valueOf(request.getRole()));
        }
        
        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }
        
        // 如果提供了新密码，则更新密码
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        
        user = userRepository.save(user);
        return convertToResponse(user);
    }
    
    /**
     * 删除用户
     */
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new BusinessException(404, "用户不存在");
        }
        userRepository.deleteById(id);
    }
    
    /**
     * 转换实体为响应DTO
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