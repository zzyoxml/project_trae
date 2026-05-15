package com.ruoyi.edu.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.edu.domain.EduCourse;
import com.ruoyi.edu.domain.EduCourseLesson;
import com.ruoyi.edu.domain.EduCourseUnit;
import com.ruoyi.edu.domain.EduUserCourse;
import com.ruoyi.edu.mapper.EduCourseLessonMapper;
import com.ruoyi.edu.mapper.EduCourseMapper;
import com.ruoyi.edu.mapper.EduCourseUnitMapper;
import com.ruoyi.edu.mapper.EduUserCourseMapper;
import com.ruoyi.edu.mapper.EduVocabularyMapper;
import com.ruoyi.edu.service.IEduCourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 课程Service实现
 *
 * @author LingLearn
 */
@Service
public class EduCourseServiceImpl implements IEduCourseService {

    private static final Logger log = LoggerFactory.getLogger(EduCourseServiceImpl.class);

    @Autowired
    private EduCourseMapper eduCourseMapper;

    @Autowired
    private EduUserCourseMapper eduUserCourseMapper;

    @Autowired
    private EduCourseUnitMapper eduCourseUnitMapper;

    @Autowired
    private EduCourseLessonMapper eduCourseLessonMapper;

    @Autowired
    private EduVocabularyMapper eduVocabularyMapper;

    /**
     * 查询课程
     *
     * @param courseId 课程ID
     * @return 课程
     */
    @Override
    public EduCourse selectEduCourseById(Long courseId) {
        return eduCourseMapper.selectEduCourseById(courseId);
    }

    /**
     * 查询课程列表
     *
     * @param eduCourse 课程
     * @return 课程列表
     */
    @Override
    public List<EduCourse> selectEduCourseList(EduCourse eduCourse) {
        return eduCourseMapper.selectEduCourseList(eduCourse);
    }

    /**
     * 根据语言查询课程
     *
     * @param language 语言
     * @return 课程列表
     */
    @Override
    public List<EduCourse> selectEduCourseByLanguage(String language) {
        return eduCourseMapper.selectEduCourseByLanguage(language);
    }

    /**
     * 根据等级查询课程
     *
     * @param level 等级
     * @return 课程列表
     */
    @Override
    public List<EduCourse> selectEduCourseByLevel(String level) {
        return eduCourseMapper.selectEduCourseByLevel(level);
    }

    /**
     * 获取精选课程
     *
     * @param limit 数量限制
     * @return 精选课程列表
     */
    @Override
    public List<EduCourse> selectFeaturedCourses(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        return eduCourseMapper.selectFeaturedCourses(limit);
    }

    /**
     * 获取热门课程
     *
     * @param limit 数量限制
     * @return 热门课程列表
     */
    @Override
    public List<EduCourse> selectPopularCourses(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        return eduCourseMapper.selectPopularCourses(limit);
    }

    @Override
    public List<EduCourse> selectMyCourses(Long userId) {
        if (userId == null) {
            return new java.util.ArrayList<>();
        }
        List<Long> courseIds = eduUserCourseMapper.selectUserCourseIds(userId);
        if (courseIds == null || courseIds.isEmpty()) {
            return new java.util.ArrayList<>();
        }
        return eduCourseMapper.selectMyCourses(courseIds);
    }

    /**
     * 新增课程
     *
     * @param eduCourse 课程
     * @return 结果
     */
    @Override
    public int insertEduCourse(EduCourse eduCourse) {
        return eduCourseMapper.insertEduCourse(eduCourse);
    }

    /**
     * 修改课程
     *
     * @param eduCourse 课程
     * @return 结果
     */
    @Override
    public int updateEduCourse(EduCourse eduCourse) {
        return eduCourseMapper.updateEduCourse(eduCourse);
    }

    /**
     * 删除课程（级联删除所有下级数据）
     *
     * @param courseId 课程ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteEduCourseById(Long courseId) {
        // 1. 获取所有单元
        List<EduCourseUnit> units = eduCourseUnitMapper.selectUnitsByCourseId(courseId);

        // 2. 删除每个单元下的课时和字词
        for (EduCourseUnit unit : units) {
            List<EduCourseLesson> lessons = eduCourseLessonMapper.selectLessonsByUnitId(unit.getUnitId());
            if (lessons != null && !lessons.isEmpty()) {
                List<Long> lessonIds = lessons.stream().map(EduCourseLesson::getLessonId).collect(java.util.stream.Collectors.toList());
                Long[] lessonIdArray = lessonIds.toArray(new Long[0]);
                // 删除课时
                eduCourseLessonMapper.deleteEduCourseLessonByIds(lessonIdArray);
                // 删除字词
                eduVocabularyMapper.deleteVocabularyByUnitId(unit.getUnitId());
            }
        }

        // 3. 删除所有单元
        if (!units.isEmpty()) {
            List<Long> unitIds = units.stream().map(EduCourseUnit::getUnitId).collect(java.util.stream.Collectors.toList());
            eduCourseUnitMapper.deleteEduCourseUnitByIds(unitIds.toArray(new Long[0]));
        }

        // 4. 删除课程
        return eduCourseMapper.deleteEduCourseById(courseId);
    }

    /**
     * 批量删除课程
     *
     * @param courseIds 课程ID数组
     * @return 结果
     */
    @Override
    public int deleteEduCourseByIds(Long[] courseIds) {
        return eduCourseMapper.deleteEduCourseByIds(courseIds);
    }

    /**
     * 获取课程详情（包含单元和课时信息）
     *
     * @param courseId 课程ID
     * @param userId   用户ID（可选）
     * @return 课程详细信息
     */
    @Override
    public EduCourse getCourseDetails(Long courseId, Long userId) {
        EduCourse course = eduCourseMapper.selectEduCourseById(courseId);
        if (course == null) {
            throw new ServiceException("课程不存在");
        }

        List<EduCourseUnit> units = eduCourseUnitMapper.selectUnitsByCourseId(courseId);
        for (EduCourseUnit unit : units) {
            List<EduCourseLesson> lessons = eduCourseLessonMapper.selectLessonsByUnitId(unit.getUnitId());
            unit.setLessons(lessons);
        }
        course.setUnits(units);

        if (userId != null) {
            EduUserCourse userCourse = eduUserCourseMapper.selectUserCourse(userId, courseId);
            course.setIsEnrolled(userCourse != null);
            if (userCourse != null) {
                course.setProgressPercent(userCourse.getProgressPercent());
            }
        }

        return course;
    }

    /**
     * 更新课程评分
     *
     * @param courseId 课程ID
     * @param rating   新评分
     * @param count    评分人数
     */
    @Override
    public void updateCourseRating(Long courseId, java.math.BigDecimal rating, Integer count) {
        eduCourseMapper.updateCourseRating(courseId, rating, count);
        log.info("更新课程评分: courseId={}, rating={}, count={}", courseId, rating, count);
    }

    /**
     * 用户报名课程
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean enrollCourse(Long userId, Long courseId) {
        // 检查课程是否存在
        EduCourse course = eduCourseMapper.selectEduCourseById(courseId);
        if (course == null) {
            throw new ServiceException("课程不存在");
        }

        // 检查是否已报名
        EduUserCourse existCourse = eduUserCourseMapper.selectUserCourse(userId, courseId);
        if (existCourse != null) {
            throw new ServiceException("您已报名该课程");
        }

        // 创建报名记录
        EduUserCourse userCourse = new EduUserCourse();
        userCourse.setUserId(userId);
        userCourse.setCourseId(courseId);
        userCourse.setProgressPercent(0);
        userCourse.setStatus("enrolled");
        userCourse.setIsFavorite(false);

        int result = eduUserCourseMapper.insertEduUserCourse(userCourse);
        if (result > 0) {
            // 增加课程学习人数
            eduCourseMapper.incrementStudentCount(courseId);
            log.info("用户报名课程: userId={}, courseId={}", userId, courseId);
            return true;
        }

        return false;
    }

    /**
     * 用户取消报名课程
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean cancelEnrollCourse(Long userId, Long courseId) {
        // 检查是否已报名
        EduUserCourse existCourse = eduUserCourseMapper.selectUserCourse(userId, courseId);
        if (existCourse == null) {
            throw new ServiceException("您尚未报名该课程");
        }

        // 删除报名记录
        int result = eduUserCourseMapper.deleteUserCourse(userId, courseId);
        if (result > 0) {
            // 减少课程学习人数
            eduCourseMapper.decrementStudentCount(courseId);
            log.info("用户取消报名课程: userId={}, courseId={}", userId, courseId);
            return true;
        }

        return false;
    }
}
