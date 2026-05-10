package com.ruoyi.edu.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 徽章表 edu_badge
 *
 * @author LingLearn
 */
public class EduBadge extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 徽章ID
     */
    private Long badgeId;

    /**
     * 徽章名称
     */
    private String badgeName;

    /**
     * 徽章类型：streak,score,social,master,rare
     */
    private String badgeType;

    /**
     * 徽章描述
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
     * 获取条件类型
     */
    private String requirementType;

    /**
     * 获取条件值
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

    public Long getBadgeId() {
        return badgeId;
    }

    public void setBadgeId(Long badgeId) {
        this.badgeId = badgeId;
    }

    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }

    public String getBadgeType() {
        return badgeType;
    }

    public void setBadgeType(String badgeType) {
        this.badgeType = badgeType;
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

    @Override
    public String toString() {
        return "EduBadge{" +
                "badgeId=" + badgeId +
                ", badgeName='" + badgeName + '\'' +
                ", badgeType='" + badgeType + '\'' +
                ", description='" + description + '\'' +
                ", tier='" + tier + '\'' +
                ", rarity=" + rarity +
                '}';
    }
}
