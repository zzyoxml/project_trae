package com.ruoyi.edu.service;

import com.ruoyi.edu.domain.EduCourse;
import com.ruoyi.edu.service.IRecommendationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 推荐服务单元测试
 * 
 * @description 测试个性化推荐、薄弱点分析、学习路径生成等功能
 * @author LingLearn
 */
@SpringBootTest
public class RecommendationServiceTest {

    @Autowired
    private IRecommendationService recommendationService;

    /**
     * 测试获取个性化课程推荐
     */
    @Test
    public void testGetPersonalizedCourseRecommendations() {
        Long userId = 1L;
        Integer limit = 10;
        
        List<EduCourse> recommendations = recommendationService.getPersonalizedCourseRecommendations(userId, limit);
        
        assertNotNull(recommendations);
        assertTrue(recommendations.size() <= limit);
        
        // 验证推荐课程数据完整性
        for (EduCourse course : recommendations) {
            assertNotNull(course.getCourseId());
            assertNotNull(course.getCourseName());
            assertNotNull(course.getLanguage());
        }
    }

    /**
     * 测试获取每日学习任务
     */
    @Test
    public void testGetDailyLearningTasks() {
        Long userId = 1L;
        
        List<Map<String, Object>> tasks = recommendationService.getDailyLearningTasks(userId);
        
        assertNotNull(tasks);
        // 验证任务数据完整性
        for (Map<String, Object> task : tasks) {
            assertTrue(task.containsKey("type"));
            assertTrue(task.containsKey("description"));
            assertTrue(task.containsKey("priority"));
        }
    }

    /**
     * 测试获取薄弱点分析
     */
    @Test
    public void testGetWeakPointsAnalysis() {
        Long userId = 1L;
        String language = "en";
        
        Map<String, Object> analysis = recommendationService.getWeakPointsAnalysis(userId, language);
        
        assertNotNull(analysis);
        assertTrue(analysis.containsKey("skillScores"));
        assertTrue(analysis.containsKey("weakPoints"));
        assertTrue(analysis.containsKey("assessment"));
        
        // 验证技能评分范围
        @SuppressWarnings("unchecked")
        Map<String, Integer> skillScores = (Map<String, Integer>) analysis.get("skillScores");
        for (Integer score : skillScores.values()) {
            assertTrue(score >= 0 && score <= 100);
        }
    }

    /**
     * 测试获取技能雷达图数据
     */
    @Test
    public void testGetSkillRadarData() {
        Long userId = 1L;
        String language = "en";
        
        Map<String, Object> data = recommendationService.getSkillRadarData(userId, language);
        
        assertNotNull(data);
        assertTrue(data.containsKey("scores"));
        assertTrue(data.containsKey("overall"));
        
        // 验证综合评分范围
        assertTrue((Integer) data.get("overall") >= 0);
        assertTrue((Integer) data.get("overall") <= 100);
    }

    /**
     * 测试获取学习效率分析
     */
    @Test
    public void testGetLearningEfficiencyAnalysis() {
        Long userId = 1L;
        
        Map<String, Object> analysis = recommendationService.getLearningEfficiencyAnalysis(userId);
        
        assertNotNull(analysis);
        assertTrue(analysis.containsKey("efficiency"));
        assertTrue(analysis.containsKey("suggestions"));
        
        // 验证效率值
        assertTrue((Double) analysis.get("efficiency") >= 0);
    }

    /**
     * 测试获取个性化建议
     */
    @Test
    public void testGetPersonalizedSuggestions() {
        Long userId = 1L;
        
        List<String> suggestions = recommendationService.getPersonalizedSuggestions(userId);
        
        assertNotNull(suggestions);
        // 验证建议非空
        for (String suggestion : suggestions) {
            assertNotNull(suggestion);
            assertFalse(suggestion.isEmpty());
        }
    }

    /**
     * 测试计算课程匹配度
     */
    @Test
    public void testCalculateCourseMatchScore() {
        Long userId = 1L;
        Long courseId = 1L;
        
        int score = recommendationService.calculateCourseMatchScore(userId, courseId);
        
        // 验证匹配度范围
        assertTrue(score >= 0);
        assertTrue(score <= 100);
    }

    /**
     * 测试预测学习效果
     */
    @Test
    public void testPredictLearningOutcome() {
        Long userId = 1L;
        Long courseId = 1L;
        
        Map<String, Object> prediction = recommendationService.predictLearningOutcome(userId, courseId);
        
        assertNotNull(prediction);
        assertTrue(prediction.containsKey("matchScore"));
        assertTrue(prediction.containsKey("estimatedDays"));
        assertTrue(prediction.containsKey("successRate"));
        assertTrue(prediction.containsKey("prediction"));
        
        // 验证预测数据合理性
        assertTrue((Integer) prediction.get("matchScore") >= 0);
        assertTrue((Integer) prediction.get("matchScore") <= 100);
        assertTrue((Integer) prediction.get("estimatedDays") > 0);
        assertTrue((Double) prediction.get("successRate") >= 0);
        assertTrue((Double) prediction.get("successRate") <= 1);
    }

    /**
     * 测试生成学习路径
     */
    @Test
    public void testGenerateLearningPath() {
        Long userId = 1L;
        String targetLevel = "intermediate";
        String language = "en";
        
        var learningPath = recommendationService.generateLearningPath(userId, targetLevel, language);
        
        assertNotNull(learningPath);
        assertNotNull(learningPath.getPathName());
        assertEquals(targetLevel, learningPath.getTargetLevel());
        assertEquals(language, learningPath.getLanguage());
        assertTrue(learningPath.getEstimatedDuration() > 0);
    }

    /**
     * 测试获取复习计划
     */
    @Test
    public void testGetSpacedRepetitionPlan() {
        Long userId = 1L;
        String language = "en";
        
        List<Map<String, Object>> plan = recommendationService.getSpacedRepetitionPlan(userId, language);
        
        assertNotNull(plan);
        // 验证复习计划数据完整性
        for (Map<String, Object> review : plan) {
            assertTrue(review.containsKey("lessonId"));
            assertTrue(review.containsKey("nextReview"));
            assertTrue(review.containsKey("interval"));
        }
    }
}
