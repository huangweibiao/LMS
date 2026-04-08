package com.lms.repository;

import com.lms.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 章节Repository接口
 */
@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    
    /**
     * 根据课程ID查询章节
     */
    List<Chapter> findByCourseId(Long courseId);
    
    /**
     * 根据课程ID和父章节ID查询子章节
     */
    List<Chapter> findByCourseIdAndParentId(Long courseId, Long parentId);
    
    /**
     * 删除课程的所有章节
     */
    void deleteByCourseId(Long courseId);
}