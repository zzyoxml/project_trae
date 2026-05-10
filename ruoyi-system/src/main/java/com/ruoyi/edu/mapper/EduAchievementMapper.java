package com.ruoyi.edu.mapper;

import com.ruoyi.edu.domain.EduAchievement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 成就Mapper接口
 *
 * @author LingLearn
 */
@Mapper
public interface EduAchievementMapper {

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
    public List<EduAchievement> selectEduAchievementByType(@Param("type") String type);

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
     * 批量删除成就
     *
     * @param achievementIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteEduAchievementByIds(Long[] achievementIds);

    /**
     * 获取用户已完成成就数
     *
     * @param userId 用户ID
     * @return 已完成成就数
     */
    public int countUserCompletedAchievements(Long userId);

    /**
     * 获取用户待完成成就
     *
     * @param userId 用户ID
     * @return 待完成成就列表
     */
    public List<EduAchievement> selectPendingAchievements(Long userId);
}
