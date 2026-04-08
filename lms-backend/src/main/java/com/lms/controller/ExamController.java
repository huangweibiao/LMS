package com.lms.controller;

import com.lms.common.Result;
import com.lms.entity.Exam;
import com.lms.service.ExamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 考试控制器
 */
@RestController
@RequestMapping("/api/exams")
public class ExamController {
    
    private final ExamService examService;
    
    public ExamController(ExamService examService) {
        this.examService = examService;
    }
    
    /**
     * 获取课程关联的试卷
     * GET /api/exams/course/{courseId}
     */
    @GetMapping("/course/{courseId}")
    public Result<List<Exam>> getExamsByCourse(@PathVariable Long courseId) {
        List<Exam> exams = examService.getExamsByCourse(courseId);
        return Result.success(exams);
    }
    
    /**
     * 获取试卷详情
     * GET /api/exams/{id}
     */
    @GetMapping("/{id}")
    public Result<Exam> getExamById(@PathVariable Long id) {
        Exam exam = examService.getExamById(id);
        return Result.success(exam);
    }
    
    /**
     * 获取所有已发布试卷
     * GET /api/exams/published
     */
    @GetMapping("/published")
    public Result<List<Exam>> getPublishedExams() {
        List<Exam> exams = examService.getPublishedExams();
        return Result.success(exams);
    }
    
    /**
     * 创建试卷
     * POST /api/exams
     */
    @PostMapping
    public Result<Exam> createExam(@RequestBody Exam exam) {
        Exam created = examService.createExam(exam);
        return Result.success("创建成功", created);
    }
    
    /**
     * 更新试卷
     * PUT /api/exams/{id}
     */
    @PutMapping("/{id}")
    public Result<Exam> updateExam(@PathVariable Long id, @RequestBody Exam exam) {
        Exam updated = examService.updateExam(id, exam);
        return Result.success("更新成功", updated);
    }
    
    /**
     * 发布试卷
     * PUT /api/exams/{id}/publish
     */
    @PutMapping("/{id}/publish")
    public Result<Exam> publishExam(@PathVariable Long id) {
        Exam exam = examService.publishExam(id);
        return Result.success("发布成功", exam);
    }
    
    /**
     * 关闭试卷
     * PUT /api/exams/{id}/close
     */
    @PutMapping("/{id}/close")
    public Result<Exam> closeExam(@PathVariable Long id) {
        Exam exam = examService.closeExam(id);
        return Result.success("关闭成功", exam);
    }
    
    /**
     * 删除试卷
     * DELETE /api/exams/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteExam(@PathVariable Long id) {
        examService.deleteExam(id);
        return Result.success("删除成功", null);
    }
}