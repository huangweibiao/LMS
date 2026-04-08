package com.lms.service;

import com.lms.common.BusinessException;
import com.lms.entity.LearningRecord;
import com.lms.repository.LearningRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 学习记录服务
 */
@Service
public class LearningRecordService {
    
    private final LearningRecordRepository learningRecordRepository;
    
    public LearningRecordService(LearningRecordRepository learningRecordRepository) {
        this.learningRecordRepository = learningRecordRepository;
    }
    
    /**
     * 记录学习（创建或更新）
     */
    @Transactional
    public LearningRecord recordLearning(Long userId, Long courseId, Long chapterId, Long contentId, 
                                         Integer watchDuration, Integer lastPosition) {
        // 查找现有记录
        Optional<LearningRecord> existing = learningRecordRepository
                .findByUserIdAndCourseIdAndChapterId(userId, courseId, chapterId);
        
        LearningRecord record;
        if (existing.isPresent()) {
            record = existing.get();
            // 更新观看时长和最后位置
            record.setWatchDuration(record.getWatchDuration() + watchDuration);
            record.setLastPosition(lastPosition);
            // 如果播放位置超过90%，标记为完成
            if (lastPosition > 0 && watchDuration > 0) {
                record.setIsCompleted(1);
            }
        } else {
            // 创建新记录
            record = new LearningRecord();
            record.setUserId(userId);
            record.setCourseId(courseId);
            record.setChapterId(chapterId);
            record.setContentId(contentId);
            record.setWatchDuration(watchDuration);
            record.setLastPosition(lastPosition);
            record.setIsCompleted(0);
        }
        
        record.setUpdateTime(new Date());
        return learningRecordRepository.save(record);
    }
    
    /**
     * 查询用户课程的学习记录
     */
    public List<LearningRecord> getLearningRecords(Long userId, Long courseId) {
        return learningRecordRepository.findByUserIdAndCourseId(userId, courseId);
    }
    
    /**
     * 获取用户课程已完成的章节数
     */
    public long getCompletedChapterCount(Long userId, Long courseId) {
        return learningRecordRepository.countByUserIdAndCourseIdAndIsCompleted(userId, courseId, 1);
    }
    
    /**
     * 标记章节为完成
     */
    @Transactional
    public void markChapterCompleted(Long userId, Long courseId, Long chapterId) {
        Optional<LearningRecord> existing = learningRecordRepository
                .findByUserIdAndCourseIdAndChapterId(userId, courseId, chapterId);
        
        LearningRecord record;
        if (existing.isPresent()) {
            record = existing.get();
            record.setIsCompleted(1);
            record.setUpdateTime(new Date());
            learningRecordRepository.save(record);
        } else {
            record = new LearningRecord();
            record.setUserId(userId);
            record.setCourseId(courseId);
            record.setChapterId(chapterId);
            record.setIsCompleted(1);
            learningRecordRepository.save(record);
        }
    }
}