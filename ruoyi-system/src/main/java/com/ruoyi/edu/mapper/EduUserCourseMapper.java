package com.ruoyi.edu.mapper;

import com.ruoyi.edu.domain.EduUserCourse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户课程Mapper接口
 *
 * @author LingLearn
 */
@Mapper
public interface EduUserCourseMapper {

    /**
     * 查询用户课程
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 用户课程
     */
    public EduUserCourse selectUserCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);

    /**
     * 新增用户课程
     *
     * @param userCourse 用户课程
     * @return 结果
     */
    public int insertEduUserCourse(EduUserCourse userCourse);

    /**
     * 修改用户课程
     *
     * @param userCourse 用户课程
     * @return 结果
     */
    public int updateEduUserCourse(EduUserCourse userCourse);

    /**
     * 删除用户课程
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 结果
     */
    public int deleteUserCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);
}
