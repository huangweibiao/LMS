package com.lms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 课程实体类
 * 对应数据库表：course
 */
@Entity
@Table(name = "course")
public class Course {
    
    /** 课程ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 分类ID */
    @Column(name = "category_id", nullable = false)
    private Long categoryId;
    
    /** 讲师ID */
    @Column(name = "teacher_id", nullable = false)
    private Long teacherId;
    
    /** 课程标题 */
    @Column(nullable = false, length = 200)
    private String title;
    
    /** 副标题 */
    @Column(length = 300)
    private String subtitle;
    
    /** 封面图URL */
    @Column(name = "cover_image", length = 255)
    private String coverImage;
    
    /** 介绍视频URL */
    @Column(name = "intro_video", length = 255)
    private String introVideo;
    
    /** 课程简介 */
    @Column(columnDefinition = "TEXT")
    private String description;
    
    /** 难度等级：beginner-初级, intermediate-中级, advanced-高级 */
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private CourseLevel level = CourseLevel.BEGINNER;
    
    /** 价格（0表示免费） */
    @Column(precision = 10, scale = 2)
    private BigDecimal price = BigDecimal.ZERO;
    
    /** 总时长（分钟） */
    @Column(name = "total_duration")
    private Integer totalDuration = 0;
    
    /** 学习人数 */
    @Column(name = "total_students")
    private Integer totalStudents = 0;
    
    /** 综合评分 */
    @Column(precision = 2, scale = 1)
    private BigDecimal rating = BigDecimal.ZERO;
    
    /** 状态：draft-草稿, published-已发布, closed-已关闭 */
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private CourseStatus status = CourseStatus.DRAFT;
    
    /** 发布时间 */
    @Column(name = "publish_time")
    private Date publishTime;
    
    /** 创建时间 */
    @Column(nullable = false, updatable = false)
    private Date createTime;
    
    /** 更新时间 */
    @Column(nullable = false)
    private Date updateTime;
    
    /**
     * 课程等级枚举
     */
    public enum CourseLevel {
        BEGINNER, INTERMEDIATE, ADVANCED
    }
    
    /**
     * 课程状态枚举
     */
    public enum CourseStatus {
        DRAFT, PUBLISHED, CLOSED
    }
    
    // 构造方法
    public Course() {
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
    
    public Long getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    
    public Long getTeacherId() {
        return teacherId;
    }
    
    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getSubtitle() {
        return subtitle;
    }
    
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
    
    public String getCoverImage() {
        return coverImage;
    }
    
    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }
    
    public String getIntroVideo() {
        return introVideo;
    }
    
    public void setIntroVideo(String introVideo) {
        this.introVideo = introVideo;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public CourseLevel getLevel() {
        return level;
    }
    
    public void setLevel(CourseLevel level) {
        this.level = level;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public Integer getTotalDuration() {
        return totalDuration;
    }
    
    public void setTotalDuration(Integer totalDuration) {
        this.totalDuration = totalDuration;
    }
    
    public Integer getTotalStudents() {
        return totalStudents;
    }
    
    public void setTotalStudents(Integer totalStudents) {
        this.totalStudents = totalStudents;
    }
    
    public BigDecimal getRating() {
        return rating;
    }
    
    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }
    
    public CourseStatus getStatus() {
        return status;
    }
    
    public void setStatus(CourseStatus status) {
        this.status = status;
    }
    
    public Date getPublishTime() {
        return publishTime;
    }
    
    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
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
        return "Course{id=" + id + ", title='" + title + "', level=" + level + ", status=" + status + "}";
    }
}