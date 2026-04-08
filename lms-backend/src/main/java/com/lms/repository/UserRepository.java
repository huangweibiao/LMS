package com.lms.repository;

import com.lms.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户Repository接口
 * 对用户表进行数据操作
 */
@Repository
public interface UserRepository extends JpaRepository<SysUser, Long> {
    
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象
     */
    Optional<SysUser> findByUsername(String username);
    
    /**
     * 根据邮箱查询用户
     * @param email 邮箱
     * @return 用户对象
     */
    Optional<SysUser> findByEmail(String email);
    
    /**
     * 根据手机号查询用户
     * @param mobile 手机号
     * @return 用户对象
     */
    Optional<SysUser> findByMobile(String mobile);
    
    /**
     * 判断用户名是否存在
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);
    
    /**
     * 判断邮箱是否存在
     * @param email 邮箱
     * @return 是否存在
     */
    boolean existsByEmail(String email);
}