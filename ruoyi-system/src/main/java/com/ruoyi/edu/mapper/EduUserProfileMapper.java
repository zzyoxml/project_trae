package com.ruoyi.edu.mapper;

import com.ruoyi.edu.domain.EduUserProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户扩展信息Mapper接口
 *
 * @author LingLearn
 */
@Mapper
public interface EduUserProfileMapper {

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
     * 更新学习时间
     *
     * @param userId   用户ID
     * @param duration 学习时长（分钟）
     * @return 结果
     */
    public int updateStudyTime(@Param("userId") Long userId, @Param("duration") Integer duration);

    /**
     * 更新连续学习天数
     *
     * @param userId 用户ID
     * @return 结果
     */
    public int updateStreak(Long userId);

    /**
     * 更新用户积分和等级
     *
     * @param userId      用户ID
     * @param points      增加的积分
     * @param experience  增加的经验
     * @return 结果
     */
    public int updatePointsAndExperience(@Param("userId") Long userId,
                                         @Param("points") Integer points,
                                         @Param("experience") Integer experience);
}
