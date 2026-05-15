package com.ruoyi.edu.mapper;

import com.ruoyi.edu.domain.EduCourse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课程Mapper接口
 *
 * @author LingLearn
 */
@Mapper
public interface EduCourseMapper {

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
     * @return 课程集合
     */
    public List<EduCourse> selectEduCourseList(EduCourse eduCourse);

    /**
     * 根据语言查询课程列表
     *
     * @param language 语言
     * @return 课程集合
     */
    public List<EduCourse> selectEduCourseByLanguage(@Param("language") String language);

    /**
     * 根据等级查询课程列表
     *
     * @param level 等级
     * @return 课程集合
     */
    public List<EduCourse> selectEduCourseByLevel(@Param("level") String level);

    /**
     * 获取精选课程
     *
     * @param limit 数量限制
     * @return 精选课程列表
     */
    public List<EduCourse> selectFeaturedCourses(@Param("limit") Integer limit);

    /**
     * 获取热门课程
     *
     * @param limit 数量限制
     * @return 热门课程列表
     */
    public List<EduCourse> selectPopularCourses(@Param("limit") Integer limit);

    /**
     * 获取我的课程
     *
     * @param courseIds 课程ID列表
     * @return 我的课程列表
     */
    public List<EduCourse> selectMyCourses(@Param("courseIds") List<Long> courseIds);

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
     * @param courseIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteEduCourseByIds(Long[] courseIds);

    /**
     * 更新课程评分
     *
     * @param courseId 课程ID
     * @param rating   新评分
     * @param count    评分人数
     * @return 结果
     */
    public int updateCourseRating(@Param("courseId") Long courseId,
                                   @Param("rating") java.math.BigDecimal rating,
                                   @Param("count") Integer count);

    /**
     * 增加课程学习人数
     *
     * @param courseId 课程ID
     * @return 结果
     */
    public int incrementStudentCount(Long courseId);

    /**
     * 减少课程学习人数
     *
     * @param courseId 课程ID
     * @return 结果
     */
    public int decrementStudentCount(Long courseId);
}
