package com.lms.entity;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 证书模板实体类
 * 对应数据库表：certificate_template
 */
@Entity
@Table(name = "certificate_template")
public class CertificateTemplate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(name = "template_code", nullable = false, unique = true, length = 100)
    private String templateCode;
    
    @Column(length = 255)
    private String background;
    
    @Column(columnDefinition = "JSON")
    private String fields;
    
    @Column(name = "content_html", columnDefinition = "TEXT")
    private String contentHtml;
    
    @Column
    private Integer status = 1;
    
    @Column(nullable = false, updatable = false)
    private Date createTime;
    
    public CertificateTemplate() {
    }
    
    @PrePersist
    protected void onCreate() {
        createTime = new Date();
    }
    
    // Getter和Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTemplateCode() { return templateCode; }
    public void setTemplateCode(String templateCode) { this.templateCode = templateCode; }
    public String getBackground() { return background; }
    public void setBackground(String background) { this.background = background; }
    public String getFields() { return fields; }
    public void setFields(String fields) { this.fields = fields; }
    public String getContentHtml() { return contentHtml; }
    public void setContentHtml(String contentHtml) { this.contentHtml = contentHtml; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}