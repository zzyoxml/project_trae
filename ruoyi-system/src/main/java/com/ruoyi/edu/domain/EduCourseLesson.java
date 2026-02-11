package com.ruoyi.edu.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 课时表 edu_course_lesson
 *
 * @author LingLearn
 */
public class EduCourseLesson extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 课时ID
     */
    private Long lessonId;

    /**
     * 单元ID
     */
    private Long unitId;

    /**
     * 课时名称
     */
    private String lessonName;

    /**
     * 课时顺序
     */
    private Integer lessonOrder;

    /**
     * 课时类型：vocabulary,grammar,listening,speaking,quiz,video
     */
    private String lessonType;

    /**
     * 课时内容（JSON格式）
     */
    private String content;

    /**
     * 时长（分钟）
     */
    private Integer duration;

    /**
     * 是否免费试看
     */
    private Boolean isFree;

    /**
     * 是否预览
     */
    private Boolean isPreview;

    /**
     * 经验奖励
     */
    private Integer experienceReward;

    /**
     * 金币奖励
     */
    private Integer coinReward;

    /**
     * 及格分数
     */
    private Integer passingScore;

    /**
     * 最大尝试次数
     */
    private Integer maxAttempts;

    /**
     * 课时资源（JSON格式）
     */
    private String resources;

    /**
     * 用户学习状态（非数据库字段）
     */
    private String status;

    /**
     * 用户最佳成绩（非数据库字段）
     */
    private Integer bestScore;

    /**
     * 用户完成次数（非数据库字段）
     */
    private Integer attemptCount;

    /**
     * 删除标志
     */
    private String delFlag;

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public Integer getLessonOrder() {
        return lessonOrder;
    }

    public void setLessonOrder(Integer lessonOrder) {
        this.lessonOrder = lessonOrder;
    }

    public String getLessonType() {
        return lessonType;
    }

    public void setLessonType(String lessonType) {
        this.lessonType = lessonType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Boolean getIsFree() {
        return isFree;
    }

    public void setIsFree(Boolean isFree) {
        this.isFree = isFree;
    }

    public Boolean getIsPreview() {
        return isPreview;
    }

    public void setIsPreview(Boolean isPreview) {
        this.isPreview = isPreview;
    }

    public Integer getExperienceReward() {
        return experienceReward;
    }

    public void setExperienceReward(Integer experienceReward) {
        this.experienceReward = experienceReward;
    }

    public Integer getCoinReward() {
        return coinReward;
    }

    public void setCoinReward(Integer coinReward) {
        this.coinReward = coinReward;
    }

    public Integer getPassingScore() {
        return passingScore;
    }

    public void setPassingScore(Integer passingScore) {
        this.passingScore = passingScore;
    }

    public Integer getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(Integer maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return "EduCourseLesson{" +
                "lessonId=" + lessonId +
                ", unitId=" + unitId +
                ", lessonName='" + lessonName + '\'' +
                ", lessonOrder=" + lessonOrder +
                ", lessonType='" + lessonType + '\'' +
                ", duration=" + duration +
                ", isFree=" + isFree +
                ", isPreview=" + isPreview +
                ", experienceReward=" + experienceReward +
                ", coinReward=" + coinReward +
                ", passingScore=" + passingScore +
                ", maxAttempts=" + maxAttempts +
                '}';
    }
}
