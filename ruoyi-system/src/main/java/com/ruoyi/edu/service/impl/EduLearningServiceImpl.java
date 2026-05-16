package com.ruoyi.edu.service.impl;

import com.ruoyi.edu.domain.EduLearningProgress;
import com.ruoyi.edu.domain.EduLearningRecord;
import com.ruoyi.edu.domain.EduCourseLesson;
import com.ruoyi.edu.domain.EduCourseUnit;
import com.ruoyi.edu.domain.EduUserProfile;
import com.ruoyi.edu.domain.EduCourse;
import com.ruoyi.edu.mapper.EduLearningProgressMapper;
import com.ruoyi.edu.mapper.EduLearningRecordMapper;
import com.ruoyi.edu.mapper.EduCourseLessonMapper;
import com.ruoyi.edu.mapper.EduCourseUnitMapper;
import com.ruoyi.edu.mapper.EduUserProfileMapper;
import com.ruoyi.edu.mapper.EduCourseMapper;
import com.ruoyi.edu.service.IEduLearningService;
import com.ruoyi.edu.service.IEduUserService;
import com.ruoyi.edu.service.IEduAchievementService;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
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
    private EduCourseUnitMapper unitMapper;

    @Autowired
    private IEduUserService userService;

    @Autowired
    private IEduAchievementService achievementService;

    @Autowired
    private EduUserProfileMapper userProfileMapper;

    @Autowired
    private EduCourseMapper courseMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

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
        record.setActivityType(lesson.getLessonType() != null ? lesson.getLessonType() : "lesson");
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

    @Override
    @Transactional
    public Map<String, Object> completeLesson(Long userId, Long lessonId, Long courseId, Integer score, Integer duration) {
        Map<String, Object> result = new HashMap<>();

        EduCourseLesson lesson = lessonMapper.selectEduCourseLessonById(lessonId);
        if (lesson == null) {
            throw new RuntimeException("课时不存在");
        }

        if (courseId == null && lesson.getUnitId() != null) {
            EduCourseUnit unit = unitMapper.selectEduCourseUnitById(lesson.getUnitId());
            if (unit != null) {
                courseId = unit.getCourseId();
            }
        }

        EduLearningProgress progress = learningProgressMapper.selectEduLearningProgressByUserAndLesson(userId, lessonId);

        boolean isFirstCompletion = false;
        int pointsAdded = 0;

        if (progress == null) {
            isFirstCompletion = true;
            progress = new EduLearningProgress();
            progress.setUserId(userId);
            progress.setCourseId(courseId);
            progress.setLessonId(lessonId);
            progress.setUnitId(lesson.getUnitId());
            progress.setProgressPercent(100);
            progress.setBestScore(score);
            progress.setAttemptCount(1);
            progress.setTimeSpent(duration != null ? duration : 0);
            progress.setStatus("completed");
            progress.setCompletedTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            progress.setMasteryLevel(score);
            learningProgressMapper.insertEduLearningProgress(progress);
        } else {
            if ("completed".equals(progress.getStatus())) {
                throw new RuntimeException("该课时已完成，不可重复获取积分");
            }
            progress.setProgressPercent(100);
            progress.setBestScore(Math.max(progress.getBestScore() != null ? progress.getBestScore() : 0, score));
            progress.setAttemptCount((progress.getAttemptCount() != null ? progress.getAttemptCount() : 0) + 1);
            progress.setTimeSpent((progress.getTimeSpent() != null ? progress.getTimeSpent() : 0) + (duration != null ? duration : 0));
            progress.setStatus("completed");
            progress.setCompletedTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            progress.setMasteryLevel(score);
            learningProgressMapper.updateEduLearningProgress(progress);
        }

        userService.updateStudyTime(userId, duration != null ? duration : 0);
        userService.updateStreak(userId);

        if (isFirstCompletion) {
            pointsAdded = 5;
            userService.addUserPoints(userId, pointsAdded);
            log.info("用户完成课时获得积分: userId={}, lessonId={}, points={}", userId, lessonId, pointsAdded);
        }

        EduLearningRecord record = new EduLearningRecord();
        record.setUserId(userId);
        record.setLessonId(lessonId);
        record.setCourseId(courseId);
        record.setActivityType(lesson.getLessonType() != null ? lesson.getLessonType() : "lesson");
        record.setLanguage("");
        record.setDuration(duration != null ? duration : 0);
        record.setScore(score != null ? score : 0);
        record.setCompleted(true);
        record.setXpEarned(pointsAdded);
        record.setCoinsEarned(0);
        learningRecordMapper.insertEduLearningRecord(record);

        result.put("pointsAdded", pointsAdded);
        result.put("passed", true);
        result.put("message", isFirstCompletion ? "课时完成，获得积分" : "课时完成");

        log.info("完成课时: userId={}, lessonId={}, score={}, firstCompletion={}", userId, lessonId, score, isFirstCompletion);
        return result;
    }

    @Override
    @Transactional
    public void claimTaskReward(Long userId, String taskName, Integer reward) {
        userService.addUserPoints(userId, reward);
        log.info("领取任务奖励: userId={}, taskName={}, reward={}", userId, taskName, reward);
    }

    @Override
    public Map<String, Object> getLearningStats() {
        Map<String, Object> stats = new HashMap<>();

        // 获取所有用户学习进度
        List<EduLearningProgress> allProgress = learningProgressMapper.selectEduLearningProgressList(new EduLearningProgress());
        
        // 计算总学习时长（转换为小时）
        int totalMinutes = 0;
        int activeUsers = 0;
        int totalEnrollments = 0;
        int totalCompleted = 0;
        
        Map<Long, Integer> userMinutesMap = new HashMap<>();
        
        for (EduLearningProgress progress : allProgress) {
            if (progress.getTimeSpent() != null) {
                totalMinutes += progress.getTimeSpent();
                userMinutesMap.merge(progress.getUserId(), progress.getTimeSpent(), Integer::sum);
            }
            if ("completed".equals(progress.getStatus())) {
                totalCompleted++;
            }
        }
        
        // 活跃用户数（学习时长>0的用户）
        activeUsers = (int) userMinutesMap.entrySet().stream().filter(e -> e.getValue() > 0).count();
        
        // 统计报名人次（每个用户每门课程算一次）
        Set<String> enrollments = new HashSet<>();
        for (EduLearningProgress progress : allProgress) {
            if (progress.getUserId() != null && progress.getCourseId() != null) {
                enrollments.add(progress.getUserId() + "_" + progress.getCourseId());
            }
        }
        totalEnrollments = enrollments.size();
        
        // 平均进度
        int avgProgress = 0;
        if (!allProgress.isEmpty()) {
            int totalProgress = allProgress.stream()
                    .mapToInt(p -> p.getProgressPercent() != null ? p.getProgressPercent() : 0)
                    .sum();
            avgProgress = totalProgress / allProgress.size();
        }
        
        stats.put("totalLearningTime", totalMinutes / 60); // 转换为小时
        stats.put("activeUsers", activeUsers);
        stats.put("totalEnrollments", totalEnrollments);
        stats.put("avgProgress", avgProgress);
        
        return stats;
    }

    @Override
    public Map<String, Object> getUserLearningList(String userName, String language, Integer pageNum, Integer pageSize) {
        Map<String, Object> result = new HashMap<>();
        
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        
        // 获取所有用户
        List<SysUser> sysUsers = sysUserMapper.selectUserList(new SysUser());
        
        // 过滤用户名
        if (userName != null && !userName.trim().isEmpty()) {
            sysUsers = sysUsers.stream()
                    .filter(u -> u.getUserName().toLowerCase().contains(userName.toLowerCase()) ||
                                (u.getNickName() != null && u.getNickName().toLowerCase().contains(userName.toLowerCase())))
                    .toList();
        }
        
        // 获取用户学习数据
        List<Map<String, Object>> userList = new ArrayList<>();
        
        for (SysUser sysUser : sysUsers) {
            Long userId = sysUser.getUserId();
            EduUserProfile profile = userProfileMapper.selectEduUserProfileByUserId(userId);
            
            // 获取用户学习进度列表
            List<EduLearningProgress> progresses = learningProgressMapper.selectEduLearningProgressByUser(userId);
            
            // 按语言过滤
            if (language != null && !language.trim().isEmpty() && !"all".equals(language)) {
                progresses = progresses.stream()
                        .filter(p -> {
                            if (p.getCourseId() != null) {
                                EduCourse course = courseMapper.selectEduCourseById(p.getCourseId());
                                return course != null && language.equals(course.getLanguage());
                            }
                            return false;
                        })
                        .toList();
                
                if (progresses.isEmpty()) {
                    continue;
                }
            }
            
            // 计算统计数据
            int learningTime = progresses.stream()
                    .mapToInt(p -> p.getTimeSpent() != null ? p.getTimeSpent() : 0)
                    .sum() / 60; // 转换为小时
            
            int completedCourses = (int) progresses.stream()
                    .filter(p -> "completed".equals(p.getStatus()))
                    .map(EduLearningProgress::getCourseId)
                    .distinct()
                    .count();
            
            int totalProgress = progresses.isEmpty() ? 0 : 
                    progresses.stream()
                            .mapToInt(p -> p.getProgressPercent() != null ? p.getProgressPercent() : 0)
                            .sum() / progresses.size();
            
            // 获取最近学习时间
            String lastLearningTime = progresses.stream()
                    .filter(p -> p.getLastStudyTime() != null)
                    .map(EduLearningProgress::getLastStudyTime)
                    .max(String::compareTo)
                    .orElse(null);
            
            Map<String, Object> userData = new HashMap<>();
            userData.put("userId", userId);
            userData.put("nickName", sysUser.getNickName() != null ? sysUser.getNickName() : sysUser.getUserName());
            userData.put("language", language != null && !"all".equals(language) ? language : "");
            userData.put("learningTime", learningTime);
            userData.put("progress", totalProgress);
            userData.put("completedCourses", completedCourses);
            userData.put("points", profile != null && profile.getTotalPoints() != null ? profile.getTotalPoints() : 0);
            userData.put("achievementCount", 0); // 需要从成就表查询
            userData.put("lastLearningTime", lastLearningTime);
            
            userList.add(userData);
        }
        
        // 按学习时长排序
        userList.sort((a, b) -> {
            int aTime = (Integer) a.get("learningTime");
            int bTime = (Integer) b.get("learningTime");
            return bTime - aTime;
        });
        
        // 分页
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, userList.size());
        List<Map<String, Object>> pageUsers = start < userList.size() ? userList.subList(start, end) : new ArrayList<>();
        
        result.put("rows", pageUsers);
        result.put("total", userList.size());
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        
        return result;
    }

    @Override
    public Map<String, Object> getUserLearningDetail(Long userId) {
        Map<String, Object> detail = new HashMap<>();
        
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        EduUserProfile profile = userProfileMapper.selectEduUserProfileByUserId(userId);
        
        if (sysUser != null) {
            detail.put("userId", userId);
            detail.put("nickName", sysUser.getNickName() != null ? sysUser.getNickName() : sysUser.getUserName());
        }
        
        if (profile != null) {
            detail.put("learningTime", profile.getTotalStudyTime() != null ? profile.getTotalStudyTime() / 60 : 0);
            detail.put("points", profile.getTotalPoints() != null ? profile.getTotalPoints() : 0);
            detail.put("achievementCount", 0); // 需要从成就表查询
        }
        
        // 获取学习历史
        List<EduLearningProgress> progresses = learningProgressMapper.selectEduLearningProgressByUser(userId);
        
        List<Map<String, Object>> learningHistory = new ArrayList<>();
        Map<Long, Map<String, Object>> courseProgressMap = new HashMap<>();
        
        for (EduLearningProgress progress : progresses) {
            Long courseId = progress.getCourseId();
            if (courseId == null) continue;
            
            courseProgressMap.computeIfAbsent(courseId, k -> {
                Map<String, Object> courseData = new HashMap<>();
                EduCourse course = courseMapper.selectEduCourseById(courseId);
                courseData.put("courseName", course != null ? course.getCourseName() : "未知课程");
                courseData.put("progress", 0);
                courseData.put("duration", 0);
                courseData.put("startTime", null);
                return courseData;
            });
            
            Map<String, Object> courseData = courseProgressMap.get(courseId);
            courseData.put("progress", Math.max((Integer) courseData.get("progress"), 
                    progress.getProgressPercent() != null ? progress.getProgressPercent() : 0));
            courseData.put("duration", (Integer) courseData.get("duration") + 
                    (progress.getTimeSpent() != null ? progress.getTimeSpent() : 0));
            
            String lastStudyTime = progress.getLastStudyTime();
            if (lastStudyTime != null) {
                String currentStartTime = (String) courseData.get("startTime");
                if (currentStartTime == null || lastStudyTime.compareTo(currentStartTime) < 0) {
                    courseData.put("startTime", lastStudyTime.split(" ")[0]);
                }
            }
        }
        
        for (Map<String, Object> courseData : courseProgressMap.values()) {
            courseData.put("duration", (Integer) courseData.get("duration") / 60); // 转换为小时
            learningHistory.add(courseData);
        }
        
        detail.put("learningHistory", learningHistory);
        
        return detail;
    }
}
