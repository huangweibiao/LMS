package com.lms.service;

import com.lms.common.BusinessException;
import com.lms.common.PageUtil;
import com.lms.dto.request.CourseRequest;
import com.lms.dto.response.CourseResponse;
import com.lms.entity.Course;
import com.lms.entity.CourseCategory;
import com.lms.entity.SysUser;
import com.lms.repository.CourseCategoryRepository;
import com.lms.repository.CourseRepository;
import com.lms.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 课程服务
 * 处理课程CRUD、分类管理
 */
@Service
public class CourseService {
    
    private final CourseRepository courseRepository;
    private final CourseCategoryRepository categoryRepository;
    private final UserRepository userRepository;
    
    public CourseService(CourseRepository courseRepository, CourseCategoryRepository categoryRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }
    
    /**
     * 分页查询课程
     */
    public PageUtil<CourseResponse> getCourses(Integer page, Integer size, Long categoryId, String status) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Course> coursePage;
        
        if (status != null) {
            coursePage = courseRepository.findByStatus(Course.CourseStatus.valueOf(status), pageRequest);
        } else {
            coursePage = courseRepository.findAll(pageRequest);
        }
        
        List<CourseResponse> list = coursePage.getContent().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        
        return new PageUtil<>(list, coursePage.getTotalElements(), page, size);
    }
    
    /**
     * 根据ID查询课程
     */
    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "课程不存在"));
        return convertToResponse(course);
    }
    
    /**
     * 根据分类ID查询课程
     */
    public List<CourseResponse> getCoursesByCategory(Long categoryId) {
        return courseRepository.findByCategoryId(categoryId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据讲师ID查询课程
     */
    public List<CourseResponse> getCoursesByTeacher(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 创建课程
     */
    @Transactional
    public CourseResponse createCourse(CourseRequest request, Long teacherId) {
        // 验证分类
        if (categoryRepository.findById(request.getCategoryId()).isEmpty()) {
            throw new BusinessException(400, "分类不存在");
        }
        
        // 验证讲师
        if (userRepository.findById(teacherId).isEmpty()) {
            throw new BusinessException(400, "讲师不存在");
        }
        
        Course course = new Course();
        course.setCategoryId(request.getCategoryId());
        course.setTeacherId(teacherId);
        course.setTitle(request.getTitle());
        course.setSubtitle(request.getSubtitle());
        course.setCoverImage(request.getCoverImage());
        course.setIntroVideo(request.getIntroVideo());
        course.setDescription(request.getDescription());
        course.setLevel(Course.CourseLevel.valueOf(request.getLevel() != null ? request.getLevel() : "BEGINNER"));
        course.setPrice(request.getPrice() != null ? request.getPrice() : java.math.BigDecimal.ZERO);
        course.setStatus(Course.CourseStatus.DRAFT);
        
        course = courseRepository.save(course);
        return convertToResponse(course);
    }
    
    /**
     * 更新课程
     */
    @Transactional
    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "课程不存在"));
        
        course.setTitle(request.getTitle());
        course.setSubtitle(request.getSubtitle());
        course.setCoverImage(request.getCoverImage());
        course.setIntroVideo(request.getIntroVideo());
        course.setDescription(request.getDescription());
        
        if (request.getLevel() != null) {
            course.setLevel(Course.CourseLevel.valueOf(request.getLevel()));
        }
        
        if (request.getPrice() != null) {
            course.setPrice(request.getPrice());
        }
        
        course = courseRepository.save(course);
        return convertToResponse(course);
    }
    
    /**
     * 发布课程
     */
    @Transactional
    public CourseResponse publishCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "课程不存在"));
        
        course.setStatus(Course.CourseStatus.PUBLISHED);
        course.setPublishTime(new Date());
        
        course = courseRepository.save(course);
        return convertToResponse(course);
    }
    
    /**
     * 删除课程
     */
    @Transactional
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new BusinessException(404, "课程不存在");
        }
        courseRepository.deleteById(id);
    }
    
    /**
     * 转换实体为响应DTO
     */
    private CourseResponse convertToResponse(Course course) {
        CourseResponse response = new CourseResponse();
        response.setId(course.getId());
        response.setCategoryId(course.getCategoryId());
        response.setTeacherId(course.getTeacherId());
        response.setTitle(course.getTitle());
        response.setSubtitle(course.getSubtitle());
        response.setCoverImage(course.getCoverImage());
        response.setIntroVideo(course.getIntroVideo());
        response.setDescription(course.getDescription());
        response.setLevel(course.getLevel().name());
        response.setPrice(course.getPrice());
        response.setTotalDuration(course.getTotalDuration());
        response.setTotalStudents(course.getTotalStudents());
        response.setRating(course.getRating());
        response.setStatus(course.getStatus().name());
        response.setPublishTime(course.getPublishTime());
        response.setCreateTime(course.getCreateTime());
        response.setUpdateTime(course.getUpdateTime());
        
        // 填充分类名称和讲师名称
        categoryRepository.findById(course.getCategoryId())
                .ifPresent(cat -> response.setCategoryName(cat.getName()));
        userRepository.findById(course.getTeacherId())
                .ifPresent(teacher -> response.setTeacherName(teacher.getRealName()));
        
        return response;
    }
}