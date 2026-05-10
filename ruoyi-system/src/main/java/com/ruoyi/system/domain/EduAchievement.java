package com.ruoyi.edu.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 成就表 edu_achievement
 * 存储用户可以达成的各类成就
 *
 * @author LingLearn
 */
public class EduAchievement extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 成就ID
     */
    private Long achievementId;

    /**
     * 成就代码
     */
    private String achievementCode;

    /**
     * 成就名称
     */
    private String achievementName;

    /**
     * 成就类型：study,streak,score,social,collection
     */
    private String achievementType;

    /**
     * 适用语言
     */
    private String language;

    /**
     * 成就描述
     */
    private String description;

    /**
     * 图标URL
     */
    private String iconUrl;

    /**
     * 等级：bronze,silver,gold,diamond,legendary
     */
    private String tier;

    /**
     * 需求类型
     */
    private String requirementType;

    /**
     * 需求数值
     */
    private Integer requirementValue;

    /**
     * 经验奖励
     */
    private Integer xpReward;

    /**
     * 金币奖励
     */
    private Integer coinReward;

    /**
     * 关联徽章ID
     */
    private Long badgeId;

    /**
     * 是否启用
     */
    private Boolean isActive;

    /**
     * 显示顺序
     */
    private Integer displayOrder;

    /**
     * 稀有度（1-5）
     */
    private Integer rarity;

    /**
     * 用户进度（非数据库字段）
     */
    private Integer progress;

    /**
     * 是否完成（非数据库字段）
     */
    private Boolean isCompleted;

    /**
     * 完成时间（非数据库字段）
     */
    private String completedTime;

    public Long getAchievementId() {
        return achievementId;
    }

    public void setAchievementId(Long achievementId) {
        this.achievementId = achievementId;
    }

    public String getAchievementCode() {
        return achievementCode;
    }

    public void setAchievementCode(String achievementCode) {
        this.achievementCode = achievementCode;
    }

    public String getAchievementName() {
        return achievementName;
    }

    public void setAchievementName(String achievementName) {
        this.achievementName = achievementName;
    }

    public String getAchievementType() {
        return achievementType;
    }

    public void setAchievementType(String achievementType) {
        this.achievementType = achievementType;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getRequirementType() {
        return requirementType;
    }

    public void setRequirementType(String requirementType) {
        this.requirementType = requirementType;
    }

    public Integer getRequirementValue() {
        return requirementValue;
    }

    public void setRequirementValue(Integer requirementValue) {
        this.requirementValue = requirementValue;
    }

    public Integer getXpReward() {
        return xpReward;
    }

    public void setXpReward(Integer xpReward) {
        this.xpReward = xpReward;
    }

    public Integer getCoinReward() {
        return coinReward;
    }

    public void setCoinReward(Integer coinReward) {
        this.coinReward = coinReward;
    }

    public Long getBadgeId() {
        return badgeId;
    }

    public void setBadgeId(Long badgeId) {
        this.badgeId = badgeId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Integer getRarity() {
        return rarity;
    }

    public void setRarity(Integer rarity) {
        this.rarity = rarity;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
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

    @Override
    public String toString() {
        return "EduAchievement{" +
                "achievementId=" + achievementId +
                ", achievementCode='" + achievementCode + '\'' +
                ", achievementName='" + achievementName + '\'' +
                ", achievementType='" + achievementType + '\'' +
                ", tier='" + tier + '\'' +
                ", requirementType='" + requirementType + '\'' +
                ", requirementValue=" + requirementValue +
                ", rarity=" + rarity +
                '}';
    }
}
