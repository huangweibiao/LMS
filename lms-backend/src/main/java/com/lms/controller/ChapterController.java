package com.lms.controller;

import com.lms.common.Result;
import com.lms.entity.Chapter;
import com.lms.service.ChapterService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 章节控制器
 * 处理章节管理相关请求
 */
@RestController
@RequestMapping("/api/chapters")
public class ChapterController {
    
    private final ChapterService chapterService;
    
    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }
    
    /**
     * 根据课程ID获取章节列表
     * GET /api/chapters/course/{courseId}
     */
    @GetMapping("/course/{courseId}")
    public Result<List<Chapter>> getChaptersByCourse(@PathVariable Long courseId) {
        List<Chapter> chapters = chapterService.getChaptersByCourse(courseId);
        return Result.success(chapters);
    }
    
    /**
     * 获取章节详情
     * GET /api/chapters/{id}
     */
    @GetMapping("/{id}")
    public Result<Chapter> getChapterById(@PathVariable Long id) {
        Chapter chapter = chapterService.getChapterById(id);
        return Result.success(chapter);
    }
    
    /**
     * 创建章节
     * POST /api/chapters
     */
    @PostMapping
    public Result<Chapter> createChapter(@RequestParam Long courseId, @RequestBody Chapter chapter) {
        Chapter created = chapterService.createChapter(courseId, chapter);
        return Result.success("创建成功", created);
    }
    
    /**
     * 更新章节
     * PUT /api/chapters/{id}
     */
    @PutMapping("/{id}")
    public Result<Chapter> updateChapter(@PathVariable Long id, @RequestBody Chapter chapter) {
        Chapter updated = chapterService.updateChapter(id, chapter);
        return Result.success("更新成功", updated);
    }
    
    /**
     * 删除章节
     * DELETE /api/chapters/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteChapter(@PathVariable Long id) {
        chapterService.deleteChapter(id);
        return Result.success("删除成功", null);
    }
}