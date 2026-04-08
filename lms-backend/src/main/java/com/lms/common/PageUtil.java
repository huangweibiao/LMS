package com.lms.common;

import java.util.List;

/**
 * 分页工具类
 * 用于分页查询的统一返回格式
 * @param <T> 数据类型
 */
public class PageUtil<T> {
    
    /** 数据列表 */
    private List<T> list;
    
    /** 总记录数 */
    private Long total;
    
    /** 当前页码 */
    private Integer page;
    
    /** 每页大小 */
    private Integer size;
    
    /** 总页数 */
    private Integer pages;
    
    /**
     * 构造分页结果
     * @param list 数据列表
     * @param total 总记录数
     * @param page 当前页码
     * @param size 每页大小
     */
    public PageUtil(List<T> list, Long total, Integer page, Integer size) {
        this.list = list;
        this.total = total;
        this.page = page;
        this.size = size;
        this.pages = (int) Math.ceil((double) total / size);
    }
    
    /**
     * 创建一个空的分页结果
     */
    public static <T> PageUtil<T> empty() {
        return new PageUtil<>(List.of(), 0L, 1, 10);
    }
    
    /**
     * 创建一个空的分页结果（指定分页参数）
     */
    public static <T> PageUtil<T> empty(Integer page, Integer size) {
        return new PageUtil<>(List.of(), 0L, page, size);
    }
    
    // Getter和Setter方法
    public List<T> getList() {
        return list;
    }
    
    public void setList(List<T> list) {
        this.list = list;
    }
    
    public Long getTotal() {
        return total;
    }
    
    public void setTotal(Long total) {
        this.total = total;
    }
    
    public Integer getPage() {
        return page;
    }
    
    public void setPage(Integer page) {
        this.page = page;
    }
    
    public Integer getSize() {
        return size;
    }
    
    public void setSize(Integer size) {
        this.size = size;
    }
    
    public Integer getPages() {
        return pages;
    }
    
    public void setPages(Integer pages) {
        this.pages = pages;
    }
    
    /**
     * 是否有上一页
     */
    public boolean hasPrevious() {
        return page > 1;
    }
    
    /**
     * 是否有下一页
     */
    public boolean hasNext() {
        return page < pages;
    }
}