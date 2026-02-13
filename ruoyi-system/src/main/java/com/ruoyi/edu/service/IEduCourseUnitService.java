package com.ruoyi.edu.service;

import com.ruoyi.edu.domain.EduCourseUnit;

import java.util.List;

public interface IEduCourseUnitService {

    public EduCourseUnit selectEduCourseUnitById(Long unitId);

    public List<EduCourseUnit> selectEduCourseUnitList(EduCourseUnit eduCourseUnit);

    public List<EduCourseUnit> selectUnitsByCourseId(Long courseId);

    public List<EduCourseUnit> selectUnitsByChapterId(Long chapterId);

    public int insertEduCourseUnit(EduCourseUnit eduCourseUnit);

    public int updateEduCourseUnit(EduCourseUnit eduCourseUnit);

    public int deleteEduCourseUnitById(Long unitId);

    public int deleteEduCourseUnitByIds(Long[] unitIds);
}