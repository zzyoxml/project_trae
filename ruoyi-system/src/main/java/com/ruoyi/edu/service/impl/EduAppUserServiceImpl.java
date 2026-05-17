package com.ruoyi.edu.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.edu.domain.EduAppUser;
import com.ruoyi.edu.domain.EduUserProfile;
import com.ruoyi.edu.mapper.EduAppUserMapper;
import com.ruoyi.edu.mapper.EduUserProfileMapper;
import com.ruoyi.edu.service.IEduAppUserService;
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
 * 教育平台应用用户Service实现
 *
 * @author LingLearn
 */
@Service
public class EduAppUserServiceImpl implements IEduAppUserService {

    private static final Logger log = LoggerFactory.getLogger(EduAppUserServiceImpl.class);

    @Autowired
    private EduAppUserMapper eduAppUserMapper;

    @Autowired
    private EduUserProfileMapper eduUserProfileMapper;

    /**
     * 查询用户
     *
     * @param userId 用户ID
     * @return 用户
     */
    @Override
    public EduAppUser selectEduAppUserById(Long userId) {
        return eduAppUserMapper.selectEduAppUserById(userId);
    }

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户
     */
    @Override
    public EduAppUser selectEduAppUserByUserName(String username) {
        return eduAppUserMapper.selectEduAppUserByUserName(username);
    }

    /**
     * 查询用户列表
     *
     * @param eduAppUser 用户
     * @return 用户集合
     */
    @Override
    public List<EduAppUser> selectEduAppUserList(EduAppUser eduAppUser) {
        return eduAppUserMapper.selectEduAppUserList(eduAppUser);
    }

    /**
     * 新增用户
     *
     * @param eduAppUser 用户
     * @return 结果
     */
    @Override
    public int insertEduAppUser(EduAppUser eduAppUser) {
        return eduAppUserMapper.insertEduAppUser(eduAppUser);
    }

    /**
     * 修改用户
     *
     * @param eduAppUser 用户
     * @return 结果
     */
    @Override
    public int updateEduAppUser(EduAppUser eduAppUser) {
        return eduAppUserMapper.updateEduAppUser(eduAppUser);
    }

    /**
     * 删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int deleteEduAppUserById(Long userId) {
        return eduAppUserMapper.deleteEduAppUserById(userId);
    }

    /**
     * 批量删除用户
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    public int deleteEduAppUserByIds(Long[] userIds) {
        return eduAppUserMapper.deleteEduAppUserByIds(userIds);
    }

    /**
     * 校验用户名是否唯一
     *
     * @param eduAppUser 用户信息
     * @return 结果
     */
    @Override
    public boolean checkUserNameUnique(EduAppUser eduAppUser) {
        Long userId = StringUtils.isNull(eduAppUser.getUserId()) ? -1L : eduAppUser.getUserId();
        EduAppUser info = eduAppUserMapper.selectEduAppUserByUserName(eduAppUser.getUsername());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return false;
        }
        return true;
    }

    /**
     * 校验邮箱是否唯一
     *
     * @param eduAppUser 用户信息
     * @return 结果
     */
    @Override
    public boolean checkEmailUnique(EduAppUser eduAppUser) {
        Long userId = StringUtils.isNull(eduAppUser.getUserId()) ? -1L : eduAppUser.getUserId();
        EduAppUser info = eduAppUserMapper.checkEmailUnique(eduAppUser.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return false;
        }
        return true;
    }

    /**
     * 用户注册
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
        EduAppUser existUser = eduAppUserMapper.selectEduAppUserByUserName(username);
        if (StringUtils.isNotNull(existUser)) {
            throw new ServiceException("用户名已存在");
        }

        // 验证邮箱是否已存在
        EduAppUser existEmailUser = eduAppUserMapper.checkEmailUnique(email);
        if (StringUtils.isNotNull(existEmailUser)) {
            throw new ServiceException("邮箱已被注册");
        }

        // 创建应用用户
        EduAppUser user = new EduAppUser();
        user.setUsername(username);
        user.setNickName(username);
        user.setEmail(email);
        user.setPhonenumber("");
        user.setPassword(SecurityUtils.encryptPassword(password));
        user.setStatus("0");
        user.setDelFlag("0");

        // 注册用户
        int result = eduAppUserMapper.insertEduAppUser(user);
        if (result <= 0) {
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

        eduUserProfileMapper.insertEduUserProfile(profile);

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
        EduAppUser user = eduAppUserMapper.selectEduAppUserByUserName(username);
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
     * 获取用户列表（包含扩展信息）
     *
     * @param username 用户名
     * @param email 邮箱
     * @param learningLanguage 学习语言
     * @return 用户列表
     */
    @Override
    public List<Map<String, Object>> selectUserListWithDetails(String username, String email, String learningLanguage) {
        List<Map<String, Object>> result = new ArrayList<>();

        // 获取所有应用用户
        EduAppUser queryUser = new EduAppUser();
        if (StringUtils.isNotEmpty(username)) {
            queryUser.setUsername(username);
        }
        if (StringUtils.isNotEmpty(email)) {
            queryUser.setEmail(email);
        }

        List<EduAppUser> appUsers = eduAppUserMapper.selectEduAppUserList(queryUser);

        for (EduAppUser appUser : appUsers) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("userId", appUser.getUserId());
            userMap.put("username", appUser.getUsername());
            userMap.put("nickName", appUser.getNickName());
            userMap.put("email", appUser.getEmail());
            userMap.put("phonenumber", appUser.getPhonenumber());
            userMap.put("status", appUser.getStatus());
            userMap.put("createTime", appUser.getCreateTime());
            userMap.put("avatar", appUser.getAvatar());

            // 获取教育扩展信息
            EduUserProfile profile = eduUserProfileMapper.selectEduUserProfileByUserId(appUser.getUserId());
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

            // 如果指定了学习语言筛选
            if (StringUtils.isNotEmpty(learningLanguage) && profile != null) {
                String userLangs = profile.getLearningLanguages();
                if (userLangs == null || !userLangs.contains(learningLanguage)) {
                    continue;
                }
            }

            result.add(userMap);
        }

        return result;
    }

    /**
     * 更新用户状态
     *
     * @param userId 用户ID
     * @param status 状态
     * @return 结果
     */
    @Override
    public int updateUserStatus(Long userId, String status) {
        EduAppUser user = eduAppUserMapper.selectEduAppUserById(userId);
        if (user != null) {
            user.setStatus(status);
            return eduAppUserMapper.updateEduAppUser(user);
        }
        return 0;
    }

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
     * 获取用户学习统计数据
     *
     * @param userId 用户ID
     * @return 学习统计数据Map
     */
    @Override
    public Map<String, Object> getLearningStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        
        EduUserProfile profile = eduUserProfileMapper.selectEduUserProfileByUserId(userId);
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
        
        log.debug("获取学习统计: userId={}, stats={}", userId, stats);
        return stats;
    }
}
