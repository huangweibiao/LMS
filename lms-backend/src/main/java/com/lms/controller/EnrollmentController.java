package com.lms.controller;

import com.lms.common.Result;
import com.lms.entity.Enrollment;
import com.lms.service.EnrollmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 选课控制器
 * 处理选课相关请求
 */
@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    
    private final EnrollmentService enrollmentService;
    
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }
    
    /**
     * 选课
     * POST /api/enrollments
     */
    @PostMapping
    public Result<Enrollment> enrollCourse(@RequestBody Map<String, Long> request) {
        Long userId = request.get("userId");
        Long courseId = request.get("courseId");
        Enrollment enrollment = enrollmentService.enrollCourse(userId, courseId);
        return Result.success("选课成功", enrollment);
    }
    
    /**
     * 获取用户选课列表
     * GET /api/enrollments/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public Result<List<Enrollment>> getUserEnrollments(@PathVariable Long userId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByUser(userId);
        return Result.success(enrollments);
    }
    
    /**
     * 获取课程选课列表
     * GET /api/enrollments/course/{courseId}
     */
    @GetMapping("/course/{courseId}")
    public Result<List<Enrollment>> getCourseEnrollments(@PathVariable Long courseId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourse(courseId);
        return Result.success(enrollments);
    }
    
    /**
     * 获取用户特定课程的选课记录
     * GET /api/enrollments/user/{userId}/course/{courseId}
     */
    @GetMapping("/user/{userId}/course/{courseId}")
    public Result<Enrollment> getEnrollment(@PathVariable Long userId, @PathVariable Long courseId) {
        Enrollment enrollment = enrollmentService.getEnrollment(userId, courseId);
        return Result.success(enrollment);
    }
    
    /**
     * 更新学习进度
     * PUT /api/enrollments/progress
     */
    @PutMapping("/progress")
    public Result<Void> updateProgress(@RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        Long courseId = Long.valueOf(request.get("courseId").toString());
        Integer progress = Integer.valueOf(request.get("progress").toString());
        enrollmentService.updateProgress(userId, courseId, progress);
        return Result.success("更新成功", null);
    }
    
    /**
     * 取消选课
     * DELETE /api/enrollments/user/{userId}/course/{courseId}
     */
    @DeleteMapping("/user/{userId}/course/{courseId}")
    public Result<Void> cancelEnrollment(@PathVariable Long userId, @PathVariable Long courseId) {
        enrollmentService.cancelEnrollment(userId, courseId);
        return Result.success("取消成功", null);
    }
}