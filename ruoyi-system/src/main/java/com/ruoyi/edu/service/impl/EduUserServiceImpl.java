package com.ruoyi.edu.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.edu.domain.EduUserProfile;
import com.ruoyi.edu.mapper.EduUserProfileMapper;
import com.ruoyi.edu.mapper.EduUserCourseMapper;
import com.ruoyi.edu.mapper.EduUserAchievementMapper;
import com.ruoyi.edu.service.IEduUserService;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户扩展信息Service实现
 *
 * @author LingLearn
 */
@Service
public class EduUserServiceImpl implements IEduUserService {

    private static final Logger log = LoggerFactory.getLogger(EduUserServiceImpl.class);

    @Autowired
    private EduUserProfileMapper eduUserProfileMapper;

    @Autowired
    private EduUserCourseMapper eduUserCourseMapper;

    @Autowired
    private EduUserAchievementMapper eduUserAchievementMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private ISysUserService userService;

    /**
     * 查询用户扩展信息
     *
     * @param userId 用户ID
     * @return 用户扩展信息
     */
    @Override
    public EduUserProfile selectEduUserProfileByUserId(Long userId) {
        return eduUserProfileMapper.selectEduUserProfileByUserId(userId);
    }

    /**
     * 查询用户扩展信息列表
     *
     * @param eduUserProfile 用户扩展信息
     * @return 用户扩展信息集合
     */
    @Override
    public List<EduUserProfile> selectEduUserProfileList(EduUserProfile eduUserProfile) {
        return eduUserProfileMapper.selectEduUserProfileList(eduUserProfile);
    }

    /**
     * 新增用户扩展信息
     *
     * @param eduUserProfile 用户扩展信息
     * @return 结果
     */
    @Override
    public int insertEduUserProfile(EduUserProfile eduUserProfile) {
        return eduUserProfileMapper.insertEduUserProfile(eduUserProfile);
    }

    /**
     * 修改用户扩展信息
     *
     * @param eduUserProfile 用户扩展信息
     * @return 结果
     */
    @Override
    public int updateEduUserProfile(EduUserProfile eduUserProfile) {
        return eduUserProfileMapper.updateEduUserProfile(eduUserProfile);
    }

    /**
     * 删除用户扩展信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int deleteEduUserProfileByUserId(Long userId) {
        return eduUserProfileMapper.deleteEduUserProfileByUserId(userId);
    }

    /**
     * 删除用户（包括系统用户和教育扩展信息）
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUser(Long userId) {
        deleteEduUserProfileByUserId(userId);

        SysUser sysUser = sysUserMapper.selectUserById(userId);
        if (sysUser != null) {
            sysUserMapper.deleteUserById(userId);
        }

        log.info("删除用户: userId={}", userId);
        return 1;
    }

    /**
     * 注册新用户
     *
     * @param username         用户名
     * @param password         密码
     * @param email            邮箱
     * @param nativeLanguage   母语
     * @param learningLanguages 学习语言
     * @return 用户ID
     */
    @Override
    @Transactional
    public Long registerUser(String username, String password, String email,
                            String nativeLanguage, String learningLanguages) {
        // 验证用户名是否已存在
        SysUser existUser = sysUserMapper.selectUserByUserName(username);
        if (StringUtils.isNotNull(existUser)) {
            throw new ServiceException("用户名已存在");
        }

        // 验证邮箱是否已存在
        SysUser existEmailUser = sysUserMapper.checkEmailUnique(email);
        if (StringUtils.isNotNull(existEmailUser)) {
            throw new ServiceException("邮箱已被注册");
        }

        // 创建系统用户
        SysUser user = new SysUser();
        user.setUserName(username);
        user.setNickName(username);
        user.setEmail(email);
        user.setPhonenumber("");
        user.setPassword(SecurityUtils.encryptPassword(password));
        user.setRoles(null);

        // 注册用户（使用框架的用户服务）
        boolean result = userService.registerUser(user);
        if (!result) {
            throw new ServiceException("用户注册失败");
        }

        Long userId = user.getUserId();

        // 创建用户扩展信息
        EduUserProfile profile = new EduUserProfile();
        profile.setUserId(userId);
        profile.setNativeLanguage(nativeLanguage != null ? nativeLanguage : "zh");
        profile.setLearningLanguages(learningLanguages != null ? learningLanguages : "en");
        profile.setProficiencyLevel("beginner");
        profile.setTotalStudyTime(0L);
        profile.setCurrentStreak(0);
        profile.setLongestStreak(0);
        profile.setDailyGoal(30);
        profile.setPreferredLearningTime("morning");
        profile.setVoiceEnabled(true);
        profile.setTotalPoints(0);
        profile.setLevel(1);
        profile.setExperiencePoints(0);

        insertEduUserProfile(profile);

        log.info("用户注册成功: username={}, userId={}", username, userId);
        return userId;
    }

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    @Override
    public String login(String username, String password) {
        SysUser user = sysUserMapper.selectUserByUserName(username);
        if (StringUtils.isNull(user)) {
            throw new ServiceException("用户不存在");
        }

        if (!SecurityUtils.matchesPassword(password, user.getPassword())) {
            throw new ServiceException("密码错误");
        }

        if (user.getStatus().equals("1")) {
            throw new ServiceException("用户已被停用");
        }

        log.info("用户登录成功: username={}", username);
        return "登录成功";
    }

    /**
     * 获取用户信息（包括扩展信息）
     *
     * @param userId 用户ID
     * @return 用户完整信息
     */
    @Override
    public EduUserProfile getUserFullInfo(Long userId) {
        EduUserProfile profile = selectEduUserProfileByUserId(userId);
        if (profile == null) {
            profile = new EduUserProfile();
            profile.setUserId(userId);
            profile.setNativeLanguage("zh");
            profile.setLearningLanguages("en");
            profile.setProficiencyLevel("beginner");
            profile.setTotalStudyTime(0L);
            profile.setCurrentStreak(0);
            profile.setLongestStreak(0);
            profile.setDailyGoal(30);
            profile.setPreferredLearningTime("morning");
            profile.setVoiceEnabled(true);
            profile.setTotalPoints(0);
            profile.setLevel(1);
            profile.setExperiencePoints(0);
        }
        return profile;
    }

    /**
     * 获取用户列表（包含详细信息）
     *
     * @param username 用户名
     * @param email 邮箱
     * @param learningLanguage 学习语言
     * @return 用户列表
     */
    @Override
    public List<Map<String, Object>> selectUserListWithDetails(String username, String email, String learningLanguage) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 获取所有系统用户
        SysUser queryUser = new SysUser();
        if (StringUtils.isNotEmpty(username)) {
            queryUser.setUserName(username);
        }
        if (StringUtils.isNotEmpty(email)) {
            queryUser.setEmail(email);
        }
        
        // 这里简化处理，直接从系统用户表查询
        List<SysUser> sysUsers = sysUserMapper.selectUserList(queryUser);
        
        for (SysUser sysUser : sysUsers) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("userId", sysUser.getUserId());
            userMap.put("username", sysUser.getUserName());
            userMap.put("nickName", sysUser.getNickName());
            userMap.put("email", sysUser.getEmail());
            userMap.put("phonenumber", sysUser.getPhonenumber());
            userMap.put("status", sysUser.getStatus());
            userMap.put("createTime", sysUser.getCreateTime());
            
            // 获取教育扩展信息
            EduUserProfile profile = selectEduUserProfileByUserId(sysUser.getUserId());
            if (profile != null) {
                userMap.put("nativeLanguage", profile.getNativeLanguage());
                userMap.put("learningLanguages", profile.getLearningLanguages());
                userMap.put("totalStudyTime", profile.getTotalStudyTime());
                userMap.put("currentStreak", profile.getCurrentStreak());
                userMap.put("longestStreak", profile.getLongestStreak());
                userMap.put("totalPoints", profile.getTotalPoints());
                userMap.put("level", profile.getLevel());
                userMap.put("experiencePoints", profile.getExperiencePoints());
                userMap.put("bio", profile.getBio());
                userMap.put("avatarUrl", profile.getAvatarUrl());
            } else {
                userMap.put("nativeLanguage", "zh");
                userMap.put("learningLanguages", "en");
                userMap.put("totalStudyTime", 0);
                userMap.put("currentStreak", 0);
                userMap.put("longestStreak", 0);
                userMap.put("totalPoints", 0);
                userMap.put("level", 1);
                userMap.put("experiencePoints", 0);
            }
            
            // 添加系统用户头像
            userMap.put("avatar", sysUser.getAvatar());
            
            // 如果指定了学习语言筛选
            if (StringUtils.isNotEmpty(learningLanguage) && profile != null) {
                String userLangs = profile.getLearningLanguages();
                if (userLangs == null || !userLangs.contains(learningLanguage)) {
                    continue; // 跳过不匹配的用户
                }
            }
            
            result.add(userMap);
        }
        
        return result;
    }

    /**
     * 更新学习时间
     *
     * @param userId   用户ID
     * @param duration 学习时长（分钟）
     */
    @Override
    @Transactional
    public void updateStudyTime(Long userId, Integer duration) {
        EduUserProfile profile = eduUserProfileMapper.selectEduUserProfileByUserId(userId);
        
        if (profile == null) {
            profile = new EduUserProfile();
            profile.setUserId(userId);
            profile.setNativeLanguage("zh");
            profile.setLearningLanguages("en");
            profile.setProficiencyLevel("beginner");
            profile.setTotalStudyTime((long) duration);
            profile.setCurrentStreak(0);
            profile.setLongestStreak(0);
            profile.setDailyGoal(30);
            profile.setPreferredLearningTime("morning");
            profile.setVoiceEnabled(true);
            profile.setTotalPoints(0);
            profile.setLevel(1);
            profile.setExperiencePoints(0);
            
            eduUserProfileMapper.insertEduUserProfile(profile);
            log.info("创建用户扩展信息并更新学习时间: userId={}, duration={}", userId, duration);
        } else {
            eduUserProfileMapper.updateStudyTime(userId, duration);
            log.debug("更新学习时间: userId={}, duration={}", userId, duration);
        }
    }

    /**
     * 更新连续学习天数
     *
     * @param userId 用户ID
     */
    @Override
    @Transactional
    public void updateStreak(Long userId) {
        EduUserProfile profile = eduUserProfileMapper.selectEduUserProfileByUserId(userId);
        
        if (profile == null) {
            profile = new EduUserProfile();
            profile.setUserId(userId);
            profile.setNativeLanguage("zh");
            profile.setLearningLanguages("en");
            profile.setProficiencyLevel("beginner");
            profile.setTotalStudyTime(0L);
            profile.setCurrentStreak(1);
            profile.setLongestStreak(1);
            profile.setDailyGoal(30);
            profile.setPreferredLearningTime("morning");
            profile.setVoiceEnabled(true);
            profile.setTotalPoints(0);
            profile.setLevel(1);
            profile.setExperiencePoints(0);
            
            eduUserProfileMapper.insertEduUserProfile(profile);
            log.info("创建用户扩展信息并更新连续学习天数: userId={}", userId);
        } else {
            eduUserProfileMapper.updateStreak(userId);
            log.debug("更新连续学习天数: userId={}", userId);
        }
    }

    /**
     * 奖励用户积分和经验
     *
     * @param userId     用户ID
     * @param points     积分
     * @param experience 经验
     */
    @Override
    public void rewardUser(Long userId, Integer points, Integer experience) {
        eduUserProfileMapper.updatePointsAndExperience(userId, points, experience);

        EduUserProfile profile = selectEduUserProfileByUserId(userId);
        if (profile != null) {
            checkLevelUp(profile);
        }

        log.debug("奖励用户: userId={}, points={}, experience={}", userId, points, experience);
    }

    /**
     * 检查是否需要升级
     *
     * @param profile 用户扩展信息
     */
    private void checkLevelUp(EduUserProfile profile) {
        int newLevel = (profile.getExperiencePoints() / 1000) + 1;
        if (newLevel > profile.getLevel()) {
            profile.setLevel(newLevel);
            updateEduUserProfile(profile);
            log.info("用户升级: userId={}, newLevel={}", profile.getUserId(), newLevel);
        }
    }

    /**
     * 添加用户积分
     *
     * @param userId 用户ID
     * @param points 积分数量
     */
    @Override
    @Transactional
    public void addUserPoints(Long userId, Integer points) {
        EduUserProfile profile = eduUserProfileMapper.selectEduUserProfileByUserId(userId);
        
        if (profile == null) {
            profile = new EduUserProfile();
            profile.setUserId(userId);
            profile.setNativeLanguage("zh");
            profile.setLearningLanguages("en");
            profile.setProficiencyLevel("beginner");
            profile.setTotalStudyTime(0L);
            profile.setCurrentStreak(0);
            profile.setLongestStreak(0);
            profile.setDailyGoal(30);
            profile.setPreferredLearningTime("morning");
            profile.setVoiceEnabled(true);
            profile.setTotalPoints(points);
            profile.setLevel(1);
            profile.setExperiencePoints(0);
            
            eduUserProfileMapper.insertEduUserProfile(profile);
            log.info("创建用户扩展信息并添加积分: userId={}, points={}", userId, points);
        } else {
            eduUserProfileMapper.addUserPoints(userId, points);
            log.debug("添加用户积分: userId={}, points={}", userId, points);
        }
    }

    /**
     * 获取用户学习统计数据
     *
     * @param userId 用户ID
     * @return 学习统计数据Map
     */
    @Override
    public Map<String, Object> getLearningStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        
        EduUserProfile profile = getUserFullInfo(userId);
        if (profile != null) {
            stats.put("totalLearningMinutes", profile.getTotalStudyTime() != null ? profile.getTotalStudyTime() : 0);
            stats.put("streakDays", profile.getCurrentStreak() != null ? profile.getCurrentStreak() : 0);
            stats.put("totalPoints", profile.getTotalPoints() != null ? profile.getTotalPoints() : 0);
            stats.put("level", profile.getLevel() != null ? profile.getLevel() : 1);
            stats.put("experiencePoints", profile.getExperiencePoints() != null ? profile.getExperiencePoints() : 0);
        } else {
            stats.put("totalLearningMinutes", 0);
            stats.put("streakDays", 0);
            stats.put("totalPoints", 0);
            stats.put("level", 1);
            stats.put("experiencePoints", 0);
        }
        
        Integer completedCourses = eduUserCourseMapper.countCompletedCourses(userId);
        stats.put("completedCourses", completedCourses != null ? completedCourses : 0);
        
        Integer learningCourses = eduUserCourseMapper.countLearningCourses(userId);
        stats.put("learningCourses", learningCourses != null ? learningCourses : 0);
        
        Integer achievements = eduUserAchievementMapper.countAchievements(userId);
        stats.put("achievements", achievements != null ? achievements : 0);
        
        log.debug("获取学习统计: userId={}, stats={}", userId, stats);
        return stats;
    }
}
