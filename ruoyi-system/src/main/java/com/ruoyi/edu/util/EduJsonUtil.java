package com.ruoyi.edu.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 教育平台JSON工具类
 * 提供JSON数据的解析和转换功能
 *
 * @author LingLearn
 */
public class EduJsonUtil {

    /**
     * 解析JSON数组为List
     *
     * @param jsonString JSON字符串
     * @param clazz      目标类型
     * @param <T>        泛型类型
     * @return 解析后的列表
     */
    public static <T> List<T> parseArray(String jsonString, Class<T> clazz) {
        if (jsonString == null || jsonString.isEmpty()) {
            return new ArrayList<>();
        }

        try {
            return JSON.parseArray(jsonString, clazz);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * 将对象转换为JSON字符串
     *
     * @param obj 对象
     * @return JSON字符串
     */
    public static String toJsonString(Object obj) {
        if (obj == null) {
            return null;
        }
        return JSON.toJSONString(obj);
    }

    /**
     * 从JSON字符串中获取字符串值
     *
     * @param jsonString JSON字符串
     * @param key        键
     * @return 字符串值
     */
    public static String getString(String jsonString, String key) {
        if (jsonString == null || jsonString.isEmpty()) {
            return null;
        }

        try {
            JSONObject jsonObject = JSON.parseObject(jsonString);
            return jsonObject != null ? jsonObject.getString(key) : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从JSON字符串中获取整数值
     *
     * @param jsonString JSON字符串
     * @param key        键
     * @return 整数值
     */
    public static Integer getInteger(String jsonString, String key) {
        if (jsonString == null || jsonString.isEmpty()) {
            return null;
        }

        try {
            JSONObject jsonObject = JSON.parseObject(jsonString);
            return jsonObject != null ? jsonObject.getInteger(key) : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 创建简单的JSON数组字符串
     *
     * @param items 项目列表
     * @param <T>   项目类型
     * @return JSON数组字符串
     */
    @SafeVarargs
    public static <T> String createArrayString(T... items) {
        if (items == null || items.length == 0) {
            return "[]";
        }
        return JSON.toJSONString(items);
    }

    /**
     * 检查字符串是否为有效的JSON
     *
     * @param str 字符串
     * @return 是否有效
     */
    public static boolean isValidJson(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        try {
            JSON.parse(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 解析JSON数组字符串为字符串列表
     *
     * @param jsonArrayString JSON数组字符串
     * @return 字符串列表
     */
    public static List<String> parseStringArray(String jsonArrayString) {
        if (jsonArrayString == null || jsonArrayString.isEmpty()) {
            return new ArrayList<>();
        }

        try {
            JSONArray jsonArray = JSON.parseArray(jsonArrayString);
            List<String> result = new ArrayList<>();
            if (jsonArray != null) {
                for (Object item : jsonArray) {
                    result.add(item.toString());
                }
            }
            return result;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * 将字符串列表转换为JSON数组字符串
     *
     * @param list 字符串列表
     * @return JSON数组字符串
     */
    public static String listToJsonArray(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }
        return JSON.toJSONString(list);
    }
}
