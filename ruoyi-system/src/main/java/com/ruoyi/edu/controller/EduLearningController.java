package com.ruoyi.edu.controller;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.edu.domain.EduCourseLesson;
import com.ruoyi.edu.domain.EduLearningRecord;
import com.ruoyi.edu.domain.EduVocabulary;
import com.ruoyi.edu.mapper.EduCourseLessonMapper;
import com.ruoyi.edu.mapper.EduVocabularyMapper;
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

    @Autowired
    private EduCourseLessonMapper courseLessonMapper;

    @Autowired
    private EduVocabularyMapper vocabularyMapper;

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

    @Autowired
    private com.ruoyi.edu.mapper.EduCourseUnitMapper unitMapper;

    @Autowired
    private com.ruoyi.edu.mapper.EduCourseMapper courseMapper;

    @Anonymous
    @GetMapping("/lesson/{lessonId}")
    public AjaxResult getLessonDetail(@PathVariable Long lessonId) {
        EduCourseLesson lesson = courseLessonMapper.selectEduCourseLessonById(lessonId);
        if (lesson == null) {
            return error("课时不存在");
        }

        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("lessonId", lesson.getLessonId());
        result.put("lessonName", lesson.getLessonName());
        result.put("lessonType", lesson.getLessonType());
        result.put("duration", lesson.getDuration());
        result.put("content", lesson.getContent());
        result.put("resources", lesson.getResources());

        String courseName = "课程";
        String language = "en";
        if (lesson.getUnitId() != null) {
            com.ruoyi.edu.domain.EduCourseUnit unit = unitMapper.selectEduCourseUnitById(lesson.getUnitId());
            if (unit != null && unit.getCourseId() != null) {
                com.ruoyi.edu.domain.EduCourse course = courseMapper.selectEduCourseById(unit.getCourseId());
                if (course != null) {
                    courseName = course.getCourseName();
                    language = course.getLanguage();
                }
            }
        }
        result.put("courseName", courseName);
        result.put("language", language);

        EduVocabulary query = new EduVocabulary();
        query.setUnitId(lesson.getUnitId());
        java.util.List<EduVocabulary> vocabList = vocabularyMapper.selectEduVocabularyList(query);

        java.util.List<java.util.Map<String, Object>> vocabularyList = new java.util.ArrayList<>();
        for (EduVocabulary vocab : vocabList) {
            java.util.Map<String, Object> word = new java.util.HashMap<>();
            word.put("id", vocab.getVocabId());
            word.put("word", vocab.getWord());
            word.put("phonetic", vocab.getPronunciation());
            word.put("translation", vocab.getDefinition());
            word.put("example", vocab.getExampleSentences());
            word.put("partOfSpeech", vocab.getPartOfSpeech());
            vocabularyList.add(word);
        }
        result.put("vocabularyList", vocabularyList);

        java.util.List<java.util.Map<String, Object>> exampleList = new java.util.ArrayList<>();
        for (EduVocabulary vocab : vocabList) {
            String examples = vocab.getExampleSentences();
            if (examples != null && !examples.trim().isEmpty()) {
                String[] lines = examples.split("[\\r\\n]+");
                for (String line : lines) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        java.util.Map<String, Object> ex = new java.util.HashMap<>();
                        ex.put("sentence", line);
                        ex.put("translation", vocab.getDefinition());
                        exampleList.add(ex);
                    }
                }
            }
        }
        result.put("exampleList", exampleList);

        return success(result);
    }

    @PostMapping("/complete")
    public AjaxResult completeLesson(@RequestBody Map<String, Object> params) {
        Long userId = SecurityUtils.getUserId();
        Long lessonId = Long.parseLong(params.get("lessonId").toString());
        Long courseId = params.get("courseId") != null ? Long.parseLong(params.get("courseId").toString()) : null;
        Integer score = params.get("score") != null ? Integer.parseInt(params.get("score").toString()) : 0;
        Integer duration = params.get("duration") != null ? Integer.parseInt(params.get("duration").toString()) : 0;

        Map<String, Object> result = eduLearningService.completeLesson(userId, lessonId, courseId, score, duration);
        return success(result);
    }
}
