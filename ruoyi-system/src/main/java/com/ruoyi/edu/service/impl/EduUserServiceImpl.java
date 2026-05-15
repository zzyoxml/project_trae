package com.ruoyi.edu.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.edu.domain.EduUserProfile;
import com.ruoyi.edu.mapper.EduUserProfileMapper;
import com.ruoyi.edu.service.IEduUserService;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        user.setPassword(com.ruoyi.common.utils.SecurityUtils.encryptPassword(password));
        // 设置默认角色
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
        // 这里应该使用框架的登录机制
        // 由于若依框架有自己的登录流程，我们可以直接返回成功标志
        // 实际的token生成和验证由Spring Security处理
        SysUser user = sysUserMapper.selectUserByUserName(username);
        if (StringUtils.isNull(user)) {
            throw new ServiceException("用户不存在");
        }

        // 验证密码
        if (!com.ruoyi.common.utils.SecurityUtils.matchesPassword(password, user.getPassword())) {
            throw new ServiceException("密码错误");
        }

        // 检查用户状态
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
            // 如果没有扩展信息，创建默认的
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
            // 如果用户没有扩展信息，创建默认的
            profile = new EduUserProfile();
            profile.setUserId(userId);
            profile.setNativeLanguage("zh");
            profile.setLearningLanguages("en");
            profile.setProficiencyLevel("beginner");
            profile.setTotalStudyTime((long) duration); // 直接设置学习时长
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
            // 如果用户没有扩展信息，创建默认的
            profile = new EduUserProfile();
            profile.setUserId(userId);
            profile.setNativeLanguage("zh");
            profile.setLearningLanguages("en");
            profile.setProficiencyLevel("beginner");
            profile.setTotalStudyTime(0L);
            profile.setCurrentStreak(1); // 设置为1天
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

        // 检查是否需要升级
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
        // 经验值升级规则：每1000点经验升一级
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
            // 如果用户没有扩展信息，创建默认的
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
            profile.setTotalPoints(points); // 直接设置为新积分
            profile.setLevel(1);
            profile.setExperiencePoints(0);
            
            eduUserProfileMapper.insertEduUserProfile(profile);
            log.info("创建用户扩展信息并添加积分: userId={}, points={}", userId, points);
        } else {
            eduUserProfileMapper.addUserPoints(userId, points);
            log.debug("添加用户积分: userId={}, points={}", userId, points);
        }
    }
}
