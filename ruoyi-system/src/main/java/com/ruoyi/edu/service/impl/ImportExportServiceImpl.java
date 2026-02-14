package com.ruoyi.edu.service.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.edu.domain.EduCourse;
import com.ruoyi.edu.domain.EduCourseLesson;
import com.ruoyi.edu.domain.EduCourseUnit;
import com.ruoyi.edu.domain.EduVocabulary;
import com.ruoyi.edu.mapper.EduCourseLessonMapper;
import com.ruoyi.edu.mapper.EduCourseMapper;
import com.ruoyi.edu.mapper.EduCourseUnitMapper;
import com.ruoyi.edu.service.IImportExportService;
import com.ruoyi.edu.service.IEduCourseLessonService;
import com.ruoyi.edu.service.IEduCourseService;
import com.ruoyi.edu.service.IEduCourseUnitService;
import com.ruoyi.edu.service.IEduVocabularyService;
import com.ruoyi.common.utils.SecurityUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 导入导出服务实现 - 智能版
 * 支持自动创建课程和单元，智能识别多种格式
 */
@Service
public class ImportExportServiceImpl implements IImportExportService {

    private static final Logger log = LoggerFactory.getLogger(ImportExportServiceImpl.class);

    @Autowired
    private IEduCourseService eduCourseService;

    @Autowired
    private IEduCourseUnitService eduCourseUnitService;

    @Autowired
    private IEduCourseLessonService eduCourseLessonService;

    @Autowired
    private IEduVocabularyService eduVocabularyService;

    @Autowired
    private EduCourseMapper eduCourseMapper;

    @Autowired
    private EduCourseUnitMapper eduCourseUnitMapper;

    @Autowired
    private EduCourseLessonMapper eduCourseLessonMapper;

    @Autowired
    private ObjectMapper objectMapper;

    // 智能识别模式：词性标注模式（如 n. adj. v. 等）
    private static final Pattern POS_PATTERN = Pattern.compile(
        "^(\\w+)\\s+([a-z]+\\.\\s*.+)$",
        Pattern.CASE_INSENSITIVE
    );

    // 多种分隔符模式
    private static final String[] DELIMITERS = {"\t", "  ", " | ", " → ", " -> ", "=", ":"};

    /**
     * 词汇条目结构（用于解析JSON）
     */
    private static class VocabularyItem {
        private String word;
        private List<String> definitions;
        private List<Map<String, String>> phrases;

        public String getWord() { return word; }
        public void setWord(String word) { this.word = word; }
        public List<String> getDefinitions() { return definitions; }
        public void setDefinitions(List<String> definitions) { this.definitions = definitions; }
        public List<Map<String, String>> getPhrases() { return phrases; }
        public void setPhrases(List<Map<String, String>> phrases) { this.phrases = phrases; }
    }

    @Override
    @Transactional
    public Map<String, Object> smartImportVocabulary(MultipartFile file, Long courseId, Long unitId,
                                                      String courseName, String unitName) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 1. 确保课程存在
            if (courseId == null) {
                if (courseName == null || courseName.trim().isEmpty()) {
                    courseName = extractCourseNameFromFile(file.getOriginalFilename());
                }
                courseId = getOrCreateCourse(courseName);
                result.put("courseCreated", true);
                result.put("courseName", courseName);
            }
            result.put("courseId", courseId);

            // 2. 根据文件类型解析所有单词
            List<Map<String, String>> allWords = new ArrayList<>();
            String filename = file.getOriginalFilename();

            if (filename != null && filename.toLowerCase().endsWith(".json")) {
                allWords = parseJsonFile(file);
            } else {
                allWords = parseTxtFile(file);
            }

            if (allWords.isEmpty()) {
                result.put("success", false);
                result.put("error", "文件中没有找到有效词汇");
                return result;
            }

            EduCourse course = eduCourseMapper.selectEduCourseById(courseId);
            String courseLanguage = (course != null && course.getLanguage() != null) ? course.getLanguage() : "en";

            Map<String, Object> importResult = importWithStructure(courseId, allWords, courseLanguage);
            result.putAll(importResult);
            result.put("success", true);

        } catch (Exception e) {
            log.error("智能导入失败", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        return result;
    }

    /**
     * 按结构化方式导入词汇
     * 50个单词 = 1个课时
     * 20个课时 = 1个章节（单元）
     */
    private Map<String, Object> importWithStructure(Long courseId, List<Map<String, String>> allWords, String courseLanguage) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        List<String> errors = new ArrayList<>();
        String username = SecurityUtils.getUsername();

        int WORDS_PER_LESSON = 50;
        int LESSONS_PER_CHAPTER = 20;

        int totalWords = allWords.size();
        int totalLessons = (int) Math.ceil((double) totalWords / WORDS_PER_LESSON);
        int totalChapters = (int) Math.ceil((double) totalLessons / LESSONS_PER_CHAPTER);

        int currentWordIndex = 0;
        int currentChapter = 0;
        int currentLessonInChapter = 0;

        while (currentWordIndex < totalWords) {
            currentChapter++;
            String chapterName = "第" + currentChapter + "章";
            EduCourseUnit chapter = new EduCourseUnit();
            chapter.setCourseId(courseId);
            chapter.setUnitName(chapterName);
            chapter.setDescription("自动创建的章节");
            chapter.setUnitOrder(currentChapter);
            chapter.setCreateBy(username);
            eduCourseUnitService.insertEduCourseUnit(chapter);
            Long chapterId = chapter.getUnitId();

            currentLessonInChapter = 0;
            while (currentLessonInChapter < LESSONS_PER_CHAPTER && currentWordIndex < totalWords) {
                currentLessonInChapter++;
                int lessonNum = ((currentChapter - 1) * LESSONS_PER_CHAPTER) + currentLessonInChapter;
                String lessonName = "第" + lessonNum + "课";

                List<Map<String, String>> lessonWords = new ArrayList<>();
                for (int i = 0; i < WORDS_PER_LESSON && currentWordIndex < totalWords; i++) {
                    lessonWords.add(allWords.get(currentWordIndex));
                    currentWordIndex++;
                }

                StringBuilder content = new StringBuilder();
                for (Map<String, String> w : lessonWords) {
                    content.append(w.get("word")).append("：").append(w.get("definition")).append("\n");
                }

                EduCourseLesson lesson = new EduCourseLesson();
                lesson.setUnitId(chapterId);
                lesson.setLessonName(lessonName);
                lesson.setLessonType("vocabulary");
                lesson.setContent(content.toString());
                lesson.setLessonOrder(currentLessonInChapter);
                lesson.setDuration(5);
                lesson.setIsFree(true);
                lesson.setIsPreview(false);
                lesson.setExperienceReward(5);
                lesson.setCoinReward(2);
                lesson.setPassingScore(60);
                lesson.setMaxAttempts(3);
                lesson.setCreateBy(username);
                eduCourseLessonService.insertEduCourseLesson(lesson);
                Long lessonId = lesson.getLessonId();

                for (Map<String, String> w : lessonWords) {
                    try {
                        EduVocabulary vocabulary = new EduVocabulary();
                        vocabulary.setUnitId(chapterId);
                        vocabulary.setLessonId(lessonId);
                        vocabulary.setWord(w.get("word"));
                        vocabulary.setDefinition(w.get("definition"));
                        vocabulary.setLanguage(courseLanguage);
                        vocabulary.setPartOfSpeech(extractPartOfSpeech(w.get("definition")));
                        vocabulary.setCreateBy(username);
                        eduVocabularyService.insertEduVocabulary(vocabulary);
                        successCount++;
                    } catch (Exception e) {
                        failCount++;
                        errors.add("单词[" + w.get("word") + "]保存失败：" + e.getMessage());
                    }
                }
            }
        }

        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("errors", errors);
        result.put("chapterCount", currentChapter);
        result.put("lessonCount", totalLessons);
        result.put("success", failCount == 0);
        return result;
    }

    /**
     * 解析TXT文件
     */
    private List<Map<String, String>> parseTxtFile(MultipartFile file) {
        List<Map<String, String>> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#") || line.startsWith("//")) {
                    continue;
                }
                Map<String, String> parsed = smartParseLine(line);
                if (parsed != null && parsed.get("word") != null && !parsed.get("word").isEmpty()) {
                    words.add(parsed);
                }
            }
        } catch (Exception e) {
            log.error("解析TXT文件失败", e);
        }
        return words;
    }

    /**
     * 解析JSON文件
     */
    private List<Map<String, String>> parseJsonFile(MultipartFile file) {
        List<Map<String, String>> words = new ArrayList<>();
        try {
            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            List<VocabularyItem> items = objectMapper.readValue(content,
                    new TypeReference<List<VocabularyItem>>() {});

            for (VocabularyItem item : items) {
                if (item.getWord() != null && !item.getWord().trim().isEmpty()) {
                    Map<String, String> wordMap = new HashMap<>();
                    wordMap.put("word", item.getWord().trim());
                    StringBuilder definition = new StringBuilder();
                    if (item.getDefinitions() != null && !item.getDefinitions().isEmpty()) {
                        definition.append(String.join("；", item.getDefinitions()));
                    }
                    wordMap.put("definition", definition.toString());
                    words.add(wordMap);
                }
            }
        } catch (Exception e) {
            log.error("解析JSON文件失败", e);
        }
        return words;
    }

    /**
     * 从文件名提取课程名
     */
    private String extractCourseNameFromFile(String filename) {
        if (filename == null) return "导入课程";
        // 移除扩展名
        String name = filename.replaceAll("\\.(txt|json)$", "");
        // 移除常见的单元标识
        name = name.replaceAll("(?i)(unit|lesson|chapter|part)\\s*\\d+", "").trim();
        // 如果为空，使用默认名
        if (name.isEmpty()) {
            name = "导入课程";
        }
        return name;
    }

    /**
     * 从文件名提取单元名
     */
    private String extractUnitNameFromFile(String filename) {
        if (filename == null) return "默认单元";
        // 尝试匹配单元标识
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
            "(?i)(unit|lesson|chapter|part)\\s*(\\d+)",
            java.util.regex.Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = pattern.matcher(filename);
        if (matcher.find()) {
            return matcher.group(1) + " " + matcher.group(2);
        }
        // 使用文件名（不含扩展名）
        String name = filename.replaceAll("\\.(txt|json)$", "").trim();
        if (name.isEmpty()) {
            name = "默认单元";
        }
        return name;
    }

    /**
     * 获取或创建课程
     */
    private Long getOrCreateCourse(String courseName) {
        // 先查找是否已存在
        EduCourse query = new EduCourse();
        query.setCourseName(courseName);
        List<EduCourse> existing = eduCourseService.selectEduCourseList(query);
        
        if (existing != null && !existing.isEmpty()) {
            return existing.get(0).getCourseId();
        }
        
        // 创建新课程
        EduCourse course = new EduCourse();
        course.setCourseName(courseName);
        course.setCourseCode(generateCourseCode(courseName));
        course.setLanguage("en");
        course.setLevel("beginner");
        course.setCourseType("vocabulary");
        course.setDescription("从文件导入的课程：" + courseName);
        course.setIsFree(true);
        course.setPrice(new java.math.BigDecimal("0"));
        course.setIsPublished(true);
        course.setIsFeatured(false);
        course.setTotalDuration(0);
        course.setTotalLessons(0);
        course.setTotalStudents(0);
        course.setCreateBy(SecurityUtils.getUsername());
        
        eduCourseService.insertEduCourse(course);
        return course.getCourseId();
    }

    /**
     * 生成课程编码
     */
    private String generateCourseCode(String courseName) {
        String code = courseName.toLowerCase()
            .replaceAll("[^a-z0-9]", "-")
            .replaceAll("-+", "-")
            .replaceAll("^-|-$", "");
        if (code.length() > 20) {
            code = code.substring(0, 20);
        }
        return code + "-" + System.currentTimeMillis() % 10000;
    }

    /**
     * 获取或创建单元
     */
    private Long getOrCreateUnit(Long courseId, String unitName) {
        // 先查找是否已存在
        List<EduCourseUnit> existing = eduCourseUnitMapper.selectUnitsByCourseId(courseId);
        if (existing != null) {
            for (EduCourseUnit unit : existing) {
                if (unitName.equals(unit.getUnitName())) {
                    return unit.getUnitId();
                }
            }
        }
        
        // 创建新单元
        EduCourseUnit unit = new EduCourseUnit();
        unit.setCourseId(courseId);
        unit.setUnitName(unitName);
        unit.setUnitOrder(existing != null ? existing.size() + 1 : 1);
        unit.setDescription("从文件导入的单元");
        unit.setTotalLessons(0);
        unit.setTotalDuration(0);
        unit.setIsLocked(false);
        unit.setPassingScore(60);
        unit.setExperienceReward(10);
        unit.setCreateBy(SecurityUtils.getUsername());
        
        eduCourseUnitService.insertEduCourseUnit(unit);
        return unit.getUnitId();
    }

    @Override
    @Transactional
    public Map<String, Object> importVocabularyTxt(MultipartFile file, Long unitId) {
        return new HashMap<>();
    }

    /**
     * 智能解析一行文本
     * 支持格式：
     * 1. boat 	 n. 小船；轮船 v. 划船
     * 2. boat n. 小船；轮船 v. 划船
     * 3. boat | n. 小船；轮船 v. 划船
     * 4. boat -> n. 小船；轮船 v. 划船
     * 5. boat=小船；轮船
     * 6. {"word": "boat", "definitions": [...]}
     */
    private Map<String, String> smartParseLine(String line) {
        Map<String, String> result = new HashMap<>();
        
        // 尝试1：制表符分隔（最优先）
        int tabIndex = line.indexOf('\t');
        if (tabIndex > 0) {
            String word = line.substring(0, tabIndex).trim();
            String definition = line.substring(tabIndex + 1).trim();
            if (!word.isEmpty() && !definition.isEmpty()) {
                result.put("word", word);
                result.put("definition", formatDefinition(definition));
                return result;
            }
        }
        
        // 尝试2：多个空格分隔（2个或以上空格）
        String[] parts = line.split("\\s{2,}", 2);
        if (parts.length == 2) {
            String word = parts[0].trim();
            String definition = parts[1].trim();
            if (!word.isEmpty() && !definition.isEmpty()) {
                result.put("word", word);
                result.put("definition", formatDefinition(definition));
                return result;
            }
        }
        
        // 尝试3：其他分隔符
        for (String delimiter : new String[]{" | ", " → ", " -> ", "=", ":"}) {
            int index = line.indexOf(delimiter);
            if (index > 0) {
                String word = line.substring(0, index).trim();
                String definition = line.substring(index + delimiter.length()).trim();
                if (!word.isEmpty() && !definition.isEmpty()) {
                    result.put("word", word);
                    result.put("definition", formatDefinition(definition));
                    return result;
                }
            }
        }
        
        // 尝试4：词性标注模式（如 "boat n. 小船 v. 划船"）
        // 匹配：单词 + 空格 + 词性标注
        String[] tokens = line.split("\\s+");
        if (tokens.length >= 2) {
            String word = tokens[0];
            // 检查第二个token是否以词性标注开头（如 n. v. adj. 等）
            if (tokens[1].matches("^[a-z]+\\..*")) {
                StringBuilder definition = new StringBuilder();
                for (int i = 1; i < tokens.length; i++) {
                    if (i > 1) definition.append(" ");
                    definition.append(tokens[i]);
                }
                result.put("word", word);
                result.put("definition", formatDefinition(definition.toString()));
                return result;
            }
        }
        
        // 尝试5：第一个空格分隔（如果前面都失败了）
        int firstSpace = line.indexOf(' ');
        if (firstSpace > 0) {
            String word = line.substring(0, firstSpace).trim();
            String definition = line.substring(firstSpace + 1).trim();
            if (!word.isEmpty() && !definition.isEmpty() && word.length() < 50) {
                result.put("word", word);
                result.put("definition", formatDefinition(definition));
                return result;
            }
        }
        
        return null;
    }

    /**
     * 格式化释义：智能处理词性标注
     * 输入：n. 小船；轮船 v. 划船
     * 输出：n. 小船；轮船 v. 划船（格式化后）
     */
    private String formatDefinition(String definition) {
        if (definition == null) return "";
        
        // 统一词性标注格式
        definition = definition
            .replaceAll("(?i)\\bn\\.", "n.")   // 名词
            .replaceAll("(?i)\\bv\\.", "v.")   // 动词
            .replaceAll("(?i)\\badj\\.", "adj.") // 形容词
            .replaceAll("(?i)\\badv\\.", "adv.") // 副词
            .replaceAll("(?i)\\bprep\\.", "prep.") // 介词
            .replaceAll("(?i)\\bconj\\.", "conj.") // 连词
            .replaceAll("(?i)\\bpron\\.", "pron.") // 代词
            .replaceAll("(?i)\\bart\\.", "art.")   // 冠词
            .replaceAll("(?i)\\bnum\\.", "num.")   // 数词
            .replaceAll("(?i)\\bint\\.", "int.")   // 感叹词
            .replaceAll("(?i)\\baux\\.", "aux.")   // 助动词
            .replaceAll("(?i)\\bmodal\\.", "modal.") // 情态动词
            ;
        
        // 规范化分号
        definition = definition.replace("；", "；").replace(";", "；");
        
        // 规范化词性之间的分隔
        definition = definition.replaceAll("\\s+v\\.", " v.");
        definition = definition.replaceAll("\\s+n\\.", " n.");
        definition = definition.replaceAll("\\s+adj\\.", " adj.");
        definition = definition.replaceAll("\\s+adv\\.", " adv.");
        
        return definition.trim();
    }

    @Override
    @Transactional
    public Map<String, Object> importVocabularyJson(MultipartFile file, Long unitId) {
        return new HashMap<>();
    }

    @Override
    @Transactional
    public Map<String, Object> importFullCourseJson(MultipartFile file, Long targetCourseId) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        List<String> errors = new ArrayList<>();

        try {
            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            Map<String, Object> courseData = objectMapper.readValue(content, new TypeReference<Map<String, Object>>() {});

            String username = SecurityUtils.getUsername();

            // 解析课程信息
            Map<String, Object> courseInfo = (Map<String, Object>) courseData.get("course");
            String courseName = courseInfo != null ? (String) courseInfo.get("courseName") : "导入课程";
            String language = courseInfo != null ? (String) courseInfo.get("language") : "en";

            // 获取或创建课程
            Long courseId = targetCourseId;
            if (courseId == null) {
                EduCourse course = new EduCourse();
                course.setCourseName(courseName);
                course.setLanguage(language);
                course.setLevel("beginner");
                course.setCourseType("general");
                course.setCreateBy(username);
                eduCourseService.insertEduCourse(course);
                courseId = course.getCourseId();
                result.put("courseCreated", true);
                result.put("courseName", courseName);
            }

            // 解析单元列表
            List<Map<String, Object>> unitsList = (List<Map<String, Object>>) courseData.get("units");
            if (unitsList != null) {
                for (Map<String, Object> unitInfo : unitsList) {
                    try {
                        String unitName = (String) unitInfo.get("unitName");
                        Integer unitOrder = (Integer) unitInfo.get("unitOrder");

                        EduCourseUnit unit = new EduCourseUnit();
                        unit.setCourseId(courseId);
                        unit.setUnitName(unitName);
                        unit.setUnitOrder(unitOrder != null ? unitOrder : 1);
                        unit.setDescription((String) unitInfo.get("description"));
                        unit.setCreateBy(username);
                        eduCourseUnitService.insertEduCourseUnit(unit);
                        Long unitId = unit.getUnitId();

                        result.put("unitCreated", true);
                        result.put("unitName", unitName);
                    } catch (Exception e) {
                        failCount++;
                        errors.add("单元[" + unitInfo.get("unitName") + "]创建失败：" + e.getMessage());
                    }
                }
            }

            // 解析课时列表（按单元分组）
            Map<String, Object> lessonsByUnit = (Map<String, Object>) courseData.get("lessonsByUnit");
            if (lessonsByUnit != null) {
                // 重新查询所有单元，建立 unitId 映射
                List<EduCourseUnit> allUnits = eduCourseUnitMapper.selectUnitsByCourseId(courseId);
                Map<String, Long> unitNameToIdMap = new HashMap<>();
                for (EduCourseUnit unit : allUnits) {
                    unitNameToIdMap.put(unit.getUnitName(), unit.getUnitId());
                }

                // 遍历每个单元的课时
                for (Map.Entry<String, Object> entry : lessonsByUnit.entrySet()) {
                    String unitIdStr = entry.getKey();
                    List<Map<String, Object>> lessonsList = (List<Map<String, Object>>) entry.getValue();

                    // 从 unitIdStr 找到对应的真实 unitId
                    Long realUnitId = null;
                    for (EduCourseUnit unit : allUnits) {
                        if (String.valueOf(unit.getUnitId()).equals(unitIdStr)) {
                            realUnitId = unit.getUnitId();
                            break;
                        }
                    }

                    if (realUnitId == null || lessonsList == null) {
                        continue;
                    }

                    // 导入该单元的所有课时
                    for (Map<String, Object> lessonInfo : lessonsList) {
                        try {
                            String lessonName = (String) lessonInfo.get("lessonName");
                            Integer lessonOrder = (Integer) lessonInfo.get("lessonOrder");
                            String lessonType = (String) lessonInfo.get("lessonType");
                            String lessonContent = (String) lessonInfo.get("content");
                            Integer duration = (Integer) lessonInfo.get("duration");

                            EduCourseLesson lesson = new EduCourseLesson();
                            lesson.setUnitId(realUnitId);
                            lesson.setLessonName(lessonName);
                            lesson.setLessonOrder(lessonOrder != null ? lessonOrder : 1);
                            lesson.setLessonType(lessonType != null ? lessonType : "vocabulary");
                            lesson.setContent(lessonContent);
                            lesson.setDuration(duration != null ? duration : 10);
                            lesson.setIsFree(true);
                            lesson.setStatus("0");
                            lesson.setCreateBy(username);
                            eduCourseLessonService.insertEduCourseLesson(lesson);

                            successCount++;
                        } catch (Exception e) {
                            failCount++;
                            errors.add("课时[" + lessonInfo.get("lessonName") + "]创建失败：" + e.getMessage());
                        }
                    }
                }
            }

            result.put("success", true);
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("errors", errors);

        } catch (Exception e) {
            log.error("导入完整课程JSON失败", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }

    @Override
    public String exportCourseJson(Long courseId) {
        try {
            Map<String, Object> courseData = new HashMap<>();

            // 获取课程信息
            EduCourse course = eduCourseMapper.selectEduCourseById(courseId);
            if (course == null) {
                throw new RuntimeException("课程不存在");
            }
            courseData.put("course", course);

            // 获取单元列表
            List<EduCourseUnit> units = eduCourseUnitMapper.selectUnitsByCourseId(courseId);
            courseData.put("units", units);

            // 获取课时列表并按单元分组
            Map<Long, List<EduCourseLesson>> lessonsByUnit = new LinkedHashMap<>();
            for (EduCourseUnit unit : units) {
                List<EduCourseLesson> lessons = eduCourseLessonMapper.selectLessonsByUnitId(unit.getUnitId());
                lessonsByUnit.put(unit.getUnitId(), lessons);
            }
            courseData.put("lessonsByUnit", lessonsByUnit);

            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(courseData);
        } catch (Exception e) {
            log.error("导出课程JSON失败", e);
            throw new RuntimeException("导出失败：" + e.getMessage());
        }
    }

    @Override
    public String exportUnitVocabularyTxt(Long unitId) {
        StringBuilder sb = new StringBuilder();
        List<EduCourseLesson> lessons = eduCourseLessonMapper.selectLessonsByUnitId(unitId);

        for (EduCourseLesson lesson : lessons) {
            if (lesson.getLessonName() != null && lesson.getContent() != null) {
                // 提取纯单词和释义（去除HTML标签）
                String word = lesson.getLessonName().trim();
                String content = lesson.getContent().trim();
                // 移除可能的HTML标签
                content = content.replaceAll("<[^>]*>", "");
                // 只取第一部分作为释义
                int firstNewline = content.indexOf('\n');
                if (firstNewline > 0) {
                    content = content.substring(0, firstNewline).trim();
                }
                sb.append(word).append("\t").append(content).append("\n");
            }
        }

        return sb.toString();
    }

    @Override
    public String exportUnitVocabularyJson(Long unitId) {
        try {
            List<EduCourseLesson> lessons = eduCourseLessonMapper.selectLessonsByUnitId(unitId);
            List<Map<String, Object>> vocabularyList = new ArrayList<>();

            for (EduCourseLesson lesson : lessons) {
                Map<String, Object> item = new HashMap<>();
                item.put("word", lesson.getLessonName());

                String content = lesson.getContent();
                if (content != null) {
                    content = content.replaceAll("<[^>]*>", "");
                    String[] parts = content.split("\n\n");

                    // 第一个部分作为释义
                    if (parts.length > 0 && !parts[0].trim().isEmpty()) {
                        List<String> definitions = new ArrayList<>();
                        String firstPart = parts[0].trim();
                        // 支持分号分隔的多条释义
                        if (firstPart.contains("；")) {
                            definitions.addAll(Arrays.asList(firstPart.split("；")));
                        } else {
                            definitions.add(firstPart);
                        }
                        item.put("definitions", definitions);
                    }

                    // 后续部分作为词组
                    if (parts.length > 1) {
                        List<Map<String, String>> phrases = new ArrayList<>();
                        for (int i = 1; i < parts.length; i++) {
                            String part = parts[i].trim();
                            if (part.startsWith("词组：")) {
                                part = part.substring(3);
                                String[] phraseParts = part.split("；");
                                for (String phraseItem : phraseParts) {
                                    String[] kv = phraseItem.split(" - ", 2);
                                    if (kv.length == 2) {
                                        Map<String, String> phrase = new HashMap<>();
                                        phrase.put("phrase", kv[0].trim());
                                        phrase.put("meaning", kv[1].trim());
                                        phrases.add(phrase);
                                    }
                                }
                            }
                        }
                        if (!phrases.isEmpty()) {
                            item.put("phrases", phrases);
                        }
                    }
                }

                vocabularyList.add(item);
            }

            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(vocabularyList);
        } catch (Exception e) {
            log.error("导出单元词汇JSON失败", e);
            throw new RuntimeException("导出失败：" + e.getMessage());
        }
    }

    /**
     * 创建词汇课时
     */
    private EduCourseLesson createVocabularyLesson(String word, String content, Long unitId, int order) {
        EduCourseLesson lesson = new EduCourseLesson();
        lesson.setUnitId(unitId);
        lesson.setLessonName(word);
        lesson.setLessonType("vocabulary");
        lesson.setContent(content);
        lesson.setLessonOrder(order);
        lesson.setDuration(5);
        lesson.setIsFree(true);
        lesson.setIsPreview(false);
        lesson.setExperienceReward(5);
        lesson.setCoinReward(2);
        lesson.setPassingScore(60);
        lesson.setMaxAttempts(3);
        return lesson;
    }

    /**
     * 检测语言
     */
    private String detectLanguage(String word, String definition) {
        if (word == null || definition == null) return "en";
        String all = word + definition;
        if (all.matches(".*[\\u4e00-\\u9fa5]+.*")) {
            return "zh";
        } else if (all.matches(".*[\\u3040-\\u309f\\u30a0-\\u30ff]+.*")) {
            return "ja";
        }
        return "en";
    }

    /**
     * 提取词性
     */
    private String extractPartOfSpeech(String definition) {
        if (definition == null) return null;
        String[] parts = definition.split("\\s+");
        if (parts.length > 0) {
            String first = parts[0].toLowerCase();
            if (first.matches("^[a-z]+\\.")) {
                return first.replace(".", "");
            }
        }
        return null;
    }
}
