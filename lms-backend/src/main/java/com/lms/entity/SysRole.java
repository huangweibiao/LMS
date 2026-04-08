package com.lms.entity;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 角色实体类
 * 对应数据库表：sys_role
 */
@Entity
@Table(name = "sys_role")
public class SysRole {
    
    /** 角色ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 角色名称 */
    @Column(nullable = false, unique = true, length = 50)
    private String roleName;
    
    /** 角色编码 */
    @Column(nullable = false, unique = true, length = 50)
    private String roleCode;
    
    /** 角色描述 */
    @Column(length = 200)
    private String description;
    
    /** 状态：0-禁用, 1-启用 */
    @Column(nullable = false)
    private Integer status = 1;
    
    /** 创建时间 */
    @Column(nullable = false, updatable = false)
    private Date createTime;
    
    // 构造方法
    public SysRole() {
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
    
    public String getRoleName() {
        return roleName;
    }
    
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    public String getRoleCode() {
        return roleCode;
    }
    
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    @Override
    public String toString() {
        return "SysRole{id=" + id + ", roleName='" + roleName + "', roleCode='" + roleCode + "'}";
    }
}