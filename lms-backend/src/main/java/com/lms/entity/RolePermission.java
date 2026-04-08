package com.lms.entity;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 角色权限关联实体类
 * 对应数据库表：sys_role_permission
 */
@Entity
@Table(name = "sys_role_permission")
public class RolePermission {
    
    /** 关联ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 角色ID */
    @Column(name = "role_id", nullable = false)
    private Long roleId;
    
    /** 权限ID */
    @Column(name = "perm_id", nullable = false)
    private Long permId;
    
    /** 创建时间 */
    @Column(nullable = false, updatable = false)
    private Date createTime;
    
    // 构造方法
    public RolePermission() {
    }
    
    public RolePermission(Long roleId, Long permId) {
        this.roleId = roleId;
        this.permId = permId;
    }
    
    @PrePersist
    protected void onCreate() {
        createTime = new Date();
    }
    
    // Getter和Setter方法
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getRoleId() {
        return roleId;
    }
    
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    
    public Long getPermId() {
        return permId;
    }
    
    public void setPermId(Long permId) {
        this.permId = permId;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}