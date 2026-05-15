package com.ruoyi.edu.mapper;

import com.ruoyi.edu.domain.EduLearningProgress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学习进度Mapper接口
 *
 * @author LingLearn
 */
@Mapper
public interface EduLearningProgressMapper {

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
    public EduLearningProgress selectEduLearningProgressByUserAndLesson(
            @Param("userId") Long userId, @Param("lessonId") Long lessonId);

    /**
     * 查询用户课程的学习进度列表
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 学习进度列表
     */
    public List<EduLearningProgress> selectEduLearningProgressByCourse(
            @Param("userId") Long userId, @Param("courseId") Long courseId);

    /**
     * 查询用户的学习进度列表
     *
     * @param eduLearningProgress 学习进度
     * @return 学习进度列表
     */
    public List<EduLearningProgress> selectEduLearningProgressList(EduLearningProgress eduLearningProgress);

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
     * 删除学习进度
     *
     * @param progressId 进度ID
     * @return 结果
     */
    public int deleteEduLearningProgressById(Long progressId);

    /**
     * 批量删除学习进度
     *
     * @param progressIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteEduLearningProgressByIds(Long[] progressIds);

    /**
     * 查询用户已完成课时数
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 已完成课时数
     */
    public int countCompletedLessons(@Param("userId") Long userId, @Param("courseId") Long courseId);

    /**
     * 计算课程完成进度百分比
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @param total    总课时数
     * @return 完成百分比
     */
    public int calculateCourseProgress(@Param("userId") Long userId,
                                        @Param("courseId") Long courseId,
                                        @Param("total") Integer total);

    /**
     * 获取用户连续学习天数
     *
     * @param userId 用户ID
     * @return 连续学习天数
     */
    public int getUserStreak(Long userId);

    /**
     * 查询用户的所有学习进度
     *
     * @param userId 用户ID
     * @return 学习进度列表
     */
    public List<EduLearningProgress> selectEduLearningProgressByUser(@Param("userId") Long userId);
}
