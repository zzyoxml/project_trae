package com.ruoyi.edu.controller;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.edu.domain.EduCourse;
import com.ruoyi.edu.service.IEduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课程Controller
 *
 * @author LingLearn
 */
@RestController
@RequestMapping("/edu/course")
public class EduCourseController extends BaseController {

    @Autowired
    private IEduCourseService eduCourseService;

    /**
     * 查询课程列表
     */
    @Anonymous
    @GetMapping("/list")
    public TableDataInfo list(EduCourse eduCourse) {
        startPage();
        List<EduCourse> list = eduCourseService.selectEduCourseList(eduCourse);
        return getDataTable(list);
    }

    /**
     * 获取课程详情
     */
    @Anonymous
    @GetMapping("/{courseId}")
    public AjaxResult getCourseDetails(@PathVariable Long courseId) {
        Long userId = null;
        try {
            userId = SecurityUtils.getUserId();
        } catch (Exception e) {
            // 未登录用户
        }
        EduCourse course = eduCourseService.getCourseDetails(courseId, userId);
        return success(course);
    }

    /**
     * 根据语言获取课程
     */
    @Anonymous
    @GetMapping("/language/{language}")
    public AjaxResult getCoursesByLanguage(@PathVariable String language) {
        List<EduCourse> list = eduCourseService.selectEduCourseByLanguage(language);
        return success(list);
    }

    /**
     * 根据等级获取课程
     */
    @Anonymous
    @GetMapping("/level/{level}")
    public AjaxResult getCoursesByLevel(@PathVariable String level) {
        List<EduCourse> list = eduCourseService.selectEduCourseByLevel(level);
        return success(list);
    }

    /**
     * 获取精选课程
     */
    @Anonymous
    @GetMapping("/featured")
    public AjaxResult getFeaturedCourses(@RequestParam(defaultValue = "10") Integer limit) {
        List<EduCourse> list = eduCourseService.selectFeaturedCourses(limit);
        return success(list);
    }

    /**
     * 获取热门课程
     */
    @Anonymous
    @GetMapping("/popular")
    public AjaxResult getPopularCourses(@RequestParam(defaultValue = "10") Integer limit) {
        List<EduCourse> list = eduCourseService.selectPopularCourses(limit);
        return success(list);
    }

    /**
     * 用户报名课程
     */
    @PostMapping("/enroll/{courseId}")
    public AjaxResult enrollCourse(@PathVariable Long courseId) {
        Long userId = SecurityUtils.getUserId();
        boolean result = eduCourseService.enrollCourse(userId, courseId);
        return success(result);
    }

    /**
     * 新增课程
     */
    @PostMapping
    public AjaxResult add(@RequestBody EduCourse eduCourse) {
        eduCourse.setCreateBy(getUsername());
        return toAjax(eduCourseService.insertEduCourse(eduCourse));
    }

    @PutMapping
    public AjaxResult edit(@RequestBody EduCourse eduCourse) {
        eduCourse.setUpdateBy(getUsername());
        return toAjax(eduCourseService.updateEduCourse(eduCourse));
    }

    /**
     * 删除课程
     */
    @DeleteMapping("/{courseIds}")
    public AjaxResult remove(@PathVariable Long[] courseIds) {
        return toAjax(eduCourseService.deleteEduCourseByIds(courseIds));
    }
}
