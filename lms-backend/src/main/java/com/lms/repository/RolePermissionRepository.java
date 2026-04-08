package com.lms.repository;

import com.lms.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 角色权限关联Repository接口
 */
@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    
    /**
     * 根据角色ID删除权限关联
     */
    void deleteByRoleId(Long roleId);
    
    /**
     * 根据权限ID删除角色关联
     */
    void deleteByPermId(Long permId);
}