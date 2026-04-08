package com.lms.repository;

import com.lms.entity.LearningAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LearningAnalyticsRepository extends JpaRepository<LearningAnalytics, Long> {
    Optional<LearningAnalytics> findByUserIdAndCourseIdAndDate(Long userId, Long courseId, java.util.Date date);
}