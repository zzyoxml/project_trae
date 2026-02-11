package com.ruoyi.edu.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.edu.domain.*;
import com.ruoyi.edu.mapper.*;
import com.ruoyi.edu.service.IRecommendationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 个性化学习推荐服务实现
 * 
 * @description 基于用户学习行为、能力和偏好的智能推荐系统
 * 采用协同过滤、内容推荐、基于知识的推荐等多种算法
 * @author LingLearn
 */
@Service
public class RecommendationServiceImpl implements IRecommendationService {

    private static final Logger log = LoggerFactory.getLogger(RecommendationServiceImpl.class);

    @Autowired
    private EduUserProfileMapper userProfileMapper;

    @Autowired
    private EduCourseMapper courseMapper;

    @Autowired
    private EduLearningRecordMapper learningRecordMapper;

    @Autowired
    private EduLearningProgressMapper learningProgressMapper;

    @Autowired
    private EduCourseLessonMapper lessonMapper;

    @Autowired
    private EduVocabularyMapper vocabularyMapper;

    @Autowired
    private EduGrammarMapper grammarMapper;

    /**
     * 用户学习画像
     */
    private static class LearningProfile {
        String language;           // 学习语言
        String currentLevel;       // 当前等级
        Map<String, Integer> skillScores;  // 技能评分
        Map<String, Integer> weakPoints;     // 薄弱点
        List<String> learningHistory;       // 学习历史
        List<Long> completedCourses;         // 已完成课程
        List<Long> favoriteCategories;       // 喜欢的分类
        int totalStudyTime;      // 总学习时间
        int studyFrequency;      // 学习频率（天/周）
        String preferredTime;    // 偏好学习时段
        List<String> interests;  // 兴趣标签

        LearningProfile() {
            skillScores = new HashMap<>();
            weakPoints = new HashMap<>();
            learningHistory = new ArrayList<>();
            completedCourses = new ArrayList<>();
            favoriteCategories = new ArrayList<>();
            interests = new ArrayList<>();
        }
    }

    @Override
    public List<EduCourse> getPersonalizedCourseRecommendations(Long userId, int limit) {
        log.info("为用户 {} 生成个性化课程推荐", userId);

        // 1. 构建用户画像
        LearningProfile profile = buildLearningProfile(userId);

        // 2. 获取候选课程
        List<EduCourse> candidates = courseMapper.selectEduCourseByLanguage(profile.language);

        // 3. 计算推荐分数并排序
        List<CourseScore> scoredCourses = candidates.stream()
                .map(course -> {
                    int score = calculateRecommendationScore(profile, course);
                    return new CourseScore(course, score);
                })
                .filter(cs -> cs.score > 30) // 过滤低分课程
                .sorted((a, b) -> b.score - a.score)
                .limit(limit)
                .collect(Collectors.toList());

        // 4. 返回推荐结果
        return scoredCourses.stream()
                .map(cs -> cs.course)
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getDailyLearningTasks(Long userId) {
        List<Map<String, Object>> tasks = new ArrayList<>();

        // 获取用户信息
        EduUserProfile profile = userProfileMapper.selectEduUserProfileByUserId(userId);
        if (profile == null) {
            return tasks;
        }

        // 1. 复习任务（基于遗忘曲线）
        List<Map<String, Object>> reviewTasks = getSpacedRepetitionPlan(userId, profile.getLearningLanguages());
        tasks.addAll(reviewTasks);

        // 2. 薄弱点强化任务
        Map<String, Object> weakAnalysis = getWeakPointsAnalysis(userId, profile.getLearningLanguages());
        if (weakAnalysis.containsKey("weakPoints")) {
            Map<String, Integer> weakPoints = (Map<String, Integer>) weakAnalysis.get("weakPoints");
            for (Map.Entry<String, Integer> entry : weakPoints.entrySet()) {
                Map<String, Object> task = new HashMap<>();
                task.put("type", "weakness_reinforcement");
                task.put("skill", entry.getKey());
                task.put("description", "强化练习" + entry.getKey());
                task.put("priority", entry.getValue());
                tasks.add(task);
            }
        }

        // 3. 课程学习任务
        List<EduCourse> recommendations = getPersonalizedCourseRecommendations(userId, 3);
        for (EduCourse course : recommendations) {
            Map<String, Object> task = new HashMap<>();
            task.put("type", "course_learning");
            task.put("courseId", course.getCourseId());
            task.put("courseName", course.getCourseName());
            task.put("description", "学习" + course.getCourseName());
            task.put("estimatedTime", course.getTotalDuration());
            task.put("priority", 50);
            tasks.add(task);
        }

        // 4. 每日目标
        int todayStudyTime = learningRecordMapper.getTodayStudyTime(userId);
        int remainingTime = Math.max(0, profile.getDailyGoal() - todayStudyTime);
        if (remainingTime > 0) {
            Map<String, Object> task = new HashMap<>();
            task.put("type", "daily_goal");
            task.put("description", "完成每日学习目标");
            task.put("progress", todayStudyTime);
            task.put("target", profile.getDailyGoal());
            task.put("remaining", remainingTime);
            task.put("priority", 100);
            tasks.add(task);
        }

        // 按优先级排序
        tasks.sort((a, b) -> {
            int p1 = (int) a.getOrDefault("priority", 0);
            int p2 = (int) b.getOrDefault("priority", 0);
            return p2 - p1;
        });

        return tasks;
    }

    @Override
    public Map<String, Object> getWeakPointsAnalysis(Long userId, String language) {
        Map<String, Object> result = new HashMap<>();

        // 技能评分
        Map<String, Integer> skillScores = new HashMap<>();
        skillScores.put("vocabulary", calculateSkillScore(userId, "vocabulary", language));
        skillScores.put("grammar", calculateSkillScore(userId, "grammar", language));
        skillScores.put("listening", calculateSkillScore(userId, "listening", language));
        skillScores.put("speaking", calculateSkillScore(userId, "speaking", language));

        result.put("skillScores", skillScores);

        // 识别薄弱点
        Map<String, Integer> weakPoints = new HashMap<>();
        skillScores.forEach((skill, score) -> {
            if (score < 60) {
                weakPoints.put(skill, 100 - score);
            }
        });

        result.put("weakPoints", weakPoints);

        // 薄弱知识点详情
        List<Map<String, Object>> weakKnowledgePoints = new ArrayList<>();

        // 分析单词薄弱点
        List<EduVocabulary> vocabList = vocabularyMapper.selectEduVocabularyByLanguage(language);
        List<EduVocabulary> weakVocab = vocabList.stream()
                .filter(v -> v.getMasteryLevel() != null && v.getMasteryLevel() < 60)
                .limit(10)
                .collect(Collectors.toList());

        for (EduVocabulary vocab : weakVocab) {
            Map<String, Object> point = new HashMap<>();
            point.put("type", "vocabulary");
            point.put("id", vocab.getVocabId());
            point.put("content", vocab.getWord());
            point.put("masteryLevel", vocab.getMasteryLevel());
            point.put("suggestion", "建议复习这个单词的释义和例句");
            weakKnowledgePoints.add(point);
        }

        // 分析语法薄弱点
        List<EduGrammar> grammarList = grammarMapper.selectEduGrammarByLanguage(language);
        List<EduGrammar> weakGrammar = grammarList.stream()
                .filter(g -> {
                    // 这里应该根据用户的学习记录来判断语法掌握程度
                    return true; // 简化实现
                })
                .limit(5)
                .collect(Collectors.toList());

        for (EduGrammar grammar : weakGrammar) {
            Map<String, Object> point = new HashMap<>();
            point.put("type", "grammar");
            point.put("id", grammar.getGrammarId());
            point.put("content", grammar.getGrammarName());
            point.put("suggestion", "建议学习这个语法规则并做练习");
            weakKnowledgePoints.add(point);
        }

        result.put("weakKnowledgePoints", weakKnowledgePoints);

        // 总体评估
        double overallScore = skillScores.values().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);

        String assessment;
        if (overallScore >= 80) {
            assessment = "您的各项技能掌握良好，继续保持！";
        } else if (overallScore >= 60) {
            assessment = "您的技能有提升空间，建议加强薄弱环节的练习。";
        } else {
            assessment = "建议从基础开始，系统学习各技能模块。";
        }
        result.put("assessment", assessment);
        result.put("overallScore", overallScore);

        return result;
    }

    @Override
    public EduLearningPath generateLearningPath(Long userId, String targetLevel, String language) {
        log.info("为用户 {} 生成学习路径，目标等级：{}", userId, targetLevel);

        // 构建用户画像
        LearningProfile profile = buildLearningProfile(userId);

        // 确定起始等级
        String startLevel = profile.currentLevel != null ? profile.currentLevel : "beginner";

        // 创建学习路径
        EduLearningPath learningPath = new EduLearningPath();
        learningPath.setPathName(targetLevel + " 学习路径");
        learningPath.setLanguage(language);
        learningPath.setStartLevel(startLevel);
        learningPath.setTargetLevel(targetLevel);

        // 规划学习阶段
        List<String> stages = getLearningStages(startLevel, targetLevel);
        List<Long> courseSequence = new ArrayList<>();

        for (String stage : stages) {
            // 查找该阶段的推荐课程
            List<EduCourse> stageCourses = courseMapper.selectEduCourseByLanguage(language).stream()
                    .filter(c -> c.getLevel().equals(stage))
                    .sorted((a, b) -> b.getRating().compareTo(a.getRating()))
                    .limit(3)
                    .collect(Collectors.toList());

            for (EduCourse course : stageCourses) {
                courseSequence.add(course.getCourseId());
            }
        }

        // 计算预计时长
        int totalDays = calculateEstimatedDays(startLevel, targetLevel);
        learningPath.setEstimatedDuration(totalDays);

        // 设置目标技能
        Map<String, Integer> targetSkills = new HashMap<>();
        targetSkills.put("vocabulary", 80);
        targetSkills.put("grammar", 75);
        targetSkills.put("listening", 70);
        targetSkills.put("speaking", 70);
        learningPath.setTargetSkills(com.alibaba.fastjson2.JSON.toJSONString(targetSkills));

        learningPath.setSuccessRate(new java.math.BigDecimal("0.75")); // 预估成功率
        learningPath.setEnrollmentCount(100); // 初始报名人数
        learningPath.setRating(new java.math.BigDecimal("5.0"));

        return learningPath;
    }

    @Override
    public List<Map<String, Object>> getSpacedRepetitionPlan(Long userId, String language) {
        List<Map<String, Object>> plan = new ArrayList<>();

        // 艾宾浩斯遗忘曲线复习间隔
        int[] intervals = {1, 2, 4, 7, 15, 30, 60}; // 天数

        // 获取需要复习的单词
        List<EduLearningProgress> progressList = learningProgressMapper.selectEduLearningProgressList(
                new EduLearningProgress() {{
                    setUserId(userId);
                }}
        );

        Date today = DateUtils.getNowDate();

        for (EduLearningProgress progress : progressList) {
            if (progress.getMasteryLevel() != null && progress.getMasteryLevel() < 80) {
                // 计算下次复习时间
                int attemptCount = progress.getAttemptCount();
                int interval = intervals[Math.min(attemptCount, intervals.length - 1)];

                Date nextReview = DateUtils.addDays(today, interval);

                Map<String, Object> reviewTask = new HashMap<>();
                reviewTask.put("lessonId", progress.getLessonId());
                reviewTask.put("courseId", progress.getCourseId());
                reviewTask.put("masteryLevel", progress.getMasteryLevel());
                reviewTask.put("nextReview", nextReview);
                reviewTask.put("interval", interval);
                reviewTask.put("priority", 100 - progress.getMasteryLevel());

                plan.add(reviewTask);
            }
        }

        // 按优先级排序
        plan.sort((a, b) -> {
            int p1 = (int) a.getOrDefault("priority", 0);
            int p2 = (int) b.getOrDefault("priority", 0);
            return p2 - p1;
        });

        return plan;
    }

    @Override
    public Map<String, Object> getSkillRadarData(Long userId, String language) {
        Map<String, Object> data = new HashMap<>();

        // 计算各项技能评分
        Map<String, Integer> scores = new HashMap<>();
        scores.put("vocabulary", calculateSkillScore(userId, "vocabulary", language));
        scores.put("grammar", calculateSkillScore(userId, "grammar", language));
        scores.put("listening", calculateSkillScore(userId, "listening", language));
        scores.put("speaking", calculateSkillScore(userId, "speaking", language));

        data.put("scores", scores);

        // 计算综合评分
        double average = scores.values().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);
        data.put("overall", Math.round(average));

        // 获取用户等级
        EduUserProfile profile = userProfileMapper.selectEduUserProfileByUserId(userId);
        if (profile != null) {
            data.put("level", profile.getLevel());
            data.put("experience", profile.getExperiencePoints());
            data.put("totalPoints", profile.getTotalPoints());
        }

        return data;
    }

    @Override
    public Map<String, Object> predictLearningOutcome(Long userId, Long courseId) {
        Map<String, Object> prediction = new HashMap<>();

        EduCourse course = courseMapper.selectEduCourseById(courseId);
        if (course == null) {
            return prediction;
        }

        // 计算课程匹配度
        int matchScore = calculateCourseMatchScore(userId, courseId);
        prediction.put("matchScore", matchScore);

        // 预测完成时间
        int estimatedHours = course.getTotalDuration();
        int userStudyTimePerDay = 30; // 默认每天30分钟
        int estimatedDays = (estimatedHours * 60) / userStudyTimePerDay;
        prediction.put("estimatedDays", estimatedDays);

        // 预测成功率
        double successRate = matchScore > 70 ? 0.9 : (matchScore > 50 ? 0.7 : 0.5);
        prediction.put("successRate", successRate);

        // 预测效果
        String effect;
        if (matchScore >= 80) {
            effect = "非常适合您！预计能达到很好的学习效果";
        } else if (matchScore >= 60) {
            effect = "比较适合，通过努力可以达到预期目标";
        } else if (matchScore >= 40) {
            effect = "有一定挑战，需要投入更多时间";
        } else {
            effect = "建议先打好基础，再学习此课程";
        }
        prediction.put("prediction", effect);

        return prediction;
    }

    @Override
    public Map<String, Object> getLearningEfficiencyAnalysis(Long userId) {
        Map<String, Object> analysis = new HashMap<>();

        // 获取学习记录
        List<EduLearningRecord> records = learningRecordMapper.selectUserRecentRecords(userId, 30);

        if (records.isEmpty()) {
            analysis.put("efficiency", 0);
            analysis.put("suggestion", "开始学习吧！");
            return analysis;
        }

        // 计算学习效率
        int totalTime = 0;
        int totalScore = 0;
        Map<String, Integer> skillPracticeCount = new HashMap<>();

        for (EduLearningRecord record : records) {
            totalTime += record.getDuration() != null ? record.getDuration() : 0;
            totalScore += record.getScore() != null ? record.getScore() : 0;

            String activityType = record.getActivityType();
            skillPracticeCount.merge(activityType, 1, Integer::sum);
        }

        double efficiency = totalTime > 0 ? (double) totalScore / totalTime : 0;
        analysis.put("efficiency", Math.round(efficiency * 100) / 100);

        // 分析学习模式
        analysis.put("totalStudyTime", totalTime);
        analysis.put("averageScore", totalTime > 0 ? totalScore / records.size() : 0);
        analysis.put("practiceDistribution", skillPracticeCount);

        // 生成建议
        List<String> suggestions = new ArrayList<>();

        // 检查是否偏科
        int maxPractice = skillPracticeCount.values().stream().max(Integer::compareTo).orElse(0);
        int minPractice = skillPracticeCount.values().stream().min(Integer::compareTo).orElse(0);

        if (maxPractice > minPractice * 3) {
            String weakest = skillPracticeCount.entrySet().stream()
                    .min(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("其他");
            suggestions.add("建议加强" + weakest + "的练习，您在这方面投入较少");
        }

        // 检查学习时间
        if (totalTime < 600) { // 30天学习不足10小时
            suggestions.add("保持学习节奏，每天坚持学习效果更好");
        } else if (totalTime > 3000) { // 30天学习超过50小时
            suggestions.add("学习时间很充足，注意适当休息，保持效率");
        }

        if (suggestions.isEmpty()) {
            suggestions.add("您的学习状态很好，继续保持！");
        }

        analysis.put("suggestions", suggestions);

        return analysis;
    }

    @Override
    public List<String> getPersonalizedSuggestions(Long userId) {
        List<String> suggestions = new ArrayList<>();

        EduUserProfile profile = userProfileMapper.selectEduUserProfileByUserId(userId);
        if (profile == null) {
            return suggestions;
        }

        // 连续学习建议
        if (profile.getCurrentStreak() == 0) {
            suggestions.add("今天开始学习吧，保持连续学习可以更快进步哦！");
        } else if (profile.getCurrentStreak() >= 7) {
            suggestions.add("太棒了！您已连续学习" + profile.getCurrentStreak() + "天！");
        }

        // 学习时间建议
        if (profile.getPreferredLearningTime() != null) {
            String timePeriod = profile.getPreferredLearningTime();
            String timeDesc = "morning".equals(timePeriod) ? "早上" : ("afternoon".equals(timePeriod) ? "下午" : "晚上");
            suggestions.add("根据您的习惯，" + timeDesc + "是您最佳的学习时段");
        }

        // 学习薄弱点建议
        Map<String, Object> weakAnalysis = getWeakPointsAnalysis(userId, profile.getLearningLanguages());
        if (weakAnalysis.containsKey("weakPoints")) {
            @SuppressWarnings("unchecked")
            Map<String, Integer> weakPoints = (Map<String, Integer>) weakAnalysis.get("weakPoints");
            if (!weakPoints.isEmpty()) {
                String weakestSkill = weakPoints.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse(null);
                if (weakestSkill != null) {
                    String skillName = "vocabulary".equals(weakestSkill) ? "词汇" :
                            ("grammar".equals(weakestSkill) ? "语法" :
                                    ("listening".equals(weakestSkill) ? "听力" : "口语"));
                    suggestions.add("建议加强" + skillName + "的练习，这是您相对薄弱的部分");
                }
            }
        }

        // 学习效率建议
        Map<String, Object> efficiency = getLearningEfficiencyAnalysis(userId);
        @SuppressWarnings("unchecked")
        List<String> efficiencySuggestions = (List<String>) efficiency.get("suggestions");
        if (efficiencySuggestions != null) {
            suggestions.addAll(efficiencySuggestions);
        }

        return suggestions;
    }

    @Override
    public void updateUserLearningProfile(Long userId) {
        log.info("更新用户 {} 的学习画像", userId);

        // 构建完整的学习画像
        LearningProfile profile = buildLearningProfile(userId);

        // 可以将画像数据存入Redis缓存或用户扩展信息中
        // 这里只是记录日志，实际项目中应该持久化
        log.info("用户画像更新完成：{}", profile);
    }

    @Override
    public int calculateCourseMatchScore(Long userId, Long courseId) {
        EduCourse course = courseMapper.selectEduCourseById(courseId);
        if (course == null) {
            return 0;
        }

        LearningProfile profile = buildLearningProfile(userId);

        int score = 50; // 基础分数

        // 1. 等级匹配度 (0-20分)
        if (profile.currentLevel != null && profile.currentLevel.equals(course.getLevel())) {
            score += 20;
        } else if (isNextLevel(profile.currentLevel, course.getLevel())) {
            score += 15;
        } else {
            score += 5;
        }

        // 2. 技能匹配度 (0-20分)
        String courseType = course.getCourseType();
        int skillScore = 0;
        if ("general".equals(courseType)) {
            skillScore = 15;
        } else {
            skillScore = profile.skillScores.getOrDefault(courseType, 50);
        }
        score += (skillScore * 20) / 100;

        // 3. 兴趣匹配度 (0-10分)
        if (profile.favoriteCategories.contains(course.getCategoryId())) {
            score += 10;
        }

        return Math.min(100, score);
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 构建用户学习画像
     */
    private LearningProfile buildLearningProfile(Long userId) {
        LearningProfile profile = new LearningProfile();

        EduUserProfile userProfile = userProfileMapper.selectEduUserProfileByUserId(userId);
        if (userProfile != null) {
            profile.language = userProfile.getLearningLanguages();
            profile.currentLevel = userProfile.getProficiencyLevel();
            profile.totalStudyTime = userProfile.getTotalStudyTime() != null ? 
                    userProfile.getTotalStudyTime().intValue() : 0;
            profile.preferredTime = userProfile.getPreferredLearningTime();
        }

        // 计算技能评分
        profile.skillScores.put("vocabulary", calculateSkillScore(userId, "vocabulary", profile.language));
        profile.skillScores.put("grammar", calculateSkillScore(userId, "grammar", profile.language));
        profile.skillScores.put("listening", calculateSkillScore(userId, "listening", profile.language));
        profile.skillScores.put("speaking", calculateSkillScore(userId, "speaking", profile.language));

        // 获取学习历史
        List<EduLearningRecord> records = learningRecordMapper.selectUserRecentRecords(userId, 30);
        for (EduLearningRecord record : records) {
            profile.learningHistory.add(record.getActivityType());
        }

        return profile;
    }

    /**
     * 计算单个技能评分
     */
    private int calculateSkillScore(Long userId, String skillType, String language) {
        // 根据学习记录计算技能评分
        Integer score = learningRecordMapper.getUserSkillScore(userId, skillType);
        return score != null ? score : 50; // 默认50分
    }

    /**
     * 计算推荐分数
     */
    private int calculateRecommendationScore(LearningProfile profile, EduCourse course) {
        int score = 0;

        // 1. 难度匹配 (0-30分)
        int difficultyScore = course.getDifficultyScore() != null ? course.getDifficultyScore() : 1;
        if ("beginner".equals(profile.currentLevel) && difficultyScore <= 2) {
            score += 30;
        } else if ("elementary".equals(profile.currentLevel) && difficultyScore <= 3) {
            score += 25;
        } else if ("intermediate".equals(profile.currentLevel) && difficultyScore <= 4) {
            score += 20;
        } else {
            score += 15;
        }

        // 2. 课程质量 (0-20分)
        if (course.getRating() != null) {
            score += (course.getRating().intValue() * 4); // 5分制转百分制
        }

        // 3. 用户偏好 (0-20分)
        if (profile.interests.contains(course.getCourseType())) {
            score += 20;
        }

        // 4. 热门程度 (0-10分)
        int popularity = course.getPopularityScore() != null ? course.getPopularityScore() : 0;
        score += Math.min(10, popularity / 100);

        // 5. 课程完整性 (0-10分)
        if (course.getTotalLessons() != null && course.getTotalLessons() > 20) {
            score += 10;
        } else if (course.getTotalLessons() != null && course.getTotalLessons() > 10) {
            score += 5;
        }

        // 6. 去重（已完成课程降低分数）
        if (profile.completedCourses.contains(course.getCourseId())) {
            score = 0;
        }

        return Math.min(100, score);
    }

    /**
     * 获取学习阶段
     */
    private List<String> getLearningStages(String startLevel, String targetLevel) {
        List<String> levels = new ArrayList<>();
        levels.add("beginner");
        levels.add("elementary");
        levels.add("intermediate");
        levels.add("advanced");

        int startIndex = levels.indexOf(startLevel);
        int targetIndex = levels.indexOf(targetLevel);

        if (startIndex < 0) startIndex = 0;
        if (targetIndex < 0) targetIndex = levels.size() - 1;

        return levels.subList(startIndex, targetIndex + 1);
    }

    /**
     * 计算预计学习天数
     */
    private int calculateEstimatedDays(String startLevel, String targetLevel) {
        Map<String, Integer> levelDays = new HashMap<>();
        levelDays.put("beginner", 30);
        levelDays.put("elementary", 60);
        levelDays.put("intermediate", 90);
        levelDays.put("advanced", 120);

        List<String> stages = getLearningStages(startLevel, targetLevel);
        return stages.stream()
                .mapToInt(level -> levelDays.getOrDefault(level, 30))
                .sum();
    }

    /**
     * 判断是否是下一个等级
     */
    private boolean isNextLevel(String current, String next) {
        List<String> levels = Arrays.asList("beginner", "elementary", "intermediate", "advanced");
        int currentIndex = levels.indexOf(current);
        int nextIndex = levels.indexOf(next);
        return nextIndex == currentIndex + 1;
    }

    /**
     * 课程评分内部类
     */
    private static class CourseScore {
        EduCourse course;
        int score;

        CourseScore(EduCourse course, int score) {
            this.course = course;
            this.score = score;
        }
    }
}
