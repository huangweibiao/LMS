package com.lms.entity;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 内容实体类
 * 对应数据库表：content
 */
@Entity
@Table(name = "content")
public class Content {
    
    /** 内容ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 所属章节ID */
    @Column(name = "section_id", nullable = false)
    private Long sectionId;
    
    /** 内容标题 */
    @Column(nullable = false, length = 200)
    private String title;
    
    /** 内容类型：video-视频, doc-文档, text-富文本, link-外链 */
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ContentType type;
    
    /** 内容URL */
    @Column(length = 500)
    private String url;
    
    /** 时长（秒，仅视频） */
    @Column
    private Integer duration = 0;
    
    /** 文件大小（字节） */
    @Column(name = "file_size")
    private Long fileSize;
    
    /** 是否必修内容：0-否, 1-是 */
    @Column(name = "is_required")
    private Integer isRequired = 1;
    
    /** 创建时间 */
    @Column(nullable = false, updatable = false)
    private Date createTime;
    
    /**
     * 内容类型枚举
     */
    public enum ContentType {
        VIDEO, DOC, TEXT, LINK
    }
    
    // 构造方法
    public Content() {
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
    
    public Long getSectionId() {
        return sectionId;
    }
    
    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public ContentType getType() {
        return type;
    }
    
    public void setType(ContentType type) {
        this.type = type;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public Integer getDuration() {
        return duration;
    }
    
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    
    public Long getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    
    public Integer getIsRequired() {
        return isRequired;
    }
    
    public void setIsRequired(Integer isRequired) {
        this.isRequired = isRequired;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}