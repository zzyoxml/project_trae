package com.ruoyi.edu.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 用户扩展信息表 edu_user_profile
 *
 * @author LingLearn
 */
public class EduUserProfile extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 母语
     */
    private String nativeLanguage;

    /**
     * 学习语言（多个用逗号分隔）
     */
    private String learningLanguages;

    /**
     * 当前等级：beginner,elementary,intermediate,advanced
     */
    private String proficiencyLevel;

    /**
     * 总学习时间（分钟）
     */
    private Long totalStudyTime;

    /**
     * 连续学习天数
     */
    private Integer currentStreak;

    /**
     * 最长连续学习天数
     */
    private Integer longestStreak;

    /**
     * 最后学习日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String lastStudyDate;

    /**
     * 每日目标（分钟）
     */
    private Integer dailyGoal;

    /**
     * 最佳学习时段：morning,afternoon,evening
     */
    private String preferredLearningTime;

    /**
     * 是否启用语音功能（0否 1是）
     */
    private Boolean voiceEnabled;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 个人简介
     */
    private String bio;

    /**
     * 总积分
     */
    private Integer totalPoints;

    /**
     * 用户等级
     */
    private Integer level;

    /**
     * 经验值
     */
    private Integer experiencePoints;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNativeLanguage() {
        return nativeLanguage;
    }

    public void setNativeLanguage(String nativeLanguage) {
        this.nativeLanguage = nativeLanguage;
    }

    public String getLearningLanguages() {
        return learningLanguages;
    }

    public void setLearningLanguages(String learningLanguages) {
        this.learningLanguages = learningLanguages;
    }

    public String getProficiencyLevel() {
        return proficiencyLevel;
    }

    public void setProficiencyLevel(String proficiencyLevel) {
        this.proficiencyLevel = proficiencyLevel;
    }

    public Long getTotalStudyTime() {
        return totalStudyTime;
    }

    public void setTotalStudyTime(Long totalStudyTime) {
        this.totalStudyTime = totalStudyTime;
    }

    public Integer getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(Integer currentStreak) {
        this.currentStreak = currentStreak;
    }

    public Integer getLongestStreak() {
        return longestStreak;
    }

    public void setLongestStreak(Integer longestStreak) {
        this.longestStreak = longestStreak;
    }

    public String getLastStudyDate() {
        return lastStudyDate;
    }

    public void setLastStudyDate(String lastStudyDate) {
        this.lastStudyDate = lastStudyDate;
    }

    public Integer getDailyGoal() {
        return dailyGoal;
    }

    public void setDailyGoal(Integer dailyGoal) {
        this.dailyGoal = dailyGoal;
    }

    public String getPreferredLearningTime() {
        return preferredLearningTime;
    }

    public void setPreferredLearningTime(String preferredLearningTime) {
        this.preferredLearningTime = preferredLearningTime;
    }

    public Boolean getVoiceEnabled() {
        return voiceEnabled;
    }

    public void setVoiceEnabled(Boolean voiceEnabled) {
        this.voiceEnabled = voiceEnabled;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getExperiencePoints() {
        return experiencePoints;
    }

    public void setExperiencePoints(Integer experiencePoints) {
        this.experiencePoints = experiencePoints;
    }

    @Override
    public String toString() {
        return "EduUserProfile{" +
                "userId=" + userId +
                ", nativeLanguage='" + nativeLanguage + '\'' +
                ", learningLanguages='" + learningLanguages + '\'' +
                ", proficiencyLevel='" + proficiencyLevel + '\'' +
                ", totalStudyTime=" + totalStudyTime +
                ", currentStreak=" + currentStreak +
                ", longestStreak=" + longestStreak +
                ", lastStudyDate='" + lastStudyDate + '\'' +
                ", dailyGoal=" + dailyGoal +
                ", preferredLearningTime='" + preferredLearningTime + '\'' +
                ", voiceEnabled=" + voiceEnabled +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", bio='" + bio + '\'' +
                ", totalPoints=" + totalPoints +
                ", level=" + level +
                ", experiencePoints=" + experiencePoints +
                '}';
    }
}
