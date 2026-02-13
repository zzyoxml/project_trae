package com.ruoyi.edu.service;

import com.ruoyi.edu.domain.EduCourseChapter;
import java.util.List;

public interface IEduCourseChapterService {

    public EduCourseChapter selectEduCourseChapterById(Long chapterId);

    public List<EduCourseChapter> selectEduCourseChapterList(EduCourseChapter eduCourseChapter);

    public List<EduCourseChapter> selectChaptersByCourseId(Long courseId);

    public int insertEduCourseChapter(EduCourseChapter eduCourseChapter);

    public int updateEduCourseChapter(EduCourseChapter eduCourseChapter);

    public int deleteEduCourseChapterById(Long chapterId);

    public int deleteEduCourseChapterByIds(Long[] chapterIds);
}