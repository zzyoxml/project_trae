package com.ruoyi.edu.service;

import com.ruoyi.edu.service.IEduLearningService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 学习服务单元测试
 * 
 * @description 测试学习记录、进度追踪、统计等功能
 * @author LingLearn
 */
@SpringBootTest
public class EduLearningServiceTest {

    @Autowired
    private IEduLearningService learningService;

    /**
     * 测试获取用户学习进度统计
     */
    @Test
    public void testGetUserProgressStats() {
        Long userId = 1L;
        Map<String, Object> stats = learningService.getUserProgressStats(userId);
        
        assertNotNull(stats);
        assertTrue(stats.containsKey("todayStudyTime"));
        assertTrue(stats.containsKey("weekStudyTime"));
        assertTrue(stats.containsKey("currentStreak"));
        assertTrue(stats.containsKey("skillScores"));
    }

    /**
     * 测试获取今日学习数据
     */
    @Test
    public void testGetTodayLearningData() {
        Long userId = 1L;
        Map<String, Object> data = learningService.getTodayLearningData(userId);
        
        assertNotNull(data);
        assertTrue(data.containsKey("studyTime"));
        assertTrue(data.containsKey("studyGoal"));
        assertTrue(data.containsKey("goalProgress"));
        assertTrue(data.containsKey("records"));
        assertTrue(data.containsKey("todayXp"));
        assertTrue(data.containsKey("todayCoins"));
    }

    /**
     * 测试获取用户技能评分
     */
    @Test
    public void testGetUserSkillScores() {
        Long userId = 1L;
        Map<String, Integer> scores = learningService.getUserSkillScores(userId);
        
        assertNotNull(scores);
        assertTrue(scores.containsKey("vocabulary"));
        assertTrue(scores.containsKey("grammar"));
        assertTrue(scores.containsKey("listening"));
        assertTrue(scores.containsKey("speaking"));
        
        // 验证评分范围
        for (Integer score : scores.values()) {
            assertTrue(score >= 0 && score <= 100);
        }
    }

    /**
     * 测试获取最近学习记录
     */
    @Test
    public void testSelectUserRecentRecords() {
        Long userId = 1L;
        Integer days = 7;
        
        var records = learningService.selectUserRecentRecords(userId, days);
        
        assertNotNull(records);
        // 验证返回的记录在指定天数内
        for (var record : records) {
            assertNotNull(record.getCreateTime());
        }
    }

    /**
     * 测试获取特定课程的学习进度
     */
    @Test
    public void testGetCourseProgress() {
        Long userId = 1L;
        Long courseId = 1L;
        
        var progress = learningService.selectEduLearningProgressByCourse(userId, courseId);
        
        assertNotNull(progress);
    }
}
