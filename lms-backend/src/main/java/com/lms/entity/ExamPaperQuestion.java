package com.lms.entity;

import jakarta.persistence.*;

/**
 * 试卷题目关联实体类
 * 对应数据库表：exam_paper_question
 */
@Entity
@Table(name = "exam_paper_question", uniqueConstraints = @UniqueConstraint(columnNames = {"exam_id", "question_id"}))
public class ExamPaperQuestion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "exam_id", nullable = false)
    private Long examId;
    
    @Column(name = "question_id", nullable = false)
    private Long questionId;
    
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
    
    @Column(name = "custom_score")
    private Integer customScore;
    
    public ExamPaperQuestion() {
    }
    
    public ExamPaperQuestion(Long examId, Long questionId) {
        this.examId = examId;
        this.questionId = questionId;
    }
    
    // Getter和Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getExamId() { return examId; }
    public void setExamId(Long examId) { this.examId = examId; }
    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public Integer getCustomScore() { return customScore; }
    public void setCustomScore(Integer customScore) { this.customScore = customScore; }
}