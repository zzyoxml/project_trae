package com.ruoyi.edu.service;

import com.ruoyi.edu.domain.EduAchievement;

import java.util.List;
import java.util.Map;

/**
 * 成就系统Service接口
 *
 * @author LingLearn
 */
public interface IEduAchievementService {

    /**
     * 查询成就
     *
     * @param achievementId 成就ID
     * @return 成就
     */
    public EduAchievement selectEduAchievementById(Long achievementId);

    /**
     * 查询成就列表
     *
     * @param eduAchievement 成就
     * @return 成就列表
     */
    public List<EduAchievement> selectEduAchievementList(EduAchievement eduAchievement);

    /**
     * 根据类型查询成就
     *
     * @param type 成就类型
     * @return 成就列表
     */
    public List<EduAchievement> selectEduAchievementByType(String type);

    /**
     * 新增成就
     *
     * @param eduAchievement 成就
     * @return 结果
     */
    public int insertEduAchievement(EduAchievement eduAchievement);

    /**
     * 修改成就
     *
     * @param eduAchievement 成就
     * @return 结果
     */
    public int updateEduAchievement(EduAchievement eduAchievement);

    /**
     * 删除成就
     *
     * @param achievementId 成就ID
     * @return 结果
     */
    public int deleteEduAchievementById(Long achievementId);

    /**
     * 获取用户成就列表
     *
     * @param userId 用户ID
     * @return 用户成就列表
     */
    public List<EduAchievement> getUserAchievements(Long userId);

    /**
     * 获取用户待完成成就
     *
     * @param userId 用户ID
     * @return 待完成成就列表
     */
    public List<EduAchievement> getPendingAchievements(Long userId);

    /**
     * 检查并更新用户成就进度
     *
     * @param userId 用户ID
     * @return 新完成的成就列表
     */
    public List<EduAchievement> checkAndUpdateAchievements(Long userId);

    /**
     * 获取排行榜
     *
     * @param type     排行榜类型：total|weekly|daily
     * @param language 语言
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 排行榜数据
     */
    public Map<String, Object> getLeaderboard(String type, String language, Integer pageNum, Integer pageSize);

    /**
     * 获取用户排名
     *
     * @param userId   用户ID
     * @param type     排行榜类型
     * @param language 语言
     * @return 排名
     */
    public Integer getUserRank(Long userId, String type, String language);

    /**
     * 初始化用户成就进度
     *
     * @param userId 用户ID
     */
    public void initializeUserAchievements(Long userId);
}
