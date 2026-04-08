package com.lms.service;

import com.lms.common.BusinessException;
import com.lms.entity.CourseCategory;
import com.lms.repository.CourseCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 课程分类服务
 */
@Service
public class CourseCategoryService {
    
    private final CourseCategoryRepository categoryRepository;
    
    public CourseCategoryService(CourseCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    
    /**
     * 查询所有分类
     */
    public List<CourseCategory> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    /**
     * 查询根分类
     */
    public List<CourseCategory> getRootCategories() {
        return categoryRepository.findByParentId(0L);
    }
    
    /**
     * 根据父ID查询子分类
     */
    public List<CourseCategory> getCategoriesByParentId(Long parentId) {
        return categoryRepository.findByParentId(parentId);
    }
    
    /**
     * 根据ID查询分类
     */
    public CourseCategory getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "分类不存在"));
    }
    
    /**
     * 创建分类
     */
    @Transactional
    public CourseCategory createCategory(CourseCategory category) {
        return categoryRepository.save(category);
    }
    
    /**
     * 更新分类
     */
    @Transactional
    public CourseCategory updateCategory(Long id, CourseCategory category) {
        CourseCategory existing = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "分类不存在"));
        
        existing.setName(category.getName());
        existing.setParentId(category.getParentId());
        existing.setSortOrder(category.getSortOrder());
        existing.setIcon(category.getIcon());
        existing.setStatus(category.getStatus());
        
        return categoryRepository.save(existing);
    }
    
    /**
     * 删除分类
     */
    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new BusinessException(404, "分类不存在");
        }
        categoryRepository.deleteById(id);
    }
}