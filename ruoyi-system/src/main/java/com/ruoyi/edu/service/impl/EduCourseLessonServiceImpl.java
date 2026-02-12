package com.ruoyi.edu.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.edu.domain.EduCourseLesson;
import com.ruoyi.edu.mapper.EduCourseLessonMapper;
import com.ruoyi.edu.service.IEduCourseLessonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EduCourseLessonServiceImpl implements IEduCourseLessonService {

    private static final Logger log = LoggerFactory.getLogger(EduCourseLessonServiceImpl.class);

    @Autowired
    private EduCourseLessonMapper eduCourseLessonMapper;

    @Override
    public EduCourseLesson selectEduCourseLessonById(Long lessonId) {
        if (lessonId == null) {
            throw new ServiceException("课时ID不能为空");
        }
        return eduCourseLessonMapper.selectEduCourseLessonById(lessonId);
    }

    @Override
    public List<EduCourseLesson> selectEduCourseLessonList(EduCourseLesson eduCourseLesson) {
        return eduCourseLessonMapper.selectEduCourseLessonList(eduCourseLesson);
    }

    @Override
    public List<EduCourseLesson> selectLessonsByUnitId(Long unitId) {
        if (unitId == null) {
            throw new ServiceException("单元ID不能为空");
        }
        return eduCourseLessonMapper.selectLessonsByUnitId(unitId);
    }

    @Override
    public int insertEduCourseLesson(EduCourseLesson eduCourseLesson) {
        if (StringUtils.isEmpty(eduCourseLesson.getLessonName())) {
            throw new ServiceException("课时名称不能为空");
        }
        if (eduCourseLesson.getUnitId() == null) {
            throw new ServiceException("所属单元不能为空");
        }
        return eduCourseLessonMapper.insertEduCourseLesson(eduCourseLesson);
    }

    @Override
    public int updateEduCourseLesson(EduCourseLesson eduCourseLesson) {
        if (eduCourseLesson.getLessonId() == null) {
            throw new ServiceException("课时ID不能为空");
        }
        return eduCourseLessonMapper.updateEduCourseLesson(eduCourseLesson);
    }

    @Override
    public int deleteEduCourseLessonById(Long lessonId) {
        if (lessonId == null) {
            throw new ServiceException("课时ID不能为空");
        }
        return eduCourseLessonMapper.deleteEduCourseLessonById(lessonId);
    }

    @Override
    public int deleteEduCourseLessonByIds(Long[] lessonIds) {
        if (lessonIds == null || lessonIds.length == 0) {
            throw new ServiceException("请选择要删除的课时");
        }
        return eduCourseLessonMapper.deleteEduCourseLessonByIds(lessonIds);
    }
}