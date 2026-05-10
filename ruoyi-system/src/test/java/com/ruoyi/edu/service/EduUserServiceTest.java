package com.ruoyi.edu.service;

import com.ruoyi.edu.domain.EduUserProfile;
import com.ruoyi.edu.service.IEduUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户服务单元测试
 * 
 * @description 测试用户注册、登录、信息管理等功能
 * @author LingLearn
 */
@SpringBootTest
public class EduUserServiceTest {

    @Autowired
    private IEduUserService userService;

    /**
     * 测试获取用户扩展信息
     */
    @Test
    public void testSelectEduUserProfileByUserId() {
        // 测试存在用户
        EduUserProfile profile1 = userService.selectEduUserProfileByUserId(1L);
        assertNotNull(profile1);
        
        // 测试不存在用户
        EduUserProfile profile2 = userService.selectEduUserProfileByUserId(99999L);
        assertNull(profile2);
    }

    /**
     * 测试用户信息更新
     */
    @Test
    public void testUpdateStudyTime() {
        Long userId = 1L;
        Integer duration = 30;
        
        // 执行更新
        userService.updateStudyTime(userId, duration);
        
        // 验证更新结果
        EduUserProfile profile = userService.selectEduUserProfileByUserId(userId);
        assertNotNull(profile);
        assertTrue(profile.getTotalStudyTime() >= duration);
    }

    /**
     * 测试更新连续学习天数
     */
    @Test
    public void testUpdateStreak() {
        Long userId = 1L;
        Integer initialStreak = userService.selectEduUserProfileByUserId(userId).getCurrentStreak();
        
        // 更新连续天数
        userService.updateStreak(userId);
        
        // 验证更新结果
        EduUserProfile profile = userService.selectEduUserProfileByUserId(userId);
        assertNotNull(profile);
        // 连续天数应该增加或保持
        assertTrue(profile.getCurrentStreak() >= initialStreak);
    }

    /**
     * 测试用户奖励
     */
    @Test
    public void testRewardUser() {
        Long userId = 1L;
        Integer points = 100;
        Integer experience = 50;
        
        // 获取初始值
        EduUserProfile beforeProfile = userService.selectEduUserProfileByUserId(userId);
        int initialPoints = beforeProfile.getTotalPoints();
        int initialExperience = beforeProfile.getExperiencePoints();
        
        // 奖励用户
        userService.rewardUser(userId, points, experience);
        
        // 验证奖励结果
        EduUserProfile afterProfile = userService.selectEduUserProfileByUserId(userId);
        assertEquals(initialPoints + points, afterProfile.getTotalPoints());
        assertEquals(initialExperience + experience, afterProfile.getExperiencePoints());
    }

    /**
     * 测试获取用户完整信息
     */
    @Test
    public void testGetUserFullInfo() {
        Long userId = 1L;
        EduUserProfile profile = userService.getUserFullInfo(userId);
        
        assertNotNull(profile);
        assertEquals(userId, profile.getUserId());
        // 验证默认值的设置
        assertNotNull(profile.getNativeLanguage());
        assertNotNull(profile.getLearningLanguages());
        assertTrue(profile.getLevel() >= 1);
    }
}
