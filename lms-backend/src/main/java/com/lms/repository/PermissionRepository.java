package com.lms.repository;

import com.lms.entity.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 权限Repository接口
 */
@Repository
public interface PermissionRepository extends JpaRepository<SysPermission, Long> {
    
    /**
     * 根据权限编码查询
     */
    Optional<SysPermission> findByPermCode(String permCode);
    
    /**
     * 根据父权限ID查询子权限
     */
    List<SysPermission> findByParentId(Long parentId);
    
    /**
     * 判断权限编码是否存在
     */
    boolean existsByPermCode(String permCode);
}