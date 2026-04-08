package com.lms.controller;

import com.lms.common.Result;
import com.lms.dto.request.CourseRequest;
import com.lms.dto.response.CourseResponse;
import com.lms.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 课程管理控制器
 * 处理课程CRUD相关请求
 */
@RestController
@RequestMapping("/api/courses")
public class CourseController {
    
    private final CourseService courseService;
    
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }
    
    /**
     * 分页查询课程列表
     * GET /api/courses
     */
    @GetMapping
    public Result<com.lms.common.PageUtil<CourseResponse>> getCourses(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String status) {
        com.lms.common.PageUtil<CourseResponse> result = courseService.getCourses(page, size, categoryId, status);
        return Result.success(result);
    }
    
    /**
     * 根据ID查询课程
     * GET /api/courses/{id}
     */
    @GetMapping("/{id}")
    public Result<CourseResponse> getCourseById(@PathVariable Long id) {
        CourseResponse response = courseService.getCourseById(id);
        return Result.success(response);
    }
    
    /**
     * 根据分类查询课程
     * GET /api/courses/category/{categoryId}
     */
    @GetMapping("/category/{categoryId}")
    public Result<java.util.List<CourseResponse>> getCoursesByCategory(@PathVariable Long categoryId) {
        java.util.List<CourseResponse> list = courseService.getCoursesByCategory(categoryId);
        return Result.success(list);
    }
    
    /**
     * 创建课程
     * POST /api/courses
     */
    @PostMapping
    public Result<CourseResponse> createCourse(@Valid @RequestBody CourseRequest request,
                                                @RequestParam(defaultValue = "1") Long teacherId) {
        CourseResponse response = courseService.createCourse(request, teacherId);
        return Result.success("创建成功", response);
    }
    
    /**
     * 更新课程
     * PUT /api/courses/{id}
     */
    @PutMapping("/{id}")
    public Result<CourseResponse> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseRequest request) {
        CourseResponse response = courseService.updateCourse(id, request);
        return Result.success("更新成功", response);
    }
    
    /**
     * 发布课程
     * PUT /api/courses/{id}/publish
     */
    @PutMapping("/{id}/publish")
    public Result<CourseResponse> publishCourse(@PathVariable Long id) {
        CourseResponse response = courseService.publishCourse(id);
        return Result.success("发布成功", response);
    }
    
    /**
     * 删除课程
     * DELETE /api/courses/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return Result.success("删除成功", null);
    }
}