package com.ruoyi.edu.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.edu.domain.EduCourseLesson;
import com.ruoyi.edu.service.IEduCourseLessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/edu/lesson")
public class EduCourseLessonController extends BaseController {

    @Autowired
    private IEduCourseLessonService eduCourseLessonService;

    @GetMapping("/list")
    public TableDataInfo list(EduCourseLesson eduCourseLesson) {
        startPage();
        List<EduCourseLesson> list = eduCourseLessonService.selectEduCourseLessonList(eduCourseLesson);
        return getDataTable(list);
    }

    @GetMapping("/byUnit/{unitId}")
    public AjaxResult listByUnit(@PathVariable Long unitId) {
        List<EduCourseLesson> list = eduCourseLessonService.selectLessonsByUnitId(unitId);
        return success(list);
    }

    @GetMapping("/{lessonId}")
    public AjaxResult getInfo(@PathVariable Long lessonId) {
        return success(eduCourseLessonService.selectEduCourseLessonById(lessonId));
    }

    @PostMapping
    public AjaxResult add(@RequestBody EduCourseLesson eduCourseLesson) {
        eduCourseLesson.setCreateBy(getUsername());
        return toAjax(eduCourseLessonService.insertEduCourseLesson(eduCourseLesson));
    }

    @PutMapping
    public AjaxResult edit(@RequestBody EduCourseLesson eduCourseLesson) {
        eduCourseLesson.setUpdateBy(getUsername());
        return toAjax(eduCourseLessonService.updateEduCourseLesson(eduCourseLesson));
    }

    @DeleteMapping("/{lessonIds}")
    public AjaxResult remove(@PathVariable Long[] lessonIds) {
        return toAjax(eduCourseLessonService.deleteEduCourseLessonByIds(lessonIds));
    }
}