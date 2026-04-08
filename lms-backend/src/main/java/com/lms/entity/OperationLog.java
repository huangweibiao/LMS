package com.lms.entity;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 操作日志实体类
 * 对应数据库表：operation_log
 */
@Entity
@Table(name = "operation_log")
public class OperationLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id")
    private Long userId;
    
    @Column(length = 50)
    private String username;
    
    @Column(nullable = false, length = 100)
    private String operation;
    
    @Column(length = 200)
    private String method;
    
    @Column(columnDefinition = "TEXT")
    private String params;
    
    @Column(length = 50)
    private String ip;
    
    @Column
    private Integer duration;
    
    @Column(nullable = false, updatable = false)
    private Date createTime;
    
    public OperationLog() {
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
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public String getParams() { return params; }
    public void setParams(String params) { this.params = params; }
    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}