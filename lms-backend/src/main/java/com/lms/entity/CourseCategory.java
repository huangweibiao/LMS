package com.lms.entity;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 课程分类实体类
 * 对应数据库表：course_category
 */
@Entity
@Table(name = "course_category")
public class CourseCategory {
    
    /** 分类ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 父分类ID，0表示根分类 */
    @Column(name = "parent_id")
    private Long parentId = 0L;
    
    /** 分类名称 */
    @Column(nullable = false, length = 50)
    private String name;
    
    /** 排序序号 */
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
    
    /** 图标URL */
    @Column(length = 255)
    private String icon;
    
    /** 状态：0-禁用, 1-启用 */
    @Column(nullable = false)
    private Integer status = 1;
    
    /** 创建时间 */
    @Column(nullable = false, updatable = false)
    private Date createTime;
    
    // 构造方法
    public CourseCategory() {
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
    
    public Long getParentId() {
        return parentId;
    }
    
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
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
        return "CourseCategory{id=" + id + ", name='" + name + "', parentId=" + parentId + "}";
    }
}