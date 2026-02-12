package com.ruoyi.edu.service;

import com.ruoyi.edu.domain.EduCourseLesson;

import java.util.List;

public interface IEduCourseLessonService {

    public EduCourseLesson selectEduCourseLessonById(Long lessonId);

    public List<EduCourseLesson> selectEduCourseLessonList(EduCourseLesson eduCourseLesson);

    public List<EduCourseLesson> selectLessonsByUnitId(Long unitId);

    public int insertEduCourseLesson(EduCourseLesson eduCourseLesson);

    public int updateEduCourseLesson(EduCourseLesson eduCourseLesson);

    public int deleteEduCourseLessonById(Long lessonId);

    public int deleteEduCourseLessonByIds(Long[] lessonIds);
}