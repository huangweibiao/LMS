package com.lms.entity;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 证书实体类
 * 对应数据库表：certificate
 */
@Entity
@Table(name = "certificate")
public class Certificate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "course_id", nullable = false)
    private Long courseId;
    
    @Column(name = "template_id", nullable = false)
    private Long templateId;
    
    @Column(name = "certificate_no", nullable = false, unique = true, length = 50)
    private String certificateNo;
    
    @Column(name = "issue_date", nullable = false)
    private Date issueDate;
    
    @Column(name = "expire_date")
    private Date expireDate;
    
    @Column(name = "verify_code", nullable = false, unique = true, length = 32)
    private String verifyCode;
    
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private CertStatus status = CertStatus.VALID;
    
    @Column(nullable = false, updatable = false)
    private Date createTime;
    
    public enum CertStatus {
        VALID, REVOKED, EXPIRED
    }
    
    public Certificate() {
    }
    
    @PrePersist
    protected void onCreate() {
        createTime = new Date();
    }
    
    // Getter和Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public Long getTemplateId() { return templateId; }
    public void setTemplateId(Long templateId) { this.templateId = templateId; }
    public String getCertificateNo() { return certificateNo; }
    public void setCertificateNo(String certificateNo) { this.certificateNo = certificateNo; }
    public Date getIssueDate() { return issueDate; }
    public void setIssueDate(Date issueDate) { this.issueDate = issueDate; }
    public Date getExpireDate() { return expireDate; }
    public void setExpireDate(Date expireDate) { this.expireDate = expireDate; }
    public String getVerifyCode() { return verifyCode; }
    public void setVerifyCode(String verifyCode) { this.verifyCode = verifyCode; }
    public CertStatus getStatus() { return status; }
    public void setStatus(CertStatus status) { this.status = status; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}