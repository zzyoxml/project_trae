package com.ruoyi.edu.service;

import com.ruoyi.edu.domain.EduAchievement;
import com.ruoyi.edu.service.IEduAchievementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 成就服务单元测试
 * 
 * @description 测试成就查询、进度检查、排行榜等功能
 * @author LingLearn
 */
@SpringBootTest
public class EduAchievementServiceTest {

    @Autowired
    private IEduAchievementService achievementService;

    /**
     * 测试获取成就列表
     */
    @Test
    public void testSelectEduAchievementList() {
        List<EduAchievement> achievements = achievementService.selectEduAchievementList(new EduAchievement());
        
        assertNotNull(achievements);
        assertTrue(achievements.size() > 0);
        
        // 验证成就数据完整性
        for (EduAchievement achievement : achievements) {
            assertNotNull(achievement.getAchievementId());
            assertNotNull(achievement.getAchievementName());
            assertNotNull(achievement.getAchievementType());
        }
    }

    /**
     * 测试按类型查询成就
     */
    @Test
    public void testSelectEduAchievementByType() {
        String type = "study";
        List<EduAchievement> achievements = achievementService.selectEduAchievementByType(type);
        
        assertNotNull(achievements);
        // 验证所有成就都是指定类型
        for (EduAchievement achievement : achievements) {
            assertEquals(type, achievement.getAchievementType());
        }
    }

    /**
     * 测试获取用户成就
     */
    @Test
    public void testGetUserAchievements() {
        Long userId = 1L;
        List<EduAchievement> achievements = achievementService.getUserAchievements(userId);
        
        assertNotNull(achievements);
        // 验证用户成就数据完整性
        for (EduAchievement achievement : achievements) {
            assertNotNull(achievement.getIsCompleted() != null);
        }
    }

    /**
     * 测试获取待完成成就
     */
    @Test
    public void testGetPendingAchievements() {
        Long userId = 1L;
        List<EduAchievement> achievements = achievementService.getPendingAchievements(userId);
        
        assertNotNull(achievements);
        // 验证待完成成就的进度
        for (EduAchievement achievement : achievements) {
            assertTrue(achievement.getProgress() < achievement.getRequirementValue());
        }
    }

    /**
     * 测试获取排行榜
     */
    @Test
    public void testGetLeaderboard() {
        String type = "total";
        String language = "all";
        Integer pageNum = 1;
        Integer pageSize = 20;
        
        Map<String, Object> leaderboard = achievementService.getLeaderboard(type, language, pageNum, pageSize);
        
        assertNotNull(leaderboard);
        assertTrue(leaderboard.containsKey("rankings"));
        assertTrue(leaderboard.containsKey("total"));
        assertTrue(leaderboard.containsKey("pageNum"));
    }

    /**
     * 测试检查成就进度
     */
    @Test
    public void testCheckAndUpdateAchievements() {
        Long userId = 1L;
        
        List<EduAchievement> newAchievements = achievementService.checkAndUpdateAchievements(userId);
        
        assertNotNull(newAchievements);
        // 新完成的成就应该都是已完成状态
        for (EduAchievement achievement : newAchievements) {
            assertTrue(achievement.getIsCompleted());
        }
    }

    /**
     * 测试初始化用户成就
     */
    @Test
    public void testInitializeUserAchievements() {
        Long userId = 1L;
        
        // 执行初始化
        achievementService.initializeUserAchievements(userId);
        
        // 验证初始化结果
        List<EduAchievement> achievements = achievementService.getUserAchievements(userId);
        assertNotNull(achievements);
        assertTrue(achievements.size() > 0);
    }
}
