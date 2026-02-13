package com.ruoyi.edu.mapper;

import com.ruoyi.edu.domain.EduCourseChapter;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EduCourseChapterMapper {

    public EduCourseChapter selectEduCourseChapterById(Long chapterId);

    public List<EduCourseChapter> selectEduCourseChapterList(EduCourseChapter eduCourseChapter);

    public List<EduCourseChapter> selectChaptersByCourseId(Long courseId);

    public int insertEduCourseChapter(EduCourseChapter eduCourseChapter);

    public int updateEduCourseChapter(EduCourseChapter eduCourseChapter);

    public int deleteEduCourseChapterById(Long chapterId);

    public int deleteEduCourseChapterByIds(Long[] chapterIds);
}