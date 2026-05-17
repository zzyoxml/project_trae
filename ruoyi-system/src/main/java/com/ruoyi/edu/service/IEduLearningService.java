package com.ruoyi.edu.service;

import com.ruoyi.edu.domain.EduLearningProgress;
import com.ruoyi.edu.domain.EduLearningRecord;

import java.util.List;
import java.util.Map;

/**
 * 学习服务Service接口
 *
 * @author LingLearn
 */
public interface IEduLearningService {

    /**
     * 查询学习进度
     *
     * @param progressId 进度ID
     * @return 学习进度
     */
    public EduLearningProgress selectEduLearningProgressById(Long progressId);

    /**
     * 查询用户特定课时的学习进度
     *
     * @param userId   用户ID
     * @param lessonId 课时ID
     * @return 学习进度
     */
    public EduLearningProgress selectEduLearningProgressByUserAndLesson(Long userId, Long lessonId);

    /**
     * 查询用户课程的学习进度列表
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 学习进度列表
     */
    public List<EduLearningProgress> selectEduLearningProgressByCourse(Long userId, Long courseId);

    /**
     * 新增学习进度
     *
     * @param eduLearningProgress 学习进度
     * @return 结果
     */
    public int insertEduLearningProgress(EduLearningProgress eduLearningProgress);

    /**
     * 修改学习进度
     *
     * @param eduLearningProgress 学习进度
     * @return 结果
     */
    public int updateEduLearningProgress(EduLearningProgress eduLearningProgress);

    /**
     * 开始学习课时
     *
     * @param userId   用户ID
     * @param lessonId 课时ID
     * @param courseId 课程ID
     * @return 学习记录ID
     */
    public Long startLesson(Long userId, Long lessonId, Long courseId);

    /**
     * 提交学习结果
     *
     * @param recordId 记录ID
     * @param userId   用户ID
     * @param lessonId 课时ID
     * @param score    得分
     * @param duration 学习时长
     * @return 学习结果
     */
    public Map<String, Object> submitLearningResult(Long recordId, Long userId, Long lessonId,
                                                     Integer score, Integer duration);

    /**
     * 获取用户学习进度统计
     *
     * @param userId 用户ID
     * @return 学习进度统计
     */
    public Map<String, Object> getUserProgressStats(Long userId);

    /**
     * 获取用户今日学习情况
     *
     * @param userId 用户ID
     * @return 学习数据
     */
    public Map<String, Object> getTodayLearningData(Long userId);

    /**
     * 获取用户技能评分
     *
     * @param userId 用户ID
     * @return 技能评分（词汇、语法、听力、口语）
     */
    public Map<String, Integer> getUserSkillScores(Long userId);

    /**
     * 获取学习记录
     *
     * @param recordId 记录ID
     * @return 学习记录
     */
    public EduLearningRecord selectEduLearningRecordById(Long recordId);

    /**
     * 查询用户的学习记录列表
     *
     * @param eduLearningRecord 学习记录
     * @return 学习记录列表
     */
    public List<EduLearningRecord> selectEduLearningRecordList(EduLearningRecord eduLearningRecord);

    /**
     * 获取用户最近学习记录
     *
     * @param userId 用户ID
     * @param days   查询天数
     * @return 学习记录列表
     */
    public List<EduLearningRecord> selectUserRecentRecords(Long userId, Integer days);

    public Map<String, Object> completeLesson(Long userId, Long lessonId, Long courseId, Integer score, Integer duration);

    public void claimTaskReward(Long userId, String taskName, Integer reward);

    /**
     * 获取学习统计数据（后台管理）
     *
     * @return 统计数据
     */
    public Map<String, Object> getLearningStats();

    /**
     * 获取用户学习列表（后台管理）
     *
     * @param userName 用户名
     * @param language 学习语言
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @return 用户学习列表
     */
    public Map<String, Object> getUserLearningList(String userName, String language, Integer pageNum, Integer pageSize);

    /**
     * 获取用户学习详情（后台管理）
     *
     * @param userId 用户ID
     * @return 用户学习详情
     */
    public Map<String, Object> getUserLearningDetail(Long userId);

    /**
     * 获取用户今日完成课时数
     *
     * @param userId 用户ID
     * @return 今日完成课时数
     */
    public int getTodayCompletedLessons(Long userId);
}
