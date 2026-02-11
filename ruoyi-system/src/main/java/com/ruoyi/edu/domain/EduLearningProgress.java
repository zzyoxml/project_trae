package com.ruoyi.edu.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 学习进度表 edu_learning_progress
 * 跟踪用户在各课程、单元、课时中的学习进度
 *
 * @author LingLearn
 */
public class EduLearningProgress extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 进度ID
     */
    private Long progressId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 单元ID
     */
    private Long unitId;

    /**
     * 课时ID
     */
    private Long lessonId;

    /**
     * 状态：not_started,in_progress,completed,locked
     */
    private String status;

    /**
     * 完成进度百分比
     */
    private Integer progressPercent;

    /**
     * 最高得分
     */
    private Integer bestScore;

    /**
     * 尝试次数
     */
    private Integer attemptCount;

    /**
     * 花费时间（分钟）
     */
    private Integer timeSpent;

    /**
     * 最后学习时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String lastStudyTime;

    /**
     * 完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String completedTime;

    /**
     * 下次复习时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String nextReviewTime;

    /**
     * 掌握程度（0-100）
     */
    private Integer masteryLevel;

    public Long getProgressId() {
        return progressId;
    }

    public void setProgressId(Long progressId) {
        this.progressId = progressId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(Integer progressPercent) {
        this.progressPercent = progressPercent;
    }

    public Integer getBestScore() {
        return bestScore;
    }

    public void setBestScore(Integer bestScore) {
        this.bestScore = bestScore;
    }

    public Integer getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(Integer attemptCount) {
        this.attemptCount = attemptCount;
    }

    public Integer getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }

    public String getLastStudyTime() {
        return lastStudyTime;
    }

    public void setLastStudyTime(String lastStudyTime) {
        this.lastStudyTime = lastStudyTime;
    }

    public String getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(String completedTime) {
        this.completedTime = completedTime;
    }

    public String getNextReviewTime() {
        return nextReviewTime;
    }

    public void setNextReviewTime(String nextReviewTime) {
        this.nextReviewTime = nextReviewTime;
    }

    public Integer getMasteryLevel() {
        return masteryLevel;
    }

    public void setMasteryLevel(Integer masteryLevel) {
        this.masteryLevel = masteryLevel;
    }

    @Override
    public String toString() {
        return "EduLearningProgress{" +
                "progressId=" + progressId +
                ", userId=" + userId +
                ", courseId=" + courseId +
                ", unitId=" + unitId +
                ", lessonId=" + lessonId +
                ", status='" + status + '\'' +
                ", progressPercent=" + progressPercent +
                ", bestScore=" + bestScore +
                ", masteryLevel=" + masteryLevel +
                '}';
    }
}
