package com.lms.repository;

import com.lms.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 角色Repository接口
 */
@Repository
public interface RoleRepository extends JpaRepository<SysRole, Long> {
    
    /**
     * 根据角色编码查询角色
     */
    Optional<SysRole> findByRoleCode(String roleCode);
    
    /**
     * 判断角色编码是否存在
     */
    boolean existsByRoleCode(String roleCode);
}