package com.ruoyi.edu.service;

import com.ruoyi.edu.domain.EduCourse;

import java.util.List;

/**
 * 课程Service接口
 *
 * @author LingLearn
 */
public interface IEduCourseService {

    /**
     * 查询课程
     *
     * @param courseId 课程ID
     * @return 课程
     */
    public EduCourse selectEduCourseById(Long courseId);

    /**
     * 查询课程列表
     *
     * @param eduCourse 课程
     * @return 课程列表
     */
    public List<EduCourse> selectEduCourseList(EduCourse eduCourse);

    /**
     * 根据语言查询课程
     *
     * @param language 语言
     * @return 课程列表
     */
    public List<EduCourse> selectEduCourseByLanguage(String language);

    /**
     * 根据等级查询课程
     *
     * @param level 等级
     * @return 课程列表
     */
    public List<EduCourse> selectEduCourseByLevel(String level);

    /**
     * 获取精选课程
     *
     * @param limit 数量限制
     * @return 精选课程列表
     */
    public List<EduCourse> selectFeaturedCourses(Integer limit);

    /**
     * 获取热门课程
     *
     * @param limit 数量限制
     * @return 热门课程列表
     */
    public List<EduCourse> selectPopularCourses(Integer limit);

    /**
     * 新增课程
     *
     * @param eduCourse 课程
     * @return 结果
     */
    public int insertEduCourse(EduCourse eduCourse);

    /**
     * 修改课程
     *
     * @param eduCourse 课程
     * @return 结果
     */
    public int updateEduCourse(EduCourse eduCourse);

    /**
     * 删除课程
     *
     * @param courseId 课程ID
     * @return 结果
     */
    public int deleteEduCourseById(Long courseId);

    /**
     * 批量删除课程
     *
     * @param courseIds 课程ID数组
     * @return 结果
     */
    public int deleteEduCourseByIds(Long[] courseIds);

    /**
     * 获取课程详情（包含单元和课时信息）
     *
     * @param courseId 课程ID
     * @param userId   用户ID（可选）
     * @return 课程详细信息
     */
    public EduCourse getCourseDetails(Long courseId, Long userId);

    /**
     * 更新课程评分
     *
     * @param courseId 课程ID
     * @param rating   新评分
     * @param count    评分人数
     */
    public void updateCourseRating(Long courseId, java.math.BigDecimal rating, Integer count);

    /**
     * 用户报名课程
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 是否成功
     */
    public boolean enrollCourse(Long userId, Long courseId);
}
