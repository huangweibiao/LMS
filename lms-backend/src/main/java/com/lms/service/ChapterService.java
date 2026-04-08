package com.lms.service;

import com.lms.common.BusinessException;
import com.lms.entity.Chapter;
import com.lms.repository.ChapterRepository;
import com.lms.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 章节服务
 */
@Service
public class ChapterService {
    
    private final ChapterRepository chapterRepository;
    private final CourseRepository courseRepository;
    
    public ChapterService(ChapterRepository chapterRepository, CourseRepository courseRepository) {
        this.chapterRepository = chapterRepository;
        this.courseRepository = courseRepository;
    }
    
    /**
     * 根据课程ID查询章节
     */
    public List<Chapter> getChaptersByCourse(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new BusinessException(404, "课程不存在");
        }
        return chapterRepository.findByCourseId(courseId);
    }
    
    /**
     * 根据ID查询章节
     */
    public Chapter getChapterById(Long id) {
        return chapterRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "章节不存在"));
    }
    
    /**
     * 创建章节
     */
    @Transactional
    public Chapter createChapter(Long courseId, Chapter chapter) {
        if (!courseRepository.existsById(courseId)) {
            throw new BusinessException(404, "课程不存在");
        }
        chapter.setCourseId(courseId);
        return chapterRepository.save(chapter);
    }
    
    /**
     * 更新章节
     */
    @Transactional
    public Chapter updateChapter(Long id, Chapter chapter) {
        Chapter existing = chapterRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "章节不存在"));
        
        existing.setTitle(chapter.getTitle());
        existing.setContentType(chapter.getContentType());
        existing.setContentUrl(chapter.getContentUrl());
        existing.setContentText(chapter.getContentText());
        existing.setDuration(chapter.getDuration());
        existing.setSortOrder(chapter.getSortOrder());
        existing.setIsFree(chapter.getIsFree());
        
        return chapterRepository.save(existing);
    }
    
    /**
     * 删除章节
     */
    @Transactional
    public void deleteChapter(Long id) {
        if (!chapterRepository.existsById(id)) {
            throw new BusinessException(404, "章节不存在");
        }
        chapterRepository.deleteById(id);
    }
}