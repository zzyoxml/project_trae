package com.ruoyi.edu.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 学习路径推荐表 edu_learning_path
 *
 * @author LingLearn
 */
public class EduLearningPath extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 路径ID
     */
    private Long pathId;

    /**
     * 路径名称
     */
    private String pathName;

    /**
     * 语言
     */
    private String language;

    /**
     * 目标等级
     */
    private String targetLevel;

    /**
     * 起始等级
     */
    private String startLevel;

    /**
     * 路径描述
     */
    private String description;

    /**
     * 课程顺序（课程ID数组，JSON格式）
     */
    private String courseSequence;

    /**
     * 预计时长（天）
     */
    private Integer estimatedDuration;

    /**
     * 前置要求
     */
    private String prerequisites;

    /**
     * 目标技能（JSON格式）
     */
    private String targetSkills;

    /**
     * 是否推荐
     */
    private Boolean isRecommended;

    /**
     * 成功率
     */
    private java.math.BigDecimal successRate;

    /**
     * 报名人数
     */
    private Integer enrollmentCount;

    /**
     * 评分
     */
    private java.math.BigDecimal rating;

    /**
     * 课程数量（非数据库字段）
     */
    private Integer courseCount;

    /**
     * 用户是否已报名（非数据库字段）
     */
    private Boolean isEnrolled;

    public Long getPathId() {
        return pathId;
    }

    public void setPathId(Long pathId) {
        this.pathId = pathId;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTargetLevel() {
        return targetLevel;
    }

    public void setTargetLevel(String targetLevel) {
        this.targetLevel = targetLevel;
    }

    public String getStartLevel() {
        return startLevel;
    }

    public void setStartLevel(String startLevel) {
        this.startLevel = startLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCourseSequence() {
        return courseSequence;
    }

    public void setCourseSequence(String courseSequence) {
        this.courseSequence = courseSequence;
    }

    public Integer getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(Integer estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getTargetSkills() {
        return targetSkills;
    }

    public void setTargetSkills(String targetSkills) {
        this.targetSkills = targetSkills;
    }

    public Boolean getIsRecommended() {
        return isRecommended;
    }

    public void setIsRecommended(Boolean isRecommended) {
        this.isRecommended = isRecommended;
    }

    public java.math.BigDecimal getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(java.math.BigDecimal successRate) {
        this.successRate = successRate;
    }

    public Integer getEnrollmentCount() {
        return enrollmentCount;
    }

    public void setEnrollmentCount(Integer enrollmentCount) {
        this.enrollmentCount = enrollmentCount;
    }

    public java.math.BigDecimal getRating() {
        return rating;
    }

    public void setRating(java.math.BigDecimal rating) {
        this.rating = rating;
    }

    public Integer getCourseCount() {
        return courseCount;
    }

    public void setCourseCount(Integer courseCount) {
        this.courseCount = courseCount;
    }

    public Boolean getIsEnrolled() {
        return isEnrolled;
    }

    public void setIsEnrolled(Boolean isEnrolled) {
        this.isEnrolled = isEnrolled;
    }

    @Override
    public String toString() {
        return "EduLearningPath{" +
                "pathId=" + pathId +
                ", pathName='" + pathName + '\'' +
                ", language='" + language + '\'' +
                ", targetLevel='" + targetLevel + '\'' +
                ", startLevel='" + startLevel + '\'' +
                ", estimatedDuration=" + estimatedDuration +
                ", isRecommended=" + isRecommended +
                ", successRate=" + successRate +
                ", enrollmentCount=" + enrollmentCount +
                '}';
    }
}
