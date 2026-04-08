package com.lms.entity;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 用户实体类
 * 对应数据库表：sys_user
 */
@Entity
@Table(name = "sys_user")
public class SysUser {
    
    /** 用户ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 用户名 */
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    
    /** 密码 */
    @Column(nullable = false, length = 255)
    private String password;
    
    /** 真实姓名 */
    @Column(nullable = false, length = 50)
    private String realName;
    
    /** 邮箱 */
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    /** 手机号 */
    @Column(length = 20)
    private String mobile;
    
    /** 头像URL */
    @Column(length = 255)
    private String avatar;
    
    /** 角色：admin-管理员, teacher-讲师, student-学员 */
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.STUDENT;
    
    /** 状态：0-禁用, 1-启用 */
    @Column(nullable = false)
    private Integer status = 1;
    
    /** 最后登录时间 */
    @Column(name = "last_login_time")
    private Date lastLoginTime;
    
    /** 创建时间 */
    @Column(nullable = false, updatable = false)
    private Date createTime;
    
    /** 更新时间 */
    @Column(nullable = false)
    private Date updateTime;
    
    /**
     * 用户角色枚举
     */
    public enum UserRole {
        ADMIN, TEACHER, STUDENT
    }
    
    // 构造方法
    public SysUser() {
    }
    
    @PrePersist
    protected void onCreate() {
        createTime = new Date();
        updateTime = new Date();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = new Date();
    }
    
    // Getter和Setter方法
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRealName() {
        return realName;
    }
    
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getMobile() {
        return mobile;
    }
    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public UserRole getRole() {
        return role;
    }
    
    public void setRole(UserRole role) {
        this.role = role;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public Date getLastLoginTime() {
        return lastLoginTime;
    }
    
    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public Date getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    @Override
    public String toString() {
        return "SysUser{id=" + id + ", username='" + username + "', realName='" + realName + "', role=" + role + "}";
    }
}