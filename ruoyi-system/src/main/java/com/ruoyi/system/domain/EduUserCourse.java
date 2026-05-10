package com.ruoyi.edu.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 用户课程表 edu_user_course
 * 记录用户报名的课程及学习状态
 *
 * @author LingLearn
 */
public class EduUserCourse extends BaseEntity {
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
     * 课程ID
     */
    private Long courseId;

    /**
     * 报名日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String enrollmentDate;

    /**
     * 课程完成百分比
     */
    private Integer progressPercent;

    /**
     * 状态：enrolled,learning,completed,dropped
     */
    private String status;

    /**
     * 当前学习课时ID
     */
    private Long currentLessonId;

    /**
     * 开始学习日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String startDate;

    /**
     * 完成日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String completionDate;

    /**
     * 证书URL
     */
    private String certificateUrl;

    /**
     * 是否收藏
     */
    private Boolean isFavorite;

    /**
     * 用户评分
     */
    private Integer rating;

    /**
     * 用户评价
     */
    private String review;

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

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(String enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public Integer getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(Integer progressPercent) {
        this.progressPercent = progressPercent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCurrentLessonId() {
        return currentLessonId;
    }

    public void setCurrentLessonId(Long currentLessonId) {
        this.currentLessonId = currentLessonId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

    public String getCertificateUrl() {
        return certificateUrl;
    }

    public void setCertificateUrl(String certificateUrl) {
        this.certificateUrl = certificateUrl;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @Override
    public String toString() {
        return "EduUserCourse{" +
                "id=" + id +
                ", userId=" + userId +
                ", courseId=" + courseId +
                ", enrollmentDate='" + enrollmentDate + '\'' +
                ", progressPercent=" + progressPercent +
                ", status='" + status + '\'' +
                ", currentLessonId=" + currentLessonId +
                ", isFavorite=" + isFavorite +
                ", rating=" + rating +
                '}';
    }
}
