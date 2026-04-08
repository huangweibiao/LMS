package com.lms.repository;

import com.lms.entity.CourseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 课程分类Repository接口
 */
@Repository
public interface CourseCategoryRepository extends JpaRepository<CourseCategory, Long> {
    
    /**
     * 查询根分类
     */
    List<CourseCategory> findByParentId(Long parentId);
    
    /**
     * 根据名称查询分类
     */
    List<CourseCategory> findByNameContaining(String name);
}