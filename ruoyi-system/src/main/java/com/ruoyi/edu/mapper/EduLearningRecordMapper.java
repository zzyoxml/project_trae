package com.ruoyi.edu.mapper;

import com.ruoyi.edu.domain.EduLearningRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 学习记录Mapper接口
 *
 * @author LingLearn
 */
@Mapper
public interface EduLearningRecordMapper {

    /**
     * 查询学习记录
     *
     * @param recordId 记录ID
     * @return 学习记录
     */
    public EduLearningRecord selectEduLearningRecordById(Long recordId);

    /**
     * 查询学习记录列表
     *
     * @param eduLearningRecord 学习记录
     * @return 学习记录列表
     */
    public java.util.List<EduLearningRecord> selectEduLearningRecordList(EduLearningRecord eduLearningRecord);

    /**
     * 新增学习记录
     *
     * @param eduLearningRecord 学习记录
     * @return 结果
     */
    public int insertEduLearningRecord(EduLearningRecord eduLearningRecord);

    /**
     * 修改学习记录
     *
     * @param eduLearningRecord 学习记录
     * @return 结果
     */
    public int updateEduLearningRecord(EduLearningRecord eduLearningRecord);

    /**
     * 删除学习记录
     *
     * @param recordId 记录ID
     * @return 结果
     */
    public int deleteEduLearningRecordById(Long recordId);

    /**
     * 批量删除学习记录
     *
     * @param recordIds 需要删除的记录ID
     * @return 结果
     */
    public int deleteEduLearningRecordByIds(Long[] recordIds);

    /**
     * 查询用户的学习记录
     *
     * @param userId 用户ID
     * @param days   查询天数
     * @return 学习记录列表
     */
    public java.util.List<EduLearningRecord> selectUserRecentRecords(
            @org.apache.ibatis.annotations.Param("userId") Long userId,
            @org.apache.ibatis.annotations.Param("days") Integer days);

    /**
     * 获取用户今日学习时长
     *
     * @param userId 用户ID
     * @return 今日学习时长（分钟）
     */
    public int getTodayStudyTime(Long userId);

    /**
     * 获取用户本周学习时长
     *
     * @param userId 用户ID
     * @return 本周学习时长（分钟）
     */
    public int getWeekStudyTime(Long userId);

    /**
     * 获取用户学习统计
     *
     * @param userId 用户ID
     * @return 统计数据
     */
    public java.util.Map<String, Object> getUserStudyStats(Long userId);

    /**
     * 获取用户技能评分
     *
     * @param userId    用户ID
     * @param skillType 技能类型
     * @return 技能评分
     */
    public Integer getUserSkillScore(
            @org.apache.ibatis.annotations.Param("userId") Long userId,
            @org.apache.ibatis.annotations.Param("skillType") String skillType);

    /**
     * 获取用户今日完成课时数
     *
     * @param userId 用户ID
     * @return 今日完成课时数
     */
    public int getTodayCompletedLessons(Long userId);
}
