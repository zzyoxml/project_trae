package com.ruoyi.edu.mapper;

import com.ruoyi.edu.domain.EduUserAchievement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户成就Mapper接口
 *
 * @author LingLearn
 */
@Mapper
public interface EduUserAchievementMapper {

    /**
     * 查询用户成就
     *
     * @param userId        用户ID
     * @param achievementId 成就ID
     * @return 用户成就
     */
    public EduUserAchievement selectUserAchievement(
            @Param("userId") Long userId,
            @Param("achievementId") Long achievementId);

    /**
     * 查询用户成就列表
     *
     * @param userId 用户ID
     * @return 用户成就列表
     */
    public java.util.List<EduUserAchievement> selectUserAchievementList(Long userId);

    /**
     * 新增用户成就
     *
     * @param userAchievement 用户成就
     * @return 结果
     */
    public int insertEduUserAchievement(EduUserAchievement userAchievement);

    /**
     * 修改用户成就
     *
     * @param userAchievement 用户成就
     * @return 结果
     */
    public int updateEduUserAchievement(EduUserAchievement userAchievement);

    /**
     * 删除用户成就
     *
     * @param userId        用户ID
     * @param achievementId 成就ID
     * @return 结果
     */
    public int deleteUserAchievement(
            @Param("userId") Long userId,
            @Param("achievementId") Long achievementId);

    /**
     * 统计用户已获得的成就数量
     *
     * @param userId 用户ID
     * @return 已获得成就数
     */
    public Integer countAchievements(@Param("userId") Long userId);
}
