package com.lms.repository;

import com.lms.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 课程Repository接口
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    /**
     * 根据分类ID查询课程
     */
    List<Course> findByCategoryId(Long categoryId);
    
    /**
     * 根据讲师ID查询课程
     */
    List<Course> findByTeacherId(Long teacherId);
    
    /**
     * 根据状态查询课程
     */
    List<Course> findByStatus(Course.CourseStatus status);
    
    /**
     * 根据状态分页查询课程
     */
    Page<Course> findByStatus(Course.CourseStatus status, Pageable pageable);
    
    /**
     * 搜索课程
     */
    @Query("SELECT c FROM Course c WHERE c.title LIKE %?1% OR c.description LIKE %?1%")
    List<Course> searchCourses(String keyword);
    
    /**
     * 根据难度等级查询课程
     */
    List<Course> findByLevel(Course.CourseLevel level);
}