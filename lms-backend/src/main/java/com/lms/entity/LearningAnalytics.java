package com.lms.entity;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 学习统计实体类
 * 对应数据库表：learning_analytics
 */
@Entity
@Table(name = "learning_analytics", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "course_id", "date"}))
public class LearningAnalytics {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "course_id", nullable = false)
    private Long courseId;
    
    @Column(nullable = false)
    private Date date;
    
    @Column(name = "learn_duration")
    private Integer learnDuration = 0;
    
    @Column(name = "complete_chapters")
    private Integer completeChapters = 0;
    
    @Column(name = "login_count")
    private Integer loginCount = 0;
    
    public LearningAnalytics() {
    }
    
    // Getter和Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public Integer getLearnDuration() { return learnDuration; }
    public void setLearnDuration(Integer learnDuration) { this.learnDuration = learnDuration; }
    public Integer getCompleteChapters() { return completeChapters; }
    public void setCompleteChapters(Integer completeChapters) { this.completeChapters = completeChapters; }
    public Integer getLoginCount() { return loginCount; }
    public void setLoginCount(Integer loginCount) { this.loginCount = loginCount; }
}