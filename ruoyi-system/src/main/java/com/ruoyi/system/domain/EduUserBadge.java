package com.ruoyi.edu.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 用户徽章表 edu_user_badge
 *
 * @author LingLearn
 */
public class EduUserBadge extends BaseEntity {
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
     * 徽章ID
     */
    private Long badgeId;

    /**
     * 获得时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String earnedTime;

    /**
     * 是否展示
     */
    private Boolean isDisplayed;

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

    public Long getBadgeId() {
        return badgeId;
    }

    public void setBadgeId(Long badgeId) {
        this.badgeId = badgeId;
    }

    public String getEarnedTime() {
        return earnedTime;
    }

    public void setEarnedTime(String earnedTime) {
        this.earnedTime = earnedTime;
    }

    public Boolean getIsDisplayed() {
        return isDisplayed;
    }

    public void setIsDisplayed(Boolean isDisplayed) {
        this.isDisplayed = isDisplayed;
    }

    @Override
    public String toString() {
        return "EduUserBadge{" +
                "id=" + id +
                ", userId=" + userId +
                ", badgeId=" + badgeId +
                ", earnedTime='" + earnedTime + '\'' +
                ", isDisplayed=" + isDisplayed +
                '}';
    }
}
