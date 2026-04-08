package com.lms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 选课记录实体类
 * 对应数据库表：enrollment
 */
@Entity
@Table(name = "enrollment", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "course_id"}))
public class Enrollment {
    
    /** 记录ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 学员ID */
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    /** 课程ID */
    @Column(name = "course_id", nullable = false)
    private Long courseId;
    
    /** 订单号（付费课程） */
    @Column(name = "order_no", length = 32)
    private String orderNo;
    
    /** 支付金额 */
    @Column(name = "pay_amount", precision = 10, scale = 2)
    private BigDecimal payAmount = BigDecimal.ZERO;
    
    /** 学习进度（百分比） */
    @Column
    private Integer progress = 0;
    
    /** 最后学习时间 */
    @Column(name = "last_learn_time")
    private Date lastLearnTime;
    
    /** 状态：active-学习中, completed-已完成, dropped-已放弃 */
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status = EnrollmentStatus.ACTIVE;
    
    /** 选课时间 */
    @Column(name = "enroll_time", nullable = false)
    private Date enrollTime;
    
    /** 完成时间 */
    @Column(name = "complete_time")
    private Date completeTime;
    
    public enum EnrollmentStatus {
        ACTIVE, COMPLETED, DROPPED
    }
    
    public Enrollment() {
    }
    
    @PrePersist
    protected void onCreate() {
        enrollTime = new Date();
    }
    
    // Getter和Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public BigDecimal getPayAmount() { return payAmount; }
    public void setPayAmount(BigDecimal payAmount) { this.payAmount = payAmount; }
    public Integer getProgress() { return progress; }
    public void setProgress(Integer progress) { this.progress = progress; }
    public Date getLastLearnTime() { return lastLearnTime; }
    public void setLastLearnTime(Date lastLearnTime) { this.lastLearnTime = lastLearnTime; }
    public EnrollmentStatus getStatus() { return status; }
    public void setStatus(EnrollmentStatus status) { this.status = status; }
    public Date getEnrollTime() { return enrollTime; }
    public void setEnrollTime(Date enrollTime) { this.enrollTime = enrollTime; }
    public Date getCompleteTime() { return completeTime; }
    public void setCompleteTime(Date completeTime) { this.completeTime = completeTime; }
}