package com.ruoyi.edu.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 用户闯关记录表 edu_user_challenge
 *
 * @author LingLearn
 */
public class EduUserChallenge extends BaseEntity {
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
     * 关卡ID
     */
    private Long challengeId;

    /**
     * 关卡等级
     */
    private Integer stageLevel;

    /**
     * 语言
     */
    private String language;

    /**
     * 尝试次数
     */
    private Integer attempts;

    /**
     * 最高得分
     */
    private Integer bestScore;

    /**
     * 最高星级（1-3）
     */
    private Integer bestStars;

    /**
     * 最佳用时（秒）
     */
    private Integer bestTime;

    /**
     * 状态：not_started,in_progress,passed,failed
     */
    private String status;

    /**
     * 最后尝试时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String lastAttemptTime;

    /**
     * 通关时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String completedTime;

    /**
     * 获得经验
     */
    private Integer xpEarned;

    /**
     * 获得金币
     */
    private Integer coinsEarned;

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

    public Long getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(Long challengeId) {
        this.challengeId = challengeId;
    }

    public Integer getStageLevel() {
        return stageLevel;
    }

    public void setStageLevel(Integer stageLevel) {
        this.stageLevel = stageLevel;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public Integer getBestScore() {
        return bestScore;
    }

    public void setBestScore(Integer bestScore) {
        this.bestScore = bestScore;
    }

    public Integer getBestStars() {
        return bestStars;
    }

    public void setBestStars(Integer bestStars) {
        this.bestStars = bestStars;
    }

    public Integer getBestTime() {
        return bestTime;
    }

    public void setBestTime(Integer bestTime) {
        this.bestTime = bestTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastAttemptTime() {
        return lastAttemptTime;
    }

    public void setLastAttemptTime(String lastAttemptTime) {
        this.lastAttemptTime = lastAttemptTime;
    }

    public String getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(String completedTime) {
        this.completedTime = completedTime;
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

    @Override
    public String toString() {
        return "EduUserChallenge{" +
                "recordId=" + recordId +
                ", userId=" + userId +
                ", challengeId=" + challengeId +
                ", stageLevel=" + stageLevel +
                ", language='" + language + '\'' +
                ", attempts=" + attempts +
                ", bestScore=" + bestScore +
                ", bestStars=" + bestStars +
                ", status='" + status + '\'' +
                '}';
    }
}
