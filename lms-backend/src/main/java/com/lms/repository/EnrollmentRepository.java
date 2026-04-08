package com.lms.repository;

import com.lms.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 选课记录Repository接口
 */
@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    
    /**
     * 根据用户ID查询选课记录
     */
    List<Enrollment> findByUserId(Long userId);
    
    /**
     * 根据课程ID查询选课记录
     */
    List<Enrollment> findByCourseId(Long courseId);
    
    /**
     * 查询用户的某课程选课记录
     */
    Optional<Enrollment> findByUserIdAndCourseId(Long userId, Long courseId);
    
    /**
     * 判断用户是否已选课
     */
    boolean existsByUserIdAndCourseId(Long userId, Long courseId);
}