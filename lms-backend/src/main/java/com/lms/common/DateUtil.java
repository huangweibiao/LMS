package com.lms.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期工具类（不使用Hutool）
 */
public class DateUtil {
    
    /** 日期格式：yyyy-MM-dd */
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    
    /** 日期时间格式：yyyy-MM-dd HH:mm:ss */
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    
    /** 日期时间格式（无秒）：yyyy-MM-dd HH:mm */
    public static final String DATETIME_PATTERN_SHORT = "yyyy-MM-dd HH:mm";
    
    /** 时间格式：HH:mm:ss */
    public static final String TIME_PATTERN = "HH:mm:ss";
    
    /**
     * 格式化日期为 yyyy-MM-dd
     * @param date 日期对象
     * @return 格式化后的字符串
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        return sdf.format(date);
    }
    
    /**
     * 格式化日期为 yyyy-MM-dd HH:mm:ss
     * @param date 日期对象
     * @return 格式化后的字符串
     */
    public static String formatDateTime(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_PATTERN);
        return sdf.format(date);
    }
    
    /**
     * 解析日期字符串
     * @param dateStr 日期字符串
     * @return 日期对象
     */
    public static Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("日期解析失败: " + dateStr, e);
        }
    }
    
    /**
     * 解析日期时间字符串
     * @param dateTimeStr 日期时间字符串
     * @return 日期对象
     */
    public static Date parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_PATTERN);
            return sdf.parse(dateTimeStr);
        } catch (ParseException e) {
            throw new RuntimeException("日期时间解析失败: " + dateTimeStr, e);
        }
    }
    
    /**
     * 获取当前日期
     * @return 当前日期
     */
    public static Date now() {
        return new Date();
    }
    
    /**
     * 获取当前日期字符串 yyyy-MM-dd
     * @return 当前日期字符串
     */
    public static String getCurrentDate() {
        return formatDate(new Date());
    }
    
    /**
     * 获取当前日期时间字符串 yyyy-MM-dd HH:mm:ss
     * @return 当前日期时间字符串
     */
    public static String getCurrentDateTime() {
        return formatDateTime(new Date());
    }
    
    /**
     * 日期加天数
     * @param date 基准日期
     * @param days 要增加的天数（负数表示减）
     * @return 结果日期
     */
    public static Date addDays(Date date, int days) {
        if (date == null) {
            return null;
        }
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate result = localDate.plusDays(days);
        return Date.from(result.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    
    /**
     * 日期加小时
     * @param date 基准日期
     * @param hours 要增加的小时数
     * @return 结果日期
     */
    public static Date addHours(Date date, int hours) {
        if (date == null) {
            return null;
        }
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime result = localDateTime.plusHours(hours);
        return Date.from(result.atZone(ZoneId.systemDefault()).toInstant());
    }
    
    /**
     * 计算两个日期之间的天数
     * @param start 开始日期
     * @param end 结束日期
     * @return 天数差
     */
    public static long daysBetween(Date start, Date end) {
        if (start == null || end == null) {
            return 0;
        }
        long diff = end.getTime() - start.getTime();
        return diff / (1000 * 60 * 60 * 24);
    }
    
    /**
     * 判断日期是否在某个范围内
     * @param date 要判断的日期
     * @param start 开始日期
     * @param end 结束日期
     * @return 是否在范围内
     */
    public static boolean isBetween(Date date, Date start, Date end) {
        if (date == null || start == null || end == null) {
            return false;
        }
        return !date.before(start) && !date.after(end);
    }
    
    /**
     * 获取日期的开始时间（00:00:00）
     * @param date 日期
     * @return 开始时间
     */
    public static Date getStartOfDay(Date date) {
        if (date == null) {
            return null;
        }
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    
    /**
     * 获取日期的结束时间（23:59:59）
     * @param date 日期
     * @return 结束时间
     */
    public static Date getEndOfDay(Date date) {
        if (date == null) {
            return null;
        }
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime endOfDay = localDate.atTime(23, 59, 59);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }
}