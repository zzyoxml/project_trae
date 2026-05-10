package com.ruoyi.edu.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.edu.domain.EduLearningRecord;
import com.ruoyi.edu.service.IEduLearningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 学习Controller
 *
 * @author LingLearn
 */
@RestController
@RequestMapping("/edu/learning")
public class EduLearningController extends BaseController {

    @Autowired
    private IEduLearningService eduLearningService;

    /**
     * 开始学习课时
     */
    @PostMapping("/start")
    public AjaxResult startLesson(@RequestBody Map<String, Long> params) {
        Long userId = SecurityUtils.getUserId();
        Long lessonId = params.get("lessonId");
        Long courseId = params.get("courseId");

        Long recordId = eduLearningService.startLesson(userId, lessonId, courseId);
        return success().put("recordId", recordId);
    }

    /**
     * 提交学习结果
     */
    @PostMapping("/submit")
    public AjaxResult submitResult(@RequestBody Map<String, Object> params) {
        Long userId = SecurityUtils.getUserId();
        Long recordId = Long.parseLong(params.get("recordId").toString());
        Long lessonId = Long.parseLong(params.get("lessonId").toString());
        Integer score = Integer.parseInt(params.get("score").toString());
        Integer duration = Integer.parseInt(params.get("duration").toString());

        Map<String, Object> result = eduLearningService.submitLearningResult(
                recordId, userId, lessonId, score, duration);
        return success(result);
    }

    /**
     * 获取用户学习进度统计
     */
    @GetMapping("/stats")
    public AjaxResult getUserStats() {
        Long userId = SecurityUtils.getUserId();
        Map<String, Object> stats = eduLearningService.getUserProgressStats(userId);
        return success(stats);
    }

    /**
     * 获取今日学习数据
     */
    @GetMapping("/today")
    public AjaxResult getTodayData() {
        Long userId = SecurityUtils.getUserId();
        Map<String, Object> data = eduLearningService.getTodayLearningData(userId);
        return success(data);
    }

    /**
     * 获取用户技能评分
     */
    @GetMapping("/skills")
    public AjaxResult getSkillScores() {
        Long userId = SecurityUtils.getUserId();
        Map<String, Integer> scores = eduLearningService.getUserSkillScores(userId);
        return success(scores);
    }

    /**
     * 获取用户学习进度
     */
    @GetMapping("/progress/{courseId}")
    public AjaxResult getCourseProgress(@PathVariable Long courseId) {
        Long userId = SecurityUtils.getUserId();
        List<?> progress = eduLearningService.selectEduLearningProgressByCourse(userId, courseId);
        return success(progress);
    }

    /**
     * 获取用户最近学习记录
     */
    @GetMapping("/recent")
    public TableDataInfo getRecentRecords(@RequestParam(defaultValue = "7") Integer days) {
        Long userId = SecurityUtils.getUserId();
        List<EduLearningRecord> list = eduLearningService.selectUserRecentRecords(userId, days);
        return getDataTable(list);
    }

    /**
     * 查询学习记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(EduLearningRecord eduLearningRecord) {
        startPage();
        List<EduLearningRecord> list = eduLearningService.selectEduLearningRecordList(eduLearningRecord);
        return getDataTable(list);
    }

    /**
     * 获取学习记录详情
     */
    @GetMapping("/{recordId}")
    public AjaxResult getRecordDetails(@PathVariable Long recordId) {
        EduLearningRecord record = eduLearningService.selectEduLearningRecordById(recordId);
        return success(record);
    }
}
