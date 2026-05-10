package com.ruoyi.edu.service;

import com.ruoyi.edu.domain.EduUserProfile;

import java.util.List;

/**
 * 用户扩展信息Service接口
 *
 * @author LingLearn
 */
public interface IEduUserService {

    /**
     * 查询用户扩展信息
     *
     * @param userId 用户ID
     * @return 用户扩展信息
     */
    public EduUserProfile selectEduUserProfileByUserId(Long userId);

    /**
     * 查询用户扩展信息列表
     *
     * @param eduUserProfile 用户扩展信息
     * @return 用户扩展信息集合
     */
    public List<EduUserProfile> selectEduUserProfileList(EduUserProfile eduUserProfile);

    /**
     * 新增用户扩展信息
     *
     * @param eduUserProfile 用户扩展信息
     * @return 结果
     */
    public int insertEduUserProfile(EduUserProfile eduUserProfile);

    /**
     * 修改用户扩展信息
     *
     * @param eduUserProfile 用户扩展信息
     * @return 结果
     */
    public int updateEduUserProfile(EduUserProfile eduUserProfile);

    /**
     * 删除用户扩展信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteEduUserProfileByUserId(Long userId);

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
    public Long registerUser(String username, String password, String email,
                           String nativeLanguage, String learningLanguages);

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    public String login(String username, String password);

    /**
     * 获取用户信息（包括扩展信息）
     *
     * @param userId 用户ID
     * @return 用户完整信息
     */
    public EduUserProfile getUserFullInfo(Long userId);

    /**
     * 更新学习时间
     *
     * @param userId   用户ID
     * @param duration 学习时长（分钟）
     */
    public void updateStudyTime(Long userId, Integer duration);

    /**
     * 更新连续学习天数
     *
     * @param userId 用户ID
     */
    public void updateStreak(Long userId);

    /**
     * 奖励用户积分和经验
     *
     * @param userId     用户ID
     * @param points     积分
     * @param experience 经验
     */
    public void rewardUser(Long userId, Integer points, Integer experience);
}
