package com.ruoyi.edu.service;

import com.ruoyi.edu.domain.EduAppUser;

import java.util.List;
import java.util.Map;

/**
 * 教育平台应用用户Service接口
 *
 * @author LingLearn
 */
public interface IEduAppUserService {

    /**
     * 查询用户
     *
     * @param userId 用户ID
     * @return 用户
     */
    public EduAppUser selectEduAppUserById(Long userId);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户
     */
    public EduAppUser selectEduAppUserByUserName(String username);

    /**
     * 查询用户列表
     *
     * @param eduAppUser 用户
     * @return 用户集合
     */
    public List<EduAppUser> selectEduAppUserList(EduAppUser eduAppUser);

    /**
     * 新增用户
     *
     * @param eduAppUser 用户
     * @return 结果
     */
    public int insertEduAppUser(EduAppUser eduAppUser);

    /**
     * 修改用户
     *
     * @param eduAppUser 用户
     * @return 结果
     */
    public int updateEduAppUser(EduAppUser eduAppUser);

    /**
     * 删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteEduAppUserById(Long userId);

    /**
     * 批量删除用户
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    public int deleteEduAppUserByIds(Long[] userIds);

    /**
     * 校验用户名是否唯一
     *
     * @param eduAppUser 用户信息
     * @return 结果
     */
    public boolean checkUserNameUnique(EduAppUser eduAppUser);

    /**
     * 校验邮箱是否唯一
     *
     * @param eduAppUser 用户信息
     * @return 结果
     */
    public boolean checkEmailUnique(EduAppUser eduAppUser);

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
     * 获取用户列表（包含扩展信息）
     *
     * @param username 用户名
     * @param email 邮箱
     * @param learningLanguage 学习语言
     * @return 用户列表
     */
    public List<Map<String, Object>> selectUserListWithDetails(String username, String email, String learningLanguage);

    /**
     * 更新用户状态
     *
     * @param userId 用户ID
     * @param status 状态
     * @return 结果
     */
    public int updateUserStatus(Long userId, String status);

    /**
     * 查询用户扩展信息
     *
     * @param userId 用户ID
     * @return 用户扩展信息
     */
    public com.ruoyi.edu.domain.EduUserProfile selectEduUserProfileByUserId(Long userId);

    /**
     * 删除用户扩展信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteEduUserProfileByUserId(Long userId);

    /**
     * 新增用户扩展信息
     *
     * @param eduUserProfile 用户扩展信息
     * @return 结果
     */
    public int insertEduUserProfile(com.ruoyi.edu.domain.EduUserProfile eduUserProfile);

    /**
     * 修改用户扩展信息
     *
     * @param eduUserProfile 用户扩展信息
     * @return 结果
     */
    public int updateEduUserProfile(com.ruoyi.edu.domain.EduUserProfile eduUserProfile);

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

    /**
     * 获取用户学习统计数据
     *
     * @param userId 用户ID
     * @return 学习统计数据Map
     */
    public java.util.Map<String, Object> getLearningStats(Long userId);
}
