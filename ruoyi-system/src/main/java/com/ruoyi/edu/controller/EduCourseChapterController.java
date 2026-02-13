package com.ruoyi.edu.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.edu.domain.EduCourseChapter;
import com.ruoyi.edu.service.IEduCourseChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/edu/chapter")
public class EduCourseChapterController extends BaseController {

    @Autowired
    private IEduCourseChapterService eduCourseChapterService;

    @GetMapping("/list")
    public TableDataInfo list(EduCourseChapter eduCourseChapter) {
        startPage();
        List<EduCourseChapter> list = eduCourseChapterService.selectEduCourseChapterList(eduCourseChapter);
        return getDataTable(list);
    }

    @GetMapping("/byCourse/{courseId}")
    public AjaxResult listByCourse(@PathVariable Long courseId) {
        List<EduCourseChapter> list = eduCourseChapterService.selectChaptersByCourseId(courseId);
        return success(list);
    }

    @GetMapping("/{chapterId}")
    public AjaxResult getInfo(@PathVariable Long chapterId) {
        return success(eduCourseChapterService.selectEduCourseChapterById(chapterId));
    }

    @PostMapping
    public AjaxResult add(@RequestBody EduCourseChapter eduCourseChapter) {
        eduCourseChapter.setCreateBy(getUsername());
        return toAjax(eduCourseChapterService.insertEduCourseChapter(eduCourseChapter));
    }

    @PutMapping
    public AjaxResult edit(@RequestBody EduCourseChapter eduCourseChapter) {
        eduCourseChapter.setUpdateBy(getUsername());
        return toAjax(eduCourseChapterService.updateEduCourseChapter(eduCourseChapter));
    }

    @DeleteMapping("/{chapterIds}")
    public AjaxResult remove(@PathVariable Long[] chapterIds) {
        return toAjax(eduCourseChapterService.deleteEduCourseChapterByIds(chapterIds));
    }
}