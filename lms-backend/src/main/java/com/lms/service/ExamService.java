package com.lms.service;

import com.lms.common.BusinessException;
import com.lms.entity.Exam;
import com.lms.repository.ExamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 考试服务
 */
@Service
public class ExamService {
    
    private final ExamRepository examRepository;
    
    public ExamService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }
    
    /**
     * 获取课程关联的试卷
     */
    public List<Exam> getExamsByCourse(Long courseId) {
        return examRepository.findByCourseId(courseId);
    }
    
    /**
     * 获取试卷详情
     */
    public Exam getExamById(Long id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "试卷不存在"));
    }
    
    /**
     * 获取所有已发布试卷
     */
    public List<Exam> getPublishedExams() {
        return examRepository.findByStatus(Exam.ExamStatus.PUBLISHED);
    }
    
    /**
     * 创建试卷
     */
    @Transactional
    public Exam createExam(Exam exam) {
        return examRepository.save(exam);
    }
    
    /**
     * 更新试卷
     */
    @Transactional
    public Exam updateExam(Long id, Exam exam) {
        Exam existing = examRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "试卷不存在"));
        
        existing.setTitle(exam.getTitle());
        existing.setDescription(exam.getDescription());
        existing.setTotalScore(exam.getTotalScore());
        existing.setPassScore(exam.getPassScore());
        existing.setDuration(exam.getDuration());
        existing.setQuestionCount(exam.getQuestionCount());
        existing.setAttemptLimit(exam.getAttemptLimit());
        existing.setStartTime(exam.getStartTime());
        existing.setEndTime(exam.getEndTime());
        
        return examRepository.save(existing);
    }
    
    /**
     * 发布试卷
     */
    @Transactional
    public Exam publishExam(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "试卷不存在"));
        
        exam.setStatus(Exam.ExamStatus.PUBLISHED);
        return examRepository.save(exam);
    }
    
    /**
     * 关闭试卷
     */
    @Transactional
    public Exam closeExam(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "试卷不存在"));
        
        exam.setStatus(Exam.ExamStatus.CLOSED);
        return examRepository.save(exam);
    }
    
    /**
     * 删除试卷
     */
    @Transactional
    public void deleteExam(Long id) {
        if (!examRepository.existsById(id)) {
            throw new BusinessException(404, "试卷不存在");
        }
        examRepository.deleteById(id);
    }
}