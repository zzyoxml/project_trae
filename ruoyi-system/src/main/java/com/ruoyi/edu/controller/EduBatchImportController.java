package com.ruoyi.edu.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.edu.domain.EduCourse;
import com.ruoyi.edu.domain.EduCourseLesson;
import com.ruoyi.edu.domain.EduCourseUnit;
import com.ruoyi.edu.service.IEduCourseLessonService;
import com.ruoyi.edu.service.IEduCourseService;
import com.ruoyi.edu.service.IEduCourseUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/edu/batch")
public class EduBatchImportController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(EduBatchImportController.class);

    @Autowired
    private IEduCourseService eduCourseService;

    @Autowired
    private IEduCourseUnitService eduCourseUnitService;

    @Autowired
    private IEduCourseLessonService eduCourseLessonService;

    @PostMapping("/import")
    public AjaxResult batchImport(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return error("上传文件不能为空");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".txt")) {
            return error("仅支持上传TXT文件");
        }

        Map<String, Object> result = new HashMap<>();
        int courseCount = 0;
        int unitCount = 0;
        int lessonCount = 0;
        List<String> errors = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            EduCourse currentCourse = null;
            EduCourseUnit currentUnit = null;
            String line;
            int lineNum = 0;

            while ((line = reader.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                try {
                    if (line.startsWith("[COURSE]")) {
                        currentCourse = parseCourse(line.substring(8).trim());
                        if (currentCourse != null) {
                            currentCourse.setCreateBy(getUsername());
                            eduCourseService.insertEduCourse(currentCourse);
                            courseCount++;
                            currentUnit = null;
                        }
                    } else if (line.startsWith("[UNIT]")) {
                        if (currentCourse == null) {
                            errors.add("第" + lineNum + "行：未找到所属课程，请先定义[COURSE]");
                            continue;
                        }
                        currentUnit = parseUnit(line.substring(6).trim(), currentCourse.getCourseId());
                        if (currentUnit != null) {
                            currentUnit.setCreateBy(getUsername());
                            eduCourseUnitService.insertEduCourseUnit(currentUnit);
                            unitCount++;
                        }
                    } else if (line.startsWith("[LESSON]")) {
                        if (currentUnit == null) {
                            errors.add("第" + lineNum + "行：未找到所属单元，请先定义[UNIT]");
                            continue;
                        }
                        EduCourseLesson lesson = parseLesson(line.substring(8).trim(), currentUnit.getUnitId());
                        if (lesson != null) {
                            lesson.setCreateBy(getUsername());
                            eduCourseLessonService.insertEduCourseLesson(lesson);
                            lessonCount++;
                        }
                    } else {
                        errors.add("第" + lineNum + "行：无法识别的行格式");
                    }
                } catch (Exception e) {
                    errors.add("第" + lineNum + "行处理失败：" + e.getMessage());
                    log.error("批量导入第{}行失败", lineNum, e);
                }
            }
        } catch (Exception e) {
            log.error("批量导入文件读取失败", e);
            return error("文件读取失败：" + e.getMessage());
        }

        result.put("courseCount", courseCount);
        result.put("unitCount", unitCount);
        result.put("lessonCount", lessonCount);
        result.put("errors", errors);
        result.put("success", errors.isEmpty());

        return success(result);
    }

    private EduCourse parseCourse(String data) {
        String[] parts = data.split("\\|", -1);
        if (parts.length < 4) {
            throw new RuntimeException("课程数据格式错误，需要：名称|语言|等级|类型[|描述]");
        }

        EduCourse course = new EduCourse();
        course.setCourseName(parts[0].trim());
        course.setLanguage(parts[1].trim());
        course.setLevel(parts[2].trim());
        course.setCourseType(parts[3].trim());
        course.setDescription(parts.length > 4 ? parts[4].trim() : "");
        course.setIsFree(true);
        course.setIsPublished(false);
        course.setIsFeatured(false);
        course.setDifficultyScore(1);
        course.setTotalDuration(0);
        course.setTotalLessons(0);
        course.setTotalStudents(0);
        return course;
    }

    private EduCourseUnit parseUnit(String data, Long courseId) {
        String[] parts = data.split("\\|", -1);
        if (parts.length < 2) {
            throw new RuntimeException("单元数据格式错误，需要：名称|顺序[|描述]");
        }

        EduCourseUnit unit = new EduCourseUnit();
        unit.setCourseId(courseId);
        unit.setUnitName(parts[0].trim());
        unit.setUnitOrder(Integer.parseInt(parts[1].trim()));
        unit.setDescription(parts.length > 2 ? parts[2].trim() : "");
        unit.setIsLocked(false);
        unit.setPassingScore(60);
        unit.setExperienceReward(100);
        unit.setTotalLessons(0);
        unit.setTotalDuration(0);
        return unit;
    }

    private EduCourseLesson parseLesson(String data, Long unitId) {
        String[] parts = data.split("\\|", -1);
        if (parts.length < 4) {
            throw new RuntimeException("课时数据格式错误，需要：名称|类型|内容|顺序[|时长|是否免费]");
        }

        EduCourseLesson lesson = new EduCourseLesson();
        lesson.setUnitId(unitId);
        lesson.setLessonName(parts[0].trim());
        lesson.setLessonType(parts[1].trim());
        lesson.setContent(parts[2].trim());
        lesson.setLessonOrder(Integer.parseInt(parts[3].trim()));
        lesson.setDuration(parts.length > 4 && !parts[4].trim().isEmpty()
                ? Integer.parseInt(parts[4].trim()) : 10);
        lesson.setIsFree(parts.length > 5 ? Boolean.parseBoolean(parts[5].trim()) : true);
        lesson.setIsPreview(false);
        lesson.setExperienceReward(10);
        lesson.setCoinReward(5);
        lesson.setPassingScore(60);
        lesson.setMaxAttempts(3);
        return lesson;
    }
}