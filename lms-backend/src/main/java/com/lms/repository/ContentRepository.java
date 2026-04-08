package com.lms.repository;

import com.lms.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 内容Repository接口
 */
@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    
    /**
     * 根据章节ID查询内容
     */
    List<Content> findBySectionId(Long sectionId);
    
    /**
     * 删除章节的所有内容
     */
    void deleteBySectionId(Long sectionId);
}