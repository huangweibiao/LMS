package com.lms.entity;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 考试记录实体类
 * 对应数据库表：exam_record
 */
@Entity
@Table(name = "exam_record")
public class ExamRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "exam_id", nullable = false)
    private Long examId;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "attempt_number", nullable = false)
    private Integer attemptNumber = 1;
    
    @Column(columnDefinition = "JSON")
    private String answers;
    
    @Column
    private Integer score;
    
    @Column(name = "is_passed")
    private Integer isPassed = 0;
    
    @Column(name = "start_time", nullable = false)
    private Date startTime;
    
    @Column(name = "submit_time")
    private Date submitTime;
    
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private RecordStatus status = RecordStatus.IN_PROGRESS;
    
    @Column(nullable = false, updatable = false)
    private Date createTime;
    
    public enum RecordStatus {
        IN_PROGRESS, SUBMITTED, GRADED
    }
    
    public ExamRecord() {
    }
    
    @PrePersist
    protected void onCreate() {
        createTime = new Date();
    }
    
    // Getter和Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getExamId() { return examId; }
    public void setExamId(Long examId) { this.examId = examId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Integer getAttemptNumber() { return attemptNumber; }
    public void setAttemptNumber(Integer attemptNumber) { this.attemptNumber = attemptNumber; }
    public String getAnswers() { return answers; }
    public void setAnswers(String answers) { this.answers = answers; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public Integer getIsPassed() { return isPassed; }
    public void setIsPassed(Integer isPassed) { this.isPassed = isPassed; }
    public Date getStartTime() { return startTime; }
    public void setStartTime(Date startTime) { this.startTime = startTime; }
    public Date getSubmitTime() { return submitTime; }
    public void setSubmitTime(Date submitTime) { this.submitTime = submitTime; }
    public RecordStatus getStatus() { return status; }
    public void setStatus(RecordStatus status) { this.status = status; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}