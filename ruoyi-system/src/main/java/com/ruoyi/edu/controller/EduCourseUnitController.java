package com.ruoyi.edu.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.edu.domain.EduCourseUnit;
import com.ruoyi.edu.service.IEduCourseUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/edu/unit")
public class EduCourseUnitController extends BaseController {

    @Autowired
    private IEduCourseUnitService eduCourseUnitService;

    @GetMapping("/list")
    public TableDataInfo list(EduCourseUnit eduCourseUnit) {
        startPage();
        List<EduCourseUnit> list = eduCourseUnitService.selectEduCourseUnitList(eduCourseUnit);
        return getDataTable(list);
    }

    @GetMapping("/byCourse/{courseId}")
    public AjaxResult listByCourse(@PathVariable Long courseId) {
        List<EduCourseUnit> list = eduCourseUnitService.selectUnitsByCourseId(courseId);
        return success(list);
    }

    @GetMapping("/{unitId}")
    public AjaxResult getInfo(@PathVariable Long unitId) {
        return success(eduCourseUnitService.selectEduCourseUnitById(unitId));
    }

    @PostMapping
    public AjaxResult add(@RequestBody EduCourseUnit eduCourseUnit) {
        eduCourseUnit.setCreateBy(getUsername());
        return toAjax(eduCourseUnitService.insertEduCourseUnit(eduCourseUnit));
    }

    @PutMapping
    public AjaxResult edit(@RequestBody EduCourseUnit eduCourseUnit) {
        eduCourseUnit.setUpdateBy(getUsername());
        return toAjax(eduCourseUnitService.updateEduCourseUnit(eduCourseUnit));
    }

    @DeleteMapping("/{unitIds}")
    public AjaxResult remove(@PathVariable Long[] unitIds) {
        return toAjax(eduCourseUnitService.deleteEduCourseUnitByIds(unitIds));
    }
}