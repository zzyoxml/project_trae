package com.ruoyi.edu.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 教育平台日期工具类
 * 提供学习相关的日期计算功能
 *
 * @author LingLearn
 */
public class EduDateUtil {

    /**
     * 日期格式：yyyy-MM-dd
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 日期格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取今天的日期字符串
     *
     * @return 日期字符串
     */
    public static String getToday() {
        return formatDate(new Date());
    }

    /**
     * 格式化日期为字符串
     *
     * @param date 日期
     * @return 格式化的日期字符串
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }

    /**
     * 格式化日期时间为字符串
     *
     * @param date 日期
     * @return 格式化的日期时间字符串
     */
    public static String formatDateTime(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
        return sdf.format(date);
    }

    /**
     * 计算两个日期之间的天数
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 天数
     */
    public static long daysBetween(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }

        long diff = endDate.getTime() - startDate.getTime();
        return diff / (1000 * 60 * 60 * 24);
    }

    /**
     * 检查是否是连续的日期
     *
     * @param lastDate 上次日期
     * @param today    今天
     * @return 是否连续
     */
    public static boolean isConsecutive(String lastDate, String today) {
        if (lastDate == null || today == null) {
            return false;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            Date last = sdf.parse(lastDate);
            Date current = sdf.parse(today);

            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(last);
            cal1.add(Calendar.DAY_OF_MONTH, 1);

            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(current);

            return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                    && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取几天后的日期
     *
     * @param date 基准日期
     * @param days 天数
     * @return 目标日期
     */
    public static Date addDays(Date date, int days) {
        if (date == null) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    /**
     * 计算下次复习时间（基于艾宾浩斯遗忘曲线）
     *
     * @param reviewCount 当前复习次数
     * @return 下次复习的毫秒数
     */
    public static long calculateNextReviewTime(int reviewCount) {
        // 艾宾浩斯遗忘曲线复习间隔：1天、2天、4天、7天、15天、30天
        int[] intervals = {1, 2, 4, 7, 15, 30};
        int days = intervals[Math.min(reviewCount, intervals.length - 1)];

        return System.currentTimeMillis() + (days * 24 * 60 * 60 * 1000L);
    }

    /**
     * 检查是否是新的一天
     *
     * @param lastDate 上次日期
     * @return 是否是新的一天
     */
    public static boolean isNewDay(String lastDate) {
        if (lastDate == null) {
            return true;
        }

        String today = getToday();
        return !today.equals(lastDate);
    }

    /**
     * 获取本周开始日期
     *
     * @return 本周开始日期
     */
    public static Date getWeekStart() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取本月开始日期
     *
     * @return 本月开始日期
     */
    public static Date getMonthStart() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }
}
