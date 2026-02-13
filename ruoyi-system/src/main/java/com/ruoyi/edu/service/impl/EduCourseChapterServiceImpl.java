package com.ruoyi.edu.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.edu.domain.EduCourseChapter;
import com.ruoyi.edu.mapper.EduCourseChapterMapper;
import com.ruoyi.edu.service.IEduCourseChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EduCourseChapterServiceImpl implements IEduCourseChapterService {

    @Autowired
    private EduCourseChapterMapper eduCourseChapterMapper;

    @Override
    public EduCourseChapter selectEduCourseChapterById(Long chapterId) {
        if (chapterId == null) {
            throw new ServiceException("章节ID不能为空");
        }
        return eduCourseChapterMapper.selectEduCourseChapterById(chapterId);
    }

    @Override
    public List<EduCourseChapter> selectEduCourseChapterList(EduCourseChapter eduCourseChapter) {
        return eduCourseChapterMapper.selectEduCourseChapterList(eduCourseChapter);
    }

    @Override
    public List<EduCourseChapter> selectChaptersByCourseId(Long courseId) {
        if (courseId == null) {
            throw new ServiceException("课程ID不能为空");
        }
        return eduCourseChapterMapper.selectChaptersByCourseId(courseId);
    }

    @Override
    public int insertEduCourseChapter(EduCourseChapter eduCourseChapter) {
        if (StringUtils.isEmpty(eduCourseChapter.getChapterName())) {
            throw new ServiceException("章节名称不能为空");
        }
        if (eduCourseChapter.getCourseId() == null) {
            throw new ServiceException("所属课程不能为空");
        }
        return eduCourseChapterMapper.insertEduCourseChapter(eduCourseChapter);
    }

    @Override
    public int updateEduCourseChapter(EduCourseChapter eduCourseChapter) {
        if (eduCourseChapter.getChapterId() == null) {
            throw new ServiceException("章节ID不能为空");
        }
        return eduCourseChapterMapper.updateEduCourseChapter(eduCourseChapter);
    }

    @Override
    public int deleteEduCourseChapterById(Long chapterId) {
        if (chapterId == null) {
            throw new ServiceException("章节ID不能为空");
        }
        return eduCourseChapterMapper.deleteEduCourseChapterById(chapterId);
    }

    @Override
    public int deleteEduCourseChapterByIds(Long[] chapterIds) {
        if (chapterIds == null || chapterIds.length == 0) {
            throw new ServiceException("请选择要删除的章节");
        }
        return eduCourseChapterMapper.deleteEduCourseChapterByIds(chapterIds);
    }
}