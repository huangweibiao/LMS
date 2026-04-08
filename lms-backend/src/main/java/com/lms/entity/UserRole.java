package com.lms.entity;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 用户角色关联实体类
 * 对应数据库表：sys_user_role
 */
@Entity
@Table(name = "sys_user_role")
public class UserRole {
    
    /** 关联ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 用户ID */
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    /** 角色ID */
    @Column(name = "role_id", nullable = false)
    private Long roleId;
    
    /** 创建时间 */
    @Column(nullable = false, updatable = false)
    private Date createTime;
    
    // 构造方法
    public UserRole() {
    }
    
    public UserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
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
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getRoleId() {
        return roleId;
    }
    
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}