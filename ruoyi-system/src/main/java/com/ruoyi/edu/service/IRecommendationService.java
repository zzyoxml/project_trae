package com.ruoyi.edu.service;

import com.ruoyi.edu.domain.EduCourse;
import com.ruoyi.edu.domain.EduLearningPath;
import java.util.List;
import java.util.Map;

/**
 * 个性化学习推荐服务接口
 * 
 * @description 提供智能学习推荐、薄弱点分析、学习路径规划等功能
 * @author LingLearn
 */
public interface IRecommendationService {

    /**
     * 获取个性化课程推荐
     *
     * @param userId 用户ID
     * @param limit 推荐数量
     * @return 推荐课程列表
     */
    List<EduCourse> getPersonalizedCourseRecommendations(Long userId, int limit);

    /**
     * 获取每日学习任务
     *
     * @param userId 用户ID
     * @return 每日任务列表
     */
    List<Map<String, Object>> getDailyLearningTasks(Long userId);

    /**
     * 获取学习薄弱点分析
     *
     * @param userId 用户ID
     * @param language 学习语言
     * @return 薄弱点分析结果
     */
    Map<String, Object> getWeakPointsAnalysis(Long userId, String language);

    /**
     * 生成个性化学习路径
     *
     * @param userId 用户ID
     * @param targetLevel 目标等级
     * @param language 学习语言
     * @return 学习路径
     */
    EduLearningPath generateLearningPath(Long userId, String targetLevel, String language);

    /**
     * 获取复习计划（基于艾宾浩斯遗忘曲线）
     *
     * @param userId 用户ID
     * @param language 学习语言
     * @return 复习计划
     */
    List<Map<String, Object>> getSpacedRepetitionPlan(Long userId, String language);

    /**
     * 获取技能雷达图数据
     *
     * @param userId 用户ID
     * @param language 学习语言
     * @return 技能评分数据
     */
    Map<String, Object> getSkillRadarData(Long userId, String language);

    /**
     * 预测学习效果
     *
     * @param userId 用户ID
     * @param courseId 课程ID
     * @return 预测结果
     */
    Map<String, Object> predictLearningOutcome(Long userId, Long courseId);

    /**
     * 获取学习效率分析
     *
     * @param userId 用户ID
     * @return 效率分析结果
     */
    Map<String, Object> getLearningEfficiencyAnalysis(Long userId);

    /**
     * 获取个性化学习建议
     *
     * @param userId 用户ID
     * @return 学习建议列表
     */
    List<String> getPersonalizedSuggestions(Long userId);

    /**
     * 更新用户学习画像
     *
     * @param userId 用户ID
     */
    void updateUserLearningProfile(Long userId);

    /**
     * 计算课程匹配度
     *
     * @param userId 用户ID
     * @param courseId 课程ID
     * @return 匹配度分数 (0-100)
     */
    int calculateCourseMatchScore(Long userId, Long courseId);
}
