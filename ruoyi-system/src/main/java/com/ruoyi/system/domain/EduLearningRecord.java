package com.ruoyi.edu.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;

/**
 * 学习记录表 edu_learning_record
 * 记录用户的学习行为数据
 *
 * @author LingLearn
 */
public class EduLearningRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    private Long recordId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 课时ID
     */
    private Long lessonId;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 活动类型：vocabulary,grammar,listening,speaking,quiz,practice
     */
    private String activityType;

    /**
     * 学习语言
     */
    private String language;

    /**
     * 学习时长（分钟）
     */
    private Integer duration;

    /**
     * 得分
     */
    private Integer score;

    /**
     * 最高得分
     */
    private Integer maxScore;

    /**
     * 正确率
     */
    private BigDecimal accuracy;

    /**
     * 获得经验
     */
    private Integer xpEarned;

    /**
     * 获得金币
     */
    private Integer coinsEarned;

    /**
     * 是否完成
     */
    private Boolean completed;

    /**
     * 学习日期（非数据库字段）
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String studyDate;

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    public BigDecimal getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(BigDecimal accuracy) {
        this.accuracy = accuracy;
    }

    public Integer getXpEarned() {
        return xpEarned;
    }

    public void setXpEarned(Integer xpEarned) {
        this.xpEarned = xpEarned;
    }

    public Integer getCoinsEarned() {
        return coinsEarned;
    }

    public void setCoinsEarned(Integer coinsEarned) {
        this.coinsEarned = coinsEarned;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getStudyDate() {
        return studyDate;
    }

    public void setStudyDate(String studyDate) {
        this.studyDate = studyDate;
    }

    @Override
    public String toString() {
        return "EduLearningRecord{" +
                "recordId=" + recordId +
                ", userId=" + userId +
                ", lessonId=" + lessonId +
                ", courseId=" + courseId +
                ", activityType='" + activityType + '\'' +
                ", language='" + language + '\'' +
                ", duration=" + duration +
                ", score=" + score +
                ", accuracy=" + accuracy +
                ", xpEarned=" + xpEarned +
                ", completed=" + completed +
                '}';
    }
}
