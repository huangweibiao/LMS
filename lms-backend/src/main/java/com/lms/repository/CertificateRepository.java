package com.lms.repository;

import com.lms.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    List<Certificate> findByUserId(Long userId);
    List<Certificate> findByCourseId(Long courseId);
    Optional<Certificate> findByVerifyCode(String verifyCode);
    Optional<Certificate> findByCertificateNo(String certificateNo);
}