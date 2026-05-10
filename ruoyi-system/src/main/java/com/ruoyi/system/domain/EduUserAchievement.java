package com.ruoyi.edu.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 用户成就表 edu_user_achievement
 *
 * @author LingLearn
 */
public class EduUserAchievement extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 成就ID
     */
    private Long achievementId;

    /**
     * 当前进度
     */
    private Integer progress;

    /**
     * 目标值
     */
    private Integer targetValue;

    /**
     * 是否完成
     */
    private Boolean isCompleted;

    /**
     * 完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String completedTime;

    /**
     * 是否已发送通知
     */
    private Boolean notificationSent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAchievementId() {
        return achievementId;
    }

    public void setAchievementId(Long achievementId) {
        this.achievementId = achievementId;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Integer getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(Integer targetValue) {
        this.targetValue = targetValue;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(String completedTime) {
        this.completedTime = completedTime;
    }

    public Boolean getNotificationSent() {
        return notificationSent;
    }

    public void setNotificationSent(Boolean notificationSent) {
        this.notificationSent = notificationSent;
    }

    @Override
    public String toString() {
        return "EduUserAchievement{" +
                "id=" + id +
                ", userId=" + userId +
                ", achievementId=" + achievementId +
                ", progress=" + progress +
                ", targetValue=" + targetValue +
                ", isCompleted=" + isCompleted +
                ", completedTime='" + completedTime + '\'' +
                '}';
    }
}
