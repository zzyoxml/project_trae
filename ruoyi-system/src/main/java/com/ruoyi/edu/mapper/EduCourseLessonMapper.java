package com.ruoyi.edu.mapper;

import com.ruoyi.edu.domain.EduCourseLesson;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 课时Mapper接口
 *
 * @author LingLearn
 */
@Mapper
public interface EduCourseLessonMapper {

    /**
     * 查询课时
     *
     * @param lessonId 课时ID
     * @return 课时
     */
    public EduCourseLesson selectEduCourseLessonById(Long lessonId);

    /**
     * 查询课时列表
     *
     * @param eduCourseLesson 课时
     * @return 课时列表
     */
    public java.util.List<EduCourseLesson> selectEduCourseLessonList(EduCourseLesson eduCourseLesson);

    /**
     * 查询单元的课时
     *
     * @param unitId 单元ID
     * @return 课时列表
     */
    public java.util.List<EduCourseLesson> selectLessonsByUnitId(Long unitId);

    /**
     * 新增课时
     *
     * @param eduCourseLesson 课时
     * @return 结果
     */
    public int insertEduCourseLesson(EduCourseLesson eduCourseLesson);

    /**
     * 修改课时
     *
     * @param eduCourseLesson 课时
     * @return 结果
     */
    public int updateEduCourseLesson(EduCourseLesson eduCourseLesson);

    /**
     * 删除课时
     *
     * @param lessonId 课时ID
     * @return 结果
     */
    public int deleteEduCourseLessonById(Long lessonId);

    /**
     * 批量删除课时
     *
     * @param lessonIds 需要删除的课时ID
     * @return 结果
     */
    public int deleteEduCourseLessonByIds(Long[] lessonIds);
}
