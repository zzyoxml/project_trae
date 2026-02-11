package com.ruoyi.edu.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 闯关天梯表 edu_ladder_challenge
 * 闯关游戏的关卡配置
 *
 * @author LingLearn
 */
public class EduLadderChallenge extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 关卡ID
     */
    private Long challengeId;

    /**
     * 关卡阶段ID
     */
    private Integer stageId;

    /**
     * 关卡名称
     */
    private String stageName;

    /**
     * 关卡等级（1-100）
     */
    private Integer stageLevel;

    /**
     * 语言
     */
    private String language;

    /**
     * 挑战类型：vocabulary,grammar,listening,speaking,mixed
     */
    private String challengeType;

    /**
     * 难度（1-5）
     */
    private Integer difficulty;

    /**
     * 题目（JSON数组）
     */
    private String questions;

    /**
     * 时间限制（秒）
     */
    private Integer timeLimit;

    /**
     * 及格分数
     */
    private Integer passingScore;

    /**
     * 经验奖励
     */
    private Integer xpReward;

    /**
     * 金币奖励
     */
    private Integer coinReward;

    /**
     * 3星通关分数
     */
    private Integer stars3Score;

    /**
     * 2星通关分数
     */
    private Integer stars2Score;

    /**
     * 1星通关分数
     */
    private Integer stars1Score;

    /**
     * 是否解锁
     */
    private Boolean isUnlocked;

    /**
     * 前置关卡ID
     */
    private Long prerequisiteChallengeId;

    /**
     * 关卡头像
     */
    private String avatarUrl;

    /**
     * 关卡描述
     */
    private String description;

    /**
     * 用户是否已通过（非数据库字段）
     */
    private Boolean isPassed;

    /**
     * 用户最高星级（非数据库字段）
     */
    private Integer bestStars;

    public Long getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(Long challengeId) {
        this.challengeId = challengeId;
    }

    public Integer getStageId() {
        return stageId;
    }

    public void setStageId(Integer stageId) {
        this.stageId = stageId;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
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

    public String getChallengeType() {
        return challengeType;
    }

    public void setChallengeType(String challengeType) {
        this.challengeType = challengeType;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Integer getPassingScore() {
        return passingScore;
    }

    public void setPassingScore(Integer passingScore) {
        this.passingScore = passingScore;
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

    public Integer getStars3Score() {
        return stars3Score;
    }

    public void setStars3Score(Integer stars3Score) {
        this.stars3Score = stars3Score;
    }

    public Integer getStars2Score() {
        return stars2Score;
    }

    public void setStars2Score(Integer stars2Score) {
        this.stars2Score = stars2Score;
    }

    public Integer getStars1Score() {
        return stars1Score;
    }

    public void setStars1Score(Integer stars1Score) {
        this.stars1Score = stars1Score;
    }

    public Boolean getIsUnlocked() {
        return isUnlocked;
    }

    public void setIsUnlocked(Boolean isUnlocked) {
        this.isUnlocked = isUnlocked;
    }

    public Long getPrerequisiteChallengeId() {
        return prerequisiteChallengeId;
    }

    public void setPrerequisiteChallengeId(Long prerequisiteChallengeId) {
        this.prerequisiteChallengeId = prerequisiteChallengeId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsPassed() {
        return isPassed;
    }

    public void setIsPassed(Boolean isPassed) {
        this.isPassed = isPassed;
    }

    public Integer getBestStars() {
        return bestStars;
    }

    public void setBestStars(Integer bestStars) {
        this.bestStars = bestStars;
    }

    @Override
    public String toString() {
        return "EduLadderChallenge{" +
                "challengeId=" + challengeId +
                ", stageId=" + stageId +
                ", stageName='" + stageName + '\'' +
                ", stageLevel=" + stageLevel +
                ", language='" + language + '\'' +
                ", challengeType='" + challengeType + '\'' +
                ", difficulty=" + difficulty +
                ", timeLimit=" + timeLimit +
                ", passingScore=" + passingScore +
                '}';
    }
}
