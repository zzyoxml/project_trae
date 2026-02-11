package com.ruoyi.edu.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 用户学习路径表 edu_user_learning_path
 *
 * @author LingLearn
 */
public class EduUserLearningPath extends BaseEntity {
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
     * 路径ID
     */
    private Long pathId;

    /**
     * 完成进度
     */
    private Integer progressPercent;

    /**
     * 当前步骤
     */
    private Integer currentStep;

    /**
     * 状态：not_started,in_progress,completed
     */
    private String status;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String startTime;

    /**
     * 完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String completionTime;

    /**
     * 预计完成日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String estimatedCompletion;

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

    public Long getPathId() {
        return pathId;
    }

    public void setPathId(Long pathId) {
        this.pathId = pathId;
    }

    public Integer getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(Integer progressPercent) {
        this.progressPercent = progressPercent;
    }

    public Integer getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(Integer currentStep) {
        this.currentStep = currentStep;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(String completionTime) {
        this.completionTime = completionTime;
    }

    public String getEstimatedCompletion() {
        return estimatedCompletion;
    }

    public void setEstimatedCompletion(String estimatedCompletion) {
        this.estimatedCompletion = estimatedCompletion;
    }

    @Override
    public String toString() {
        return "EduUserLearningPath{" +
                "id=" + id +
                ", userId=" + userId +
                ", pathId=" + pathId +
                ", progressPercent=" + progressPercent +
                ", currentStep=" + currentStep +
                ", status='" + status + '\'' +
                ", startTime='" + startTime + '\'' +
                ", completionTime='" + completionTime + '\'' +
                ", estimatedCompletion='" + estimatedCompletion + '\'' +
                '}';
    }
}
