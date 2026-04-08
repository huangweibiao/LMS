package com.lms.controller;

import com.lms.common.Result;
import com.lms.entity.CourseCategory;
import com.lms.service.CourseCategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课程分类控制器
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    
    private final CourseCategoryService categoryService;
    
    public CategoryController(CourseCategoryService categoryService) {
        this.categoryService = categoryService;
    }
    
    /**
     * 获取所有分类
     */
    @GetMapping
    public Result<List<CourseCategory>> getAllCategories() {
        return Result.success(categoryService.getAllCategories());
    }
    
    /**
     * 获取根分类
     */
    @GetMapping("/root")
    public Result<List<CourseCategory>> getRootCategories() {
        return Result.success(categoryService.getRootCategories());
    }
    
    /**
     * 根据父ID获取子分类
     */
    @GetMapping("/parent/{parentId}")
    public Result<List<CourseCategory>> getCategoriesByParentId(@PathVariable Long parentId) {
        return Result.success(categoryService.getCategoriesByParentId(parentId));
    }
    
    /**
     * 根据ID获取分类
     */
    @GetMapping("/{id}")
    public Result<CourseCategory> getCategoryById(@PathVariable Long id) {
        return Result.success(categoryService.getCategoryById(id));
    }
    
    /**
     * 创建分类
     */
    @PostMapping
    public Result<CourseCategory> createCategory(@RequestBody CourseCategory category) {
        return Result.success("创建成功", categoryService.createCategory(category));
    }
    
    /**
     * 更新分类
     */
    @PutMapping("/{id}")
    public Result<CourseCategory> updateCategory(@PathVariable Long id, @RequestBody CourseCategory category) {
        return Result.success("更新成功", categoryService.updateCategory(id, category));
    }
    
    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success("删除成功", null);
    }
}