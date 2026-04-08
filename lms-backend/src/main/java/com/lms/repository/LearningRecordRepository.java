package com.lms.repository;

import com.lms.entity.LearningRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 学习记录Repository接口
 */
@Repository
public interface LearningRecordRepository extends JpaRepository<LearningRecord, Long> {
    
    /**
     * 查询用户课程的学习记录
     */
    List<LearningRecord> findByUserIdAndCourseId(Long userId, Long courseId);
    
    /**
     * 查询用户课程章节的学习记录
     */
    Optional<LearningRecord> findByUserIdAndCourseIdAndChapterId(Long userId, Long courseId, Long chapterId);
    
    /**
     * 查询用户课程内容的学习记录
     */
    Optional<LearningRecord> findByUserIdAndCourseIdAndContentId(Long userId, Long courseId, Long contentId);
    
    /**
     * 统计用户课程已完成章节数
     */
    long countByUserIdAndCourseIdAndIsCompleted(Long userId, Long courseId, Integer isCompleted);
}