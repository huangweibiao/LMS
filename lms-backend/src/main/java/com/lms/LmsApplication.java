package com.lms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * LMS学习管理系统启动类
 * Spring Boot 3.5.11
 * 支持数据库自动创建
 */
@SpringBootApplication
public class LmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(LmsApplication.class, args);
        System.out.println("========================================");
        System.out.println("LMS学习管理系统启动成功!");
        System.out.println("访问地址: http://localhost:8880");
        System.out.println("========================================");
    }
}