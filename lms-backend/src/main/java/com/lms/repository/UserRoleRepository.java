package com.lms.repository;

import com.lms.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户角色关联Repository接口
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    
    /**
     * 根据用户ID查询角色
     */
    List<UserRole> findByUserId(Long userId);
    
    /**
     * 根据角色ID查询用户
     */
    List<UserRole> findByRoleId(Long roleId);
    
    /**
     * 删除用户的所有角色关联
     */
    void deleteByUserId(Long userId);
    
    /**
     * 删除角色的所有用户关联
     */
    void deleteByRoleId(Long roleId);
}