package com.ruoyi.edu.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 课程单元表 edu_course_unit
 *
 * @author LingLearn
 */
public class EduCourseUnit extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 单元ID
     */
    private Long unitId;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 单元名称
     */
    private String unitName;

    /**
     * 单元顺序
     */
    private Integer unitOrder;

    /**
     * 单元描述
     */
    private String description;

    /**
     * 课时数
     */
    private Integer totalLessons;

    /**
     * 总时长（分钟）
     */
    private Integer totalDuration;

    /**
     * 是否锁定（需完成前一单元）
     */
    private Boolean isLocked;

    /**
     * 及格分数
     */
    private Integer passingScore;

    /**
     * 经验奖励
     */
    private Integer experienceReward;

    /**
     * 用户进度（非数据库字段）
     */
    private Integer progressPercent;

    /**
     * 是否完成（非数据库字段）
     */
    private Boolean isCompleted;

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Integer getUnitOrder() {
        return unitOrder;
    }

    public void setUnitOrder(Integer unitOrder) {
        this.unitOrder = unitOrder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTotalLessons() {
        return totalLessons;
    }

    public void setTotalLessons(Integer totalLessons) {
        this.totalLessons = totalLessons;
    }

    public Integer getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Integer totalDuration) {
        this.totalDuration = totalDuration;
    }

    public Boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public Integer getPassingScore() {
        return passingScore;
    }

    public void setPassingScore(Integer passingScore) {
        this.passingScore = passingScore;
    }

    public Integer getExperienceReward() {
        return experienceReward;
    }

    public void setExperienceReward(Integer experienceReward) {
        this.experienceReward = experienceReward;
    }

    public Integer getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(Integer progressPercent) {
        this.progressPercent = progressPercent;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    @Override
    public String toString() {
        return "EduCourseUnit{" +
                "unitId=" + unitId +
                ", courseId=" + courseId +
                ", unitName='" + unitName + '\'' +
                ", unitOrder=" + unitOrder +
                ", description='" + description + '\'' +
                ", totalLessons=" + totalLessons +
                ", totalDuration=" + totalDuration +
                ", isLocked=" + isLocked +
                ", passingScore=" + passingScore +
                ", experienceReward=" + experienceReward +
                '}';
    }
}
