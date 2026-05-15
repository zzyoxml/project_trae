package com.ruoyi.edu.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.edu.domain.EduAchievement;
import com.ruoyi.edu.domain.EduUserAchievement;
import com.ruoyi.edu.domain.EduUserProfile;
import com.ruoyi.edu.domain.EduLearningProgress;
import com.ruoyi.edu.mapper.EduAchievementMapper;
import com.ruoyi.edu.mapper.EduUserAchievementMapper;
import com.ruoyi.edu.mapper.EduUserProfileMapper;
import com.ruoyi.edu.mapper.EduLearningProgressMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.edu.service.IEduAchievementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 成就系统Service实现
 *
 * @author LingLearn
 */
@Service
public class EduAchievementServiceImpl implements IEduAchievementService {

    private static final Logger log = LoggerFactory.getLogger(EduAchievementServiceImpl.class);

    @Autowired
    private EduAchievementMapper achievementMapper;

    @Autowired
    private EduUserAchievementMapper userAchievementMapper;

    @Autowired
    private EduUserProfileMapper userProfileMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private EduLearningProgressMapper learningProgressMapper;

    /**
     * 查询成就
     *
     * @param achievementId 成就ID
     * @return 成就
     */
    @Override
    public EduAchievement selectEduAchievementById(Long achievementId) {
        return achievementMapper.selectEduAchievementById(achievementId);
    }

    /**
     * 查询成就列表
     *
     * @param eduAchievement 成就
     * @return 成就列表
     */
    @Override
    public List<EduAchievement> selectEduAchievementList(EduAchievement eduAchievement) {
        return achievementMapper.selectEduAchievementList(eduAchievement);
    }

    /**
     * 根据类型查询成就
     *
     * @param type 成就类型
     * @return 成就列表
     */
    @Override
    public List<EduAchievement> selectEduAchievementByType(String type) {
        return achievementMapper.selectEduAchievementByType(type);
    }

    /**
     * 新增成就
     *
     * @param eduAchievement 成就
     * @return 结果
     */
    @Override
    public int insertEduAchievement(EduAchievement eduAchievement) {
        return achievementMapper.insertEduAchievement(eduAchievement);
    }

    /**
     * 修改成就
     *
     * @param eduAchievement 成就
     * @return 结果
     */
    @Override
    public int updateEduAchievement(EduAchievement eduAchievement) {
        return achievementMapper.updateEduAchievement(eduAchievement);
    }

    /**
     * 删除成就
     *
     * @param achievementId 成就ID
     * @return 结果
     */
    @Override
    public int deleteEduAchievementById(Long achievementId) {
        return achievementMapper.deleteEduAchievementById(achievementId);
    }

    /**
     * 获取用户成就列表
     *
     * @param userId 用户ID
     * @return 用户成就列表
     */
    @Override
    public List<EduAchievement> getUserAchievements(Long userId) {
        List<EduAchievement> achievements = achievementMapper.selectEduAchievementList(new EduAchievement());

        // 标记用户已完成的成就
        for (EduAchievement achievement : achievements) {
            EduUserAchievement userAchievement = userAchievementMapper.selectUserAchievement(userId, achievement.getAchievementId());
            if (userAchievement != null) {
                achievement.setIsCompleted(userAchievement.getIsCompleted());
                achievement.setProgress(userAchievement.getProgress());
                achievement.setCompletedTime(userAchievement.getCompletedTime());
            } else {
                achievement.setIsCompleted(false);
                achievement.setProgress(0);
            }
        }

        return achievements;
    }

    /**
     * 获取用户待完成成就
     *
     * @param userId 用户ID
     * @return 待完成成就列表
     */
    @Override
    public List<EduAchievement> getPendingAchievements(Long userId) {
        return achievementMapper.selectPendingAchievements(userId);
    }

    /**
     * 检查并更新用户成就进度
     *
     * @param userId 用户ID
     * @return 新完成的成就列表
     */
    @Override
    @Transactional
    public List<EduAchievement> checkAndUpdateAchievements(Long userId) {
        List<EduAchievement> newAchievements = new ArrayList<>();

        // 获取用户信息
        EduUserProfile profile = userProfileMapper.selectEduUserProfileByUserId(userId);
        if (profile == null) {
            return newAchievements;
        }

        // 获取所有成就
        List<EduAchievement> achievements = achievementMapper.selectEduAchievementList(new EduAchievement());

        for (EduAchievement achievement : achievements) {
            // 检查是否已完成
            EduUserAchievement userAchievement = userAchievementMapper.selectUserAchievement(userId, achievement.getAchievementId());
            if (userAchievement != null && userAchievement.getIsCompleted()) {
                continue;
            }

            // 计算当前进度
            int progress = calculateProgress(userId, profile, achievement);
            int targetValue = achievement.getRequirementValue();

            if (userAchievement == null) {
                // 创建用户成就记录
                userAchievement = new EduUserAchievement();
                userAchievement.setUserId(userId);
                userAchievement.setAchievementId(achievement.getAchievementId());
                userAchievement.setProgress(0);
                userAchievement.setTargetValue(targetValue);
                userAchievement.setIsCompleted(false);
                userAchievementMapper.insertEduUserAchievement(userAchievement);
            }

            // 更新进度
            if (progress > userAchievement.getProgress()) {
                userAchievement.setProgress(progress);
                userAchievementMapper.updateEduUserAchievement(userAchievement);
            }

            // 检查是否完成
            if (progress >= targetValue && !userAchievement.getIsCompleted()) {
                userAchievement.setIsCompleted(true);
                userAchievement.setCompletedTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                userAchievementMapper.updateEduUserAchievement(userAchievement);

                // 奖励用户
                userProfileMapper.updatePointsAndExperience(userId, achievement.getCoinReward(), achievement.getXpReward());

                newAchievements.add(achievement);
                log.info("用户完成成就: userId={}, achievement={}", userId, achievement.getAchievementName());
            }
        }

        return newAchievements;
    }

    /**
     * 计算成就进度
     *
     * @param userId     用户ID
     * @param profile    用户信息
     * @param achievement 成就
     * @return 当前进度
     */
    private int calculateProgress(Long userId, EduUserProfile profile, EduAchievement achievement) {
        String requirementType = achievement.getRequirementType();

        switch (requirementType) {
            case "streak_days":
                return profile.getCurrentStreak() != null ? profile.getCurrentStreak() : 0;

            case "total_study_time":
                return profile.getTotalStudyTime() != null ? profile.getTotalStudyTime().intValue() : 0;

            case "total_points":
                return profile.getTotalPoints() != null ? profile.getTotalPoints() : 0;

            case "level":
                return profile.getLevel() != null ? profile.getLevel() : 1;

            default:
                return 0;
        }
    }

    /**
     * 获取排行榜
     *
     * @param type     排行榜类型
     * @param language 语言
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 排行榜数据
     */
    @Override
    public Map<String, Object> getLeaderboard(String type, String language, Integer pageNum, Integer pageSize) {
        Map<String, Object> result = new HashMap<>();

        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 20;
        }

        // 获取用户列表
        List<EduUserProfile> users = userProfileMapper.selectEduUserProfileList(new EduUserProfile());
        
        // 过滤掉在sys_user中不存在的用户
        List<EduUserProfile> validUsers = new ArrayList<>();
        for (EduUserProfile profile : users) {
            SysUser sysUser = sysUserMapper.selectUserById(profile.getUserId());
            if (sysUser != null) {
                validUsers.add(profile);
            }
        }
        users = validUsers;

        // 计算今日或本周积分（每个课时5积分）
        Map<Long, Integer> dailyPointsMap = new HashMap<>();
        Map<Long, Integer> weeklyPointsMap = new HashMap<>();
        
        if ("daily".equals(type) || "weekly".equals(type)) {
            List<EduLearningProgress> allProgress = learningProgressMapper.selectEduLearningProgressList(new EduLearningProgress());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);
            
            Calendar weekStart = Calendar.getInstance();
            weekStart.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            weekStart.set(Calendar.HOUR_OF_DAY, 0);
            weekStart.set(Calendar.MINUTE, 0);
            weekStart.set(Calendar.SECOND, 0);
            weekStart.set(Calendar.MILLISECOND, 0);
            
            for (EduLearningProgress progress : allProgress) {
                if ("completed".equals(progress.getStatus()) && progress.getCompletedTime() != null) {
                    try {
                        Date completedDate = sdf.parse(progress.getCompletedTime());
                        Long userId = progress.getUserId();
                        
                        if (completedDate.after(today.getTime())) {
                            dailyPointsMap.put(userId, dailyPointsMap.getOrDefault(userId, 0) + 5);
                        }
                        
                        if (completedDate.after(weekStart.getTime())) {
                            weeklyPointsMap.put(userId, weeklyPointsMap.getOrDefault(userId, 0) + 5);
                        }
                    } catch (Exception e) {
                        log.error("解析完成时间失败: {}", progress.getCompletedTime(), e);
                    }
                }
            }
        }

        // 按类型排序
        if ("total".equals(type) || type == null) {
            users.sort((a, b) -> {
                int aPoints = a.getTotalPoints() != null ? a.getTotalPoints() : 0;
                int bPoints = b.getTotalPoints() != null ? b.getTotalPoints() : 0;
                return bPoints - aPoints;
            });
        } else if ("daily".equals(type)) {
            users.sort((a, b) -> {
                int aPoints = dailyPointsMap.getOrDefault(a.getUserId(), 0);
                int bPoints = dailyPointsMap.getOrDefault(b.getUserId(), 0);
                return bPoints - aPoints;
            });
        } else if ("weekly".equals(type)) {
            users.sort((a, b) -> {
                int aPoints = weeklyPointsMap.getOrDefault(a.getUserId(), 0);
                int bPoints = weeklyPointsMap.getOrDefault(b.getUserId(), 0);
                return bPoints - aPoints;
            });
        } else if ("streak".equals(type)) {
            users.sort((a, b) -> {
                int aStreak = a.getCurrentStreak() != null ? a.getCurrentStreak() : 0;
                int bStreak = b.getCurrentStreak() != null ? b.getCurrentStreak() : 0;
                return bStreak - aStreak;
            });
        } else if ("level".equals(type)) {
            users.sort((a, b) -> {
                int aLevel = a.getLevel() != null ? a.getLevel() : 0;
                int bLevel = b.getLevel() != null ? b.getLevel() : 0;
                return bLevel - aLevel;
            });
        }

        // 转换为前端期望的格式
        List<Map<String, Object>> rows = new ArrayList<>();
        for (EduUserProfile profile : users) {
            Map<String, Object> userData = new HashMap<>();
            
            // 获取系统用户信息
            SysUser sysUser = sysUserMapper.selectUserById(profile.getUserId());
            
            userData.put("userId", profile.getUserId());
            userData.put("username", sysUser != null ? sysUser.getUserName() : "未知用户");
            userData.put("avatar", profile.getAvatarUrl());
            
            // 根据类型设置分数
            if ("daily".equals(type)) {
                userData.put("score", dailyPointsMap.getOrDefault(profile.getUserId(), 0));
            } else if ("weekly".equals(type)) {
                userData.put("score", weeklyPointsMap.getOrDefault(profile.getUserId(), 0));
            } else {
                userData.put("score", profile.getTotalPoints() != null ? profile.getTotalPoints() : 0);
            }
            
            userData.put("level", profile.getLevel() != null ? profile.getLevel() : 1);
            userData.put("levelName", "Lv." + (profile.getLevel() != null ? profile.getLevel() : 1));
            userData.put("achievements", 0);
            userData.put("totalMinutes", profile.getTotalStudyTime() != null ? profile.getTotalStudyTime() : 0);
            userData.put("streakDays", profile.getCurrentStreak() != null ? profile.getCurrentStreak() : 0);
            
            rows.add(userData);
        }

        // 分页
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, rows.size());
        List<Map<String, Object>> pageUsers = rows.subList(start, end);

        result.put("rows", pageUsers);
        result.put("total", rows.size());
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);

        return result;
    }

    /**
     * 获取用户排名
     *
     * @param userId   用户ID
     * @param type     排行榜类型
     * @param language 语言
     * @return 排名
     */
    @Override
    public Integer getUserRank(Long userId, String type, String language) {
        Map<String, Object> leaderboard = getLeaderboard(type, language, 1, 1000);
        List<?> rows = (List<?>) leaderboard.get("rows");

        for (int i = 0; i < rows.size(); i++) {
            Object item = rows.get(i);
            if (item instanceof Map) {
                Long itemUserId = (Long) ((Map<?, ?>) item).get("userId");
                if (itemUserId != null && itemUserId.equals(userId)) {
                    return i + 1;
                }
            }
        }

        return null;
    }

    /**
     * 初始化用户成就进度
     *
     * @param userId 用户ID
     */
    @Override
    @Transactional
    public void initializeUserAchievements(Long userId) {
        List<EduAchievement> achievements = achievementMapper.selectEduAchievementList(new EduAchievement());

        for (EduAchievement achievement : achievements) {
            EduUserAchievement existUserAchievement = userAchievementMapper.selectUserAchievement(userId, achievement.getAchievementId());
            if (existUserAchievement == null) {
                EduUserAchievement userAchievement = new EduUserAchievement();
                userAchievement.setUserId(userId);
                userAchievement.setAchievementId(achievement.getAchievementId());
                userAchievement.setProgress(0);
                userAchievement.setTargetValue(achievement.getRequirementValue());
                userAchievement.setIsCompleted(false);
                userAchievementMapper.insertEduUserAchievement(userAchievement);
            }
        }

        log.info("初始化用户成就: userId={}", userId);
    }
}
