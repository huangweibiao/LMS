package com.lms.service;

import com.lms.common.BusinessException;
import com.lms.entity.Enrollment;
import com.lms.repository.EnrollmentRepository;
import com.lms.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 选课服务
 */
@Service
public class EnrollmentService {
    
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    
    public EnrollmentService(EnrollmentRepository enrollmentRepository, CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
    }
    
    /**
     * 用户选课
     */
    @Transactional
    public Enrollment enrollCourse(Long userId, Long courseId) {
        // 检查课程是否存在
        if (!courseRepository.existsById(courseId)) {
            throw new BusinessException(404, "课程不存在");
        }
        
        // 检查是否已选课
        if (enrollmentRepository.existsByUserIdAndCourseId(userId, courseId)) {
            throw new BusinessException(400, "已选择该课程");
        }
        
        Enrollment enrollment = new Enrollment();
        enrollment.setUserId(userId);
        enrollment.setCourseId(courseId);
        enrollment.setStatus(Enrollment.EnrollmentStatus.ACTIVE);
        
        Enrollment saved = enrollmentRepository.save(enrollment);
        
        // 更新课程学习人数
        courseRepository.findById(courseId).ifPresent(course -> {
            course.setTotalStudents(course.getTotalStudents() + 1);
            courseRepository.save(course);
        });
        
        return saved;
    }
    
    /**
     * 查询用户的选课列表
     */
    public List<Enrollment> getEnrollmentsByUser(Long userId) {
        return enrollmentRepository.findByUserId(userId);
    }
    
    /**
     * 查询课程的选课列表
     */
    public List<Enrollment> getEnrollmentsByCourse(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }
    
    /**
     * 获取用户的课程选课记录
     */
    public Enrollment getEnrollment(Long userId, Long courseId) {
        return enrollmentRepository.findByUserIdAndCourseId(userId, courseId)
                .orElse(null);
    }
    
    /**
     * 更新学习进度
     */
    @Transactional
    public void updateProgress(Long userId, Long courseId, Integer progress) {
        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new BusinessException(404, "选课记录不存在"));
        
        enrollment.setProgress(progress);
        enrollment.setLastLearnTime(new Date());
        
        if (progress >= 100) {
            enrollment.setStatus(Enrollment.EnrollmentStatus.COMPLETED);
            enrollment.setCompleteTime(new Date());
        }
        
        enrollmentRepository.save(enrollment);
    }
    
    /**
     * 取消选课
     */
    @Transactional
    public void cancelEnrollment(Long userId, Long courseId) {
        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new BusinessException(404, "选课记录不存在"));
        
        enrollment.setStatus(Enrollment.EnrollmentStatus.DROPPED);
        enrollmentRepository.save(enrollment);
    }
}