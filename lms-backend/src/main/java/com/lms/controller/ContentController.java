package com.lms.controller;

import com.lms.common.Result;
import com.lms.entity.Content;
import com.lms.service.ContentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 内容控制器
 * 处理内容管理相关请求
 */
@RestController
@RequestMapping("/api/contents")
public class ContentController {
    
    private final ContentService contentService;
    
    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }
    
    /**
     * 根据章节ID获取内容列表
     * GET /api/contents/section/{sectionId}
     */
    @GetMapping("/section/{sectionId}")
    public Result<List<Content>> getContentsBySection(@PathVariable Long sectionId) {
        List<Content> contents = contentService.getContentsBySection(sectionId);
        return Result.success(contents);
    }
    
    /**
     * 获取内容详情
     * GET /api/contents/{id}
     */
    @GetMapping("/{id}")
    public Result<Content> getContentById(@PathVariable Long id) {
        Content content = contentService.getContentById(id);
        return Result.success(content);
    }
    
    /**
     * 创建内容
     * POST /api/contents
     */
    @PostMapping
    public Result<Content> createContent(@RequestParam Long sectionId, @RequestBody Content content) {
        Content created = contentService.createContent(sectionId, content);
        return Result.success("创建成功", created);
    }
    
    /**
     * 更新内容
     * PUT /api/contents/{id}
     */
    @PutMapping("/{id}")
    public Result<Content> updateContent(@PathVariable Long id, @RequestBody Content content) {
        Content updated = contentService.updateContent(id, content);
        return Result.success("更新成功", updated);
    }
    
    /**
     * 删除内容
     * DELETE /api/contents/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteContent(@PathVariable Long id) {
        contentService.deleteContent(id);
        return Result.success("删除成功", null);
    }
}