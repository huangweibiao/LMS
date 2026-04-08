package com.lms.entity;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 权限实体类
 * 对应数据库表：sys_permission
 */
@Entity
@Table(name = "sys_permission")
public class SysPermission {
    
    /** 权限ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 权限名称 */
    @Column(nullable = false, length = 50)
    private String permName;
    
    /** 权限编码（如course:create） */
    @Column(nullable = false, unique = true, length = 100)
    private String permCode;
    
    /** 权限类型：menu-菜单, button-按钮, api-接口 */
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private PermType permType;
    
    /** 父权限ID */
    @Column(name = "parent_id")
    private Long parentId = 0L;
    
    /** 排序 */
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
    
    /** 创建时间 */
    @Column(nullable = false, updatable = false)
    private Date createTime;
    
    /**
     * 权限类型枚举
     */
    public enum PermType {
        MENU, BUTTON, API
    }
    
    // 构造方法
    public SysPermission() {
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
    
    public String getPermName() {
        return permName;
    }
    
    public void setPermName(String permName) {
        this.permName = permName;
    }
    
    public String getPermCode() {
        return permCode;
    }
    
    public void setPermCode(String permCode) {
        this.permCode = permCode;
    }
    
    public PermType getPermType() {
        return permType;
    }
    
    public void setPermType(PermType permType) {
        this.permType = permType;
    }
    
    public Long getParentId() {
        return parentId;
    }
    
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    
    public Integer getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    @Override
    public String toString() {
        return "SysPermission{id=" + id + ", permName='" + permName + "', permCode='" + permCode + "'}";
    }
}