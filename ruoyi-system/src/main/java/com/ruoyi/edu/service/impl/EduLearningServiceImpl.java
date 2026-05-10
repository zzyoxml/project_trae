package com.ruoyi.edu.service.impl;

import com.ruoyi.edu.domain.EduLearningProgress;
import com.ruoyi.edu.domain.EduLearningRecord;
import com.ruoyi.edu.domain.EduCourseLesson;
import com.ruoyi.edu.mapper.EduLearningProgressMapper;
import com.ruoyi.edu.mapper.EduLearningRecordMapper;
import com.ruoyi.edu.mapper.EduCourseLessonMapper;
import com.ruoyi.edu.service.IEduLearningService;
import com.ruoyi.edu.service.IEduUserService;
import com.ruoyi.edu.service.IEduAchievementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 学习服务实现
 *
 * @author LingLearn
 */
@Service
public class EduLearningServiceImpl implements IEduLearningService {

    private static final Logger log = LoggerFactory.getLogger(EduLearningServiceImpl.class);

    @Autowired
    private EduLearningProgressMapper learningProgressMapper;

    @Autowired
    private EduLearningRecordMapper learningRecordMapper;

    @Autowired
    private EduCourseLessonMapper lessonMapper;

    @Autowired
    private IEduUserService userService;

    @Autowired
    private IEduAchievementService achievementService;

    /**
     * 查询学习进度
     *
     * @param progressId 进度ID
     * @return 学习进度
     */
    @Override
    public EduLearningProgress selectEduLearningProgressById(Long progressId) {
        return learningProgressMapper.selectEduLearningProgressById(progressId);
    }

    /**
     * 查询用户特定课时的学习进度
     *
     * @param userId   用户ID
     * @param lessonId 课时ID
     * @return 学习进度
     */
    @Override
    public EduLearningProgress selectEduLearningProgressByUserAndLesson(Long userId, Long lessonId) {
        return learningProgressMapper.selectEduLearningProgressByUserAndLesson(userId, lessonId);
    }

    /**
     * 查询用户课程的学习进度列表
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 学习进度列表
     */
    @Override
    public List<EduLearningProgress> selectEduLearningProgressByCourse(Long userId, Long courseId) {
        return learningProgressMapper.selectEduLearningProgressByCourse(userId, courseId);
    }

    /**
     * 新增学习进度
     *
     * @param eduLearningProgress 学习进度
     * @return 结果
     */
    @Override
    public int insertEduLearningProgress(EduLearningProgress eduLearningProgress) {
        return learningProgressMapper.insertEduLearningProgress(eduLearningProgress);
    }

    /**
     * 修改学习进度
     *
     * @param eduLearningProgress 学习进度
     * @return 结果
     */
    @Override
    public int updateEduLearningProgress(EduLearningProgress eduLearningProgress) {
        return learningProgressMapper.updateEduLearningProgress(eduLearningProgress);
    }

    /**
     * 开始学习课时
     *
     * @param userId   用户ID
     * @param lessonId 课时ID
     * @param courseId 课程ID
     * @return 学习记录ID
     */
    @Override
    @Transactional
    public Long startLesson(Long userId, Long lessonId, Long courseId) {
        // 获取课时信息
        EduCourseLesson lesson = lessonMapper.selectEduCourseLessonById(lessonId);
        if (lesson == null) {
            throw new RuntimeException("课时不存在");
        }

        // 创建学习记录
        EduLearningRecord record = new EduLearningRecord();
        record.setUserId(userId);
        record.setLessonId(lessonId);
        record.setCourseId(courseId);
        record.setActivityType(lesson.getLessonType());
        record.setLanguage(""); // 可以从课程信息中获取
        record.setDuration(0);
        record.setScore(0);
        record.setCompleted(false);

        learningRecordMapper.insertEduLearningRecord(record);

        log.info("开始学习: userId={}, lessonId={}", userId, lessonId);
        return record.getRecordId();
    }

    /**
     * 提交学习结果
     *
     * @param recordId 记录ID
     * @param userId   用户ID
     * @param lessonId 课时ID
     * @param score    得分
     * @param duration 学习时长
     * @return 学习结果
     */
    @Override
    @Transactional
    public Map<String, Object> submitLearningResult(Long recordId, Long userId, Long lessonId,
                                                    Integer score, Integer duration) {
        Map<String, Object> result = new HashMap<>();

        // 获取课时信息
        EduCourseLesson lesson = lessonMapper.selectEduCourseLessonById(lessonId);
        if (lesson == null) {
            throw new RuntimeException("课时不存在");
        }

        // 获取学习记录
        EduLearningRecord record = learningRecordMapper.selectEduLearningRecordById(recordId);

        // 更新学习记录
        record.setScore(score);
        record.setDuration(duration);
        record.setCompleted(score >= lesson.getPassingScore());
        if (score > 0) {
            record.setAccuracy(BigDecimal.valueOf(score));
        }

        // 计算奖励
        int xpReward = lesson.getExperienceReward();
        int coinReward = lesson.getCoinReward();

        // 根据分数调整奖励
        if (score >= 90) {
            xpReward = (int) (xpReward * 1.5);
            coinReward = (int) (coinReward * 1.5);
        } else if (score >= 80) {
            xpReward = (int) (xpReward * 1.2);
            coinReward = (int) (coinReward * 1.2);
        }

        record.setXpEarned(xpReward);
        record.setCoinsEarned(coinReward);

        learningRecordMapper.updateEduLearningRecord(record);

        // 更新用户统计
        userService.updateStudyTime(userId, duration);
        userService.updateStreak(userId);
        userService.rewardUser(userId, coinReward, xpReward);

        // 更新学习进度
        EduLearningProgress progress = learningProgressMapper.selectEduLearningProgressByUserAndLesson(userId, lessonId);
        if (progress == null) {
            progress = new EduLearningProgress();
            progress.setUserId(userId);
            progress.setCourseId(record.getCourseId());
            progress.setLessonId(lessonId);
            progress.setUnitId(lesson.getUnitId());
            progress.setStatus("in_progress");
            progress.setProgressPercent(0);
            progress.setBestScore(score);
            progress.setAttemptCount(1);
            progress.setTimeSpent(duration);
            progress.setLastStudyTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            progress.setMasteryLevel(score);
            learningProgressMapper.insertEduLearningProgress(progress);
        } else {
            progress.setBestScore(Math.max(progress.getBestScore(), score));
            progress.setAttemptCount(progress.getAttemptCount() + 1);
            progress.setTimeSpent(progress.getTimeSpent() + duration);
            progress.setLastStudyTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            progress.setMasteryLevel(score);

            if (score >= lesson.getPassingScore()) {
                progress.setStatus("completed");
                progress.setCompletedTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            }

            learningProgressMapper.updateEduLearningProgress(progress);
        }

        // 检查成就
        List<Map<String, Object>> newAchievements = new ArrayList<>();
        // 调用成就服务检查新成就（这里简化处理）

        // 设置返回结果
        result.put("xpEarned", xpReward);
        result.put("coinsEarned", coinReward);
        result.put("passed", score >= lesson.getPassingScore());
        result.put("newAchievements", newAchievements);

        log.info("提交学习结果: userId={}, lessonId={}, score={}, duration={}",
                userId, lessonId, score, duration);

        return result;
    }

    /**
     * 获取用户学习进度统计
     *
     * @param userId 用户ID
     * @return 学习进度统计
     */
    @Override
    public Map<String, Object> getUserProgressStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();

        // 今日学习时长
        int todayStudyTime = learningRecordMapper.getTodayStudyTime(userId);
        stats.put("todayStudyTime", todayStudyTime);

        // 本周学习时长
        int weekStudyTime = learningRecordMapper.getWeekStudyTime(userId);
        stats.put("weekStudyTime", weekStudyTime);

        // 连续学习天数
        int streak = learningProgressMapper.getUserStreak(userId);
        stats.put("currentStreak", streak);

        // 学习统计
        Map<String, Object> studyStats = learningRecordMapper.getUserStudyStats(userId);
        if (studyStats != null) {
            stats.putAll(studyStats);
        }

        // 技能评分
        Map<String, Integer> skillScores = getUserSkillScores(userId);
        stats.put("skillScores", skillScores);

        return stats;
    }

    /**
     * 获取用户今日学习情况
     *
     * @param userId 用户ID
     * @return 学习数据
     */
    @Override
    public Map<String, Object> getTodayLearningData(Long userId) {
        Map<String, Object> data = new HashMap<>();

        // 今日学习时长
        int studyTime = learningRecordMapper.getTodayStudyTime(userId);
        data.put("studyTime", studyTime);
        data.put("studyGoal", 30); // 默认每日目标30分钟
        data.put("goalProgress", Math.min(100, (studyTime * 100) / 30));

        // 今日学习记录
        List<EduLearningRecord> todayRecords = learningRecordMapper.selectUserRecentRecords(userId, 1);
        data.put("records", todayRecords);

        // 今日获得的积分和经验
        int todayXp = 0;
        int todayCoins = 0;
        for (EduLearningRecord record : todayRecords) {
            todayXp += record.getXpEarned();
            todayCoins += record.getCoinsEarned();
        }
        data.put("todayXp", todayXp);
        data.put("todayCoins", todayCoins);

        return data;
    }

    /**
     * 获取用户技能评分
     *
     * @param userId 用户ID
     * @return 技能评分
     */
    @Override
    public Map<String, Integer> getUserSkillScores(Long userId) {
        Map<String, Integer> scores = new HashMap<>();

        // 词汇评分
        Integer vocabScore = learningRecordMapper.getUserSkillScore(userId, "vocabulary");
        scores.put("vocabulary", vocabScore != null ? vocabScore : 0);

        // 语法评分
        Integer grammarScore = learningRecordMapper.getUserSkillScore(userId, "grammar");
        scores.put("grammar", grammarScore != null ? grammarScore : 0);

        // 听力评分
        Integer listeningScore = learningRecordMapper.getUserSkillScore(userId, "listening");
        scores.put("listening", listeningScore != null ? listeningScore : 0);

        // 口语评分
        Integer speakingScore = learningRecordMapper.getUserSkillScore(userId, "speaking");
        scores.put("speaking", speakingScore != null ? speakingScore : 0);

        return scores;
    }

    /**
     * 获取学习记录
     *
     * @param recordId 记录ID
     * @return 学习记录
     */
    @Override
    public EduLearningRecord selectEduLearningRecordById(Long recordId) {
        return learningRecordMapper.selectEduLearningRecordById(recordId);
    }

    /**
     * 查询用户的学习记录列表
     *
     * @param eduLearningRecord 学习记录
     * @return 学习记录列表
     */
    @Override
    public List<EduLearningRecord> selectEduLearningRecordList(EduLearningRecord eduLearningRecord) {
        return learningRecordMapper.selectEduLearningRecordList(eduLearningRecord);
    }

    /**
     * 获取用户最近学习记录
     *
     * @param userId 用户ID
     * @param days   查询天数
     * @return 学习记录列表
     */
    @Override
    public List<EduLearningRecord> selectUserRecentRecords(Long userId, Integer days) {
        if (days == null || days <= 0) {
            days = 7;
        }
        return learningRecordMapper.selectUserRecentRecords(userId, days);
    }
}
