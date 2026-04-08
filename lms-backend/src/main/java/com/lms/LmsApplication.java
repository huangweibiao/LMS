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

    /**
     * 应用程序入口点
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(LmsApplication.class, args);
        System.out.println("========================================");
        System.out.println("LMS学习管理系统启动成功!");
        System.out.println("访问地址: http://localhost:8080");
        System.out.println("========================================");
    }
}