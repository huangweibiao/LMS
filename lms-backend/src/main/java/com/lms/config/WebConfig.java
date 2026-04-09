package com.lms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置
 * 配置静态资源和路由 fallback
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 配置静态资源映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射静态资源
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    /**
     * 配置根路径 fallback 到 index.html
     * 解决Vue Router History模式的刷新404问题
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 所有前端路由都返回 index.html
        registry.addViewController("/")
                .setViewName("forward:/index.html");
    }
}