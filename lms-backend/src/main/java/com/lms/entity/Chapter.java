package com.lms.entity;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 章节实体类
 * 对应数据库表：chapter
 */
@Entity
@Table(name = "chapter")
public class Chapter {
    
    /** 章节ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 所属课程ID */
    @Column(name = "course_id", nullable = false)
    private Long courseId;
    
    /** 父章节ID（支持章/节结构） */
    @Column(name = "parent_id")
    private Long parentId = 0L;
    
    /** 章节标题 */
    @Column(nullable = false, length = 200)
    private String title;
    
    /** 内容类型：video-视频, document-文档, quiz-测验, assignment-作业 */
    @Column(name = "content_type", length = 20)
    @Enumerated(EnumType.STRING)
    private ContentType contentType = ContentType.VIDEO;
    
    /** 视频/文档URL */
    @Column(name = "content_url", length = 500)
    private String contentUrl;
    
    /** 富文本内容 */
    @Column(name = "content_text", columnDefinition = "LONGTEXT")
    private String contentText;
    
    /** 预计学习时长（分钟） */
    @Column(name = "duration")
    private Integer duration = 0;
    
    /** 排序序号 */
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
    
    /** 是否免费试看：0-否, 1-是 */
    @Column(name = "is_free")
    private Integer isFree = 0;
    
    /** 创建时间 */
    @Column(nullable = false, updatable = false)
    private Date createTime;
    
    /** 更新时间 */
    @Column(nullable = false)
    private Date updateTime;
    
    /**
     * 内容类型枚举
     */
    public enum ContentType {
        VIDEO, DOCUMENT, QUIZ, ASSIGNMENT
    }
    
    // 构造方法
    public Chapter() {
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
    
    public Long getCourseId() {
        return courseId;
    }
    
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
    
    public Long getParentId() {
        return parentId;
    }
    
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public ContentType getContentType() {
        return contentType;
    }
    
    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }
    
    public String getContentUrl() {
        return contentUrl;
    }
    
    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }
    
    public String getContentText() {
        return contentText;
    }
    
    public void setContentText(String contentText) {
        this.contentText = contentText;
    }
    
    public Integer getDuration() {
        return duration;
    }
    
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    
    public Integer getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public Integer getIsFree() {
        return isFree;
    }
    
    public void setIsFree(Integer isFree) {
        this.isFree = isFree;
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
}