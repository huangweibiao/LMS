package com.lms.entity;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 用户信息扩展实体类
 * 对应数据库表：user_profile
 */
@Entity
@Table(name = "user_profile")
public class UserProfile {
    
    /** 主键ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 用户ID */
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;
    
    /** 个人简介 */
    @Column(columnDefinition = "TEXT")
    private String bio;
    
    /** 所属企业 */
    @Column(length = 100)
    private String company;
    
    /** 职位 */
    @Column(length = 50)
    private String jobTitle;
    
    /** 地址 */
    @Column(length = 255)
    private String address;
    
    /** 出生日期 */
    @Column(name = "birth_date")
    private Date birthDate;
    
    /** 更新时间 */
    @Column(nullable = false)
    private Date updateTime;
    
    // 构造方法
    public UserProfile() {
    }
    
    @PrePersist
    @PreUpdate
    protected void onSave() {
        updateTime = new Date();
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
    
    public String getBio() {
        return bio;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }
    
    public String getCompany() {
        return company;
    }
    
    public void setCompany(String company) {
        this.company = company;
    }
    
    public String getJobTitle() {
        return jobTitle;
    }
    
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public Date getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
    
    public Date getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}