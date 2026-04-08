package com.lms.repository;

import com.lms.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户扩展信息Repository接口
 */
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    
    /**
     * 根据用户ID查询扩展信息
     */
    Optional<UserProfile> findByUserId(Long userId);
    
    /**
     * 判断用户扩展信息是否存在
     */
    boolean existsByUserId(Long userId);
}