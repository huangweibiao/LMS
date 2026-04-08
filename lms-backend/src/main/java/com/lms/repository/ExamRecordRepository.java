package com.lms.repository;

import com.lms.entity.ExamRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRecordRepository extends JpaRepository<ExamRecord, Long> {
    List<ExamRecord> findByExamId(Long examId);
    List<ExamRecord> findByUserId(Long userId);
    Optional<ExamRecord> findByExamIdAndUserId(Long examId, Long userId);
}