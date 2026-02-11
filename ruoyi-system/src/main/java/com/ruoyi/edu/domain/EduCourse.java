package com.ruoyi.edu.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.List;

/**
 * 课程表 edu_course
 *
 * @author LingLearn
 */
public class EduCourse extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程编码
     */
    private String courseCode;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 课程语言：en,ja,zh
     */
    private String language;

    /**
     * 课程等级：beginner,elementary,intermediate,advanced
     */
    private String level;

    /**
     * 课程类型：general,conversation,grammar,vocabulary,listening,speaking
     */
    private String courseType;

    /**
     * 课程描述
     */
    private String description;

    /**
     * 课程封面图
     */
    private String coverImage;

    /**
     * 预告视频URL
     */
    private String trailerVideo;

    /**
     * 授课教师ID
     */
    private Long teacherId;

    /**
     * 总时长（分钟）
     */
    private Integer totalDuration;

    /**
     * 总课时数
     */
    private Integer totalLessons;

    /**
     * 学习人数
     */
    private Integer totalStudents;

    /**
     * 平均评分
     */
    private BigDecimal rating;

    /**
     * 评分次数
     */
    private Integer ratingCount;

    /**
     * 是否免费
     */
    private Boolean isFree;

    /**
     * 课程价格
     */
    private BigDecimal price;

    /**
     * 是否发布
     */
    private Boolean isPublished;

    /**
     * 是否精选
     */
    private Boolean isFeatured;

    /**
     * 先修课程要求
     */
    private String prerequisites;

    /**
     * 学习目标
     */
    private String learningObjectives;

    /**
     * 标签（多个用逗号分隔）
     */
    private String tags;

    /**
     * 难度系数（1-5）
     */
    private Integer difficultyScore;

    /**
     * 热度评分
     */
    private Integer popularityScore;

    /**
     * 用户学习进度（非数据库字段）
     */
    private Integer progressPercent;

    /**
     * 是否已报名（非数据库字段）
     */
    private Boolean isEnrolled;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 创建部门
     */
    private Long createDept;

    /**
     * 课程单元列表（非数据库字段）
     */
    private List<EduCourseUnit> units;

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getTrailerVideo() {
        return trailerVideo;
    }

    public void setTrailerVideo(String trailerVideo) {
        this.trailerVideo = trailerVideo;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Integer totalDuration) {
        this.totalDuration = totalDuration;
    }

    public Integer getTotalLessons() {
        return totalLessons;
    }

    public void setTotalLessons(Integer totalLessons) {
        this.totalLessons = totalLessons;
    }

    public Integer getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(Integer totalStudents) {
        this.totalStudents = totalStudents;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    public Boolean getIsFree() {
        return isFree;
    }

    public void setIsFree(Boolean isFree) {
        this.isFree = isFree;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getLearningObjectives() {
        return learningObjectives;
    }

    public void setLearningObjectives(String learningObjectives) {
        this.learningObjectives = learningObjectives;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getDifficultyScore() {
        return difficultyScore;
    }

    public void setDifficultyScore(Integer difficultyScore) {
        this.difficultyScore = difficultyScore;
    }

    public Integer getPopularityScore() {
        return popularityScore;
    }

    public void setPopularityScore(Integer popularityScore) {
        this.popularityScore = popularityScore;
    }

    public Integer getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(Integer progressPercent) {
        this.progressPercent = progressPercent;
    }

    public Boolean getIsEnrolled() {
        return isEnrolled;
    }

    public void setIsEnrolled(Boolean isEnrolled) {
        this.isEnrolled = isEnrolled;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Long getCreateDept() {
        return createDept;
    }

    public void setCreateDept(Long createDept) {
        this.createDept = createDept;
    }

    public List<EduCourseUnit> getUnits() {
        return units;
    }

    public void setUnits(List<EduCourseUnit> units) {
        this.units = units;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("courseId", getCourseId())
                .append("courseName", getCourseName())
                .append("courseCode", getCourseCode())
                .append("categoryId", getCategoryId())
                .append("language", getLanguage())
                .append("level", getLevel())
                .append("courseType", getCourseType())
                .append("description", getDescription())
                .append("coverImage", getCoverImage())
                .append("trailerVideo", getTrailerVideo())
                .append("teacherId", getTeacherId())
                .append("totalDuration", getTotalDuration())
                .append("totalLessons", getTotalLessons())
                .append("totalStudents", getTotalStudents())
                .append("rating", getRating())
                .append("ratingCount", getRatingCount())
                .append("isFree", getIsFree())
                .append("price", getPrice())
                .append("isPublished", getIsPublished())
                .append("isFeatured", getIsFeatured())
                .append("tags", getTags())
                .append("difficultyScore", getDifficultyScore())
                .append("popularityScore", getPopularityScore())
                .append("delFlag", getDelFlag())
                .toString();
    }
}
