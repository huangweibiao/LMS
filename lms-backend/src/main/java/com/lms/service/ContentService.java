package com.lms.service;

import com.lms.common.BusinessException;
import com.lms.entity.Content;
import com.lms.repository.ContentRepository;
import com.lms.repository.ChapterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 内容服务
 */
@Service
public class ContentService {
    
    private final ContentRepository contentRepository;
    private final ChapterRepository chapterRepository;
    
    public ContentService(ContentRepository contentRepository, ChapterRepository chapterRepository) {
        this.contentRepository = contentRepository;
        this.chapterRepository = chapterRepository;
    }
    
    /**
     * 根据章节ID查询内容
     */
    public List<Content> getContentsBySection(Long sectionId) {
        if (!chapterRepository.existsById(sectionId)) {
            throw new BusinessException(404, "章节不存在");
        }
        return contentRepository.findBySectionId(sectionId);
    }
    
    /**
     * 根据ID查询内容
     */
    public Content getContentById(Long id) {
        return contentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "内容不存在"));
    }
    
    /**
     * 创建内容
     */
    @Transactional
    public Content createContent(Long sectionId, Content content) {
        if (!chapterRepository.existsById(sectionId)) {
            throw new BusinessException(404, "章节不存在");
        }
        content.setSectionId(sectionId);
        return contentRepository.save(content);
    }
    
    /**
     * 更新内容
     */
    @Transactional
    public Content updateContent(Long id, Content content) {
        Content existing = contentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "内容不存在"));
        
        existing.setTitle(content.getTitle());
        existing.setType(content.getType());
        existing.setUrl(content.getUrl());
        existing.setDuration(content.getDuration());
        existing.setFileSize(content.getFileSize());
        existing.setIsRequired(content.getIsRequired());
        
        return contentRepository.save(existing);
    }
    
    /**
     * 删除内容
     */
    @Transactional
    public void deleteContent(Long id) {
        if (!contentRepository.existsById(id)) {
            throw new BusinessException(404, "内容不存在");
        }
        contentRepository.deleteById(id);
    }
}