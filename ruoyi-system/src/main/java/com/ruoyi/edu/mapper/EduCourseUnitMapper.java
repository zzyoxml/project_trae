package com.ruoyi.edu.mapper;

import com.ruoyi.edu.domain.EduCourseUnit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EduCourseUnitMapper {

    public EduCourseUnit selectEduCourseUnitById(Long unitId);

    public List<EduCourseUnit> selectEduCourseUnitList(EduCourseUnit eduCourseUnit);

    public List<EduCourseUnit> selectUnitsByCourseId(Long courseId);

    public int insertEduCourseUnit(EduCourseUnit eduCourseUnit);

    public int updateEduCourseUnit(EduCourseUnit eduCourseUnit);

    public int deleteEduCourseUnitById(Long unitId);

    public int deleteEduCourseUnitByIds(Long[] unitIds);
}
