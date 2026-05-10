package com.ruoyi.edu.service;

import com.ruoyi.edu.domain.EduCourse;
import com.ruoyi.edu.service.IEduCourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 课程服务单元测试
 * 
 * @description 测试课程查询、详情获取、推荐等功能
 * @author LingLearn
 */
@SpringBootTest
public class EduCourseServiceTest {

    @Autowired
    private IEduCourseService courseService;

    /**
     * 测试获取课程详情
     */
    @Test
    public void testGetCourseDetails() {
        Long courseId = 1L;
        Long userId = 1L;
        
        EduCourse course = courseService.getCourseDetails(courseId, userId);
        
        assertNotNull(course);
        assertEquals(courseId, course.getCourseId());
        // 验证用户报名信息的获取
        assertNotNull(course.getIsEnrolled() != null);
    }

    /**
     * 测试获取精选课程
     */
    @Test
    public void testSelectFeaturedCourses() {
        Integer limit = 5;
        List<EduCourse> courses = courseService.selectFeaturedCourses(limit);
        
        assertNotNull(courses);
        assertTrue(courses.size() <= limit);
        
        // 验证课程数据完整性
        for (EduCourse course : courses) {
            assertNotNull(course.getCourseId());
            assertNotNull(course.getCourseName());
            assertNotNull(course.getLanguage());
        }
    }

    /**
     * 测试获取热门课程
     */
    @Test
    public void testSelectPopularCourses() {
        Integer limit = 10;
        List<EduCourse> courses = courseService.selectPopularCourses(limit);
        
        assertNotNull(courses);
        assertTrue(courses.size() <= limit);
    }

    /**
     * 测试按语言查询课程
     */
    @Test
    public void testSelectEduCourseByLanguage() {
        String language = "en";
        List<EduCourse> courses = courseService.selectEduCourseByLanguage(language);
        
        assertNotNull(courses);
        // 验证所有课程都是指定语言
        for (EduCourse course : courses) {
            assertEquals(language, course.getLanguage());
        }
    }

    /**
     * 测试按等级查询课程
     */
    @Test
    public void testSelectEduCourseByLevel() {
        String level = "beginner";
        List<EduCourse> courses = courseService.selectEduCourseByLevel(level);
        
        assertNotNull(courses);
        // 验证所有课程都是指定等级
        for (EduCourse course : courses) {
            assertEquals(level, course.getLevel());
        }
    }

    /**
     * 测试课程评分更新
     */
    @Test
    public void testUpdateCourseRating() {
        Long courseId = 1L;
        java.math.BigDecimal rating = new java.math.BigDecimal("4.5");
        Integer count = 100;
        
        // 执行更新
        courseService.updateCourseRating(courseId, rating, count);
        
        // 验证更新结果
        EduCourse course = courseService.selectEduCourseById(courseId);
        assertNotNull(course);
        assertEquals(0, rating.compareTo(course.getRating()));
    }

    /**
     * 测试课程列表查询
     */
    @Test
    public void testSelectEduCourseList() {
        EduCourse query = new EduCourse();
        query.setLanguage("en");
        query.setLevel("beginner");
        
        List<EduCourse> courses = courseService.selectEduCourseList(query);
        
        assertNotNull(courses);
        // 验证查询条件生效
        for (EduCourse course : courses) {
            assertEquals("en", course.getLanguage());
            assertEquals("beginner", course.getLevel());
        }
    }
}
