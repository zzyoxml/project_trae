package com.ruoyi.edu.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.util.List;

public class EduCourseChapter extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long chapterId;
    private Long courseId;
    private String chapterName;
    private Integer chapterOrder;
    private String description;
    private Integer totalUnits;
    private Integer totalLessons;
    private String delFlag;
    private List<EduCourseUnit> units;

    public Long getChapterId() {
        return chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public Integer getChapterOrder() {
        return chapterOrder;
    }

    public void setChapterOrder(Integer chapterOrder) {
        this.chapterOrder = chapterOrder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTotalUnits() {
        return totalUnits;
    }

    public void setTotalUnits(Integer totalUnits) {
        this.totalUnits = totalUnits;
    }

    public Integer getTotalLessons() {
        return totalLessons;
    }

    public void setTotalLessons(Integer totalLessons) {
        this.totalLessons = totalLessons;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public List<EduCourseUnit> getUnits() {
        return units;
    }

    public void setUnits(List<EduCourseUnit> units) {
        this.units = units;
    }
}