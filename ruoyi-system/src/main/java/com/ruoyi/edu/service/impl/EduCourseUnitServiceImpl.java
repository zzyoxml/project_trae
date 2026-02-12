package com.ruoyi.edu.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.edu.domain.EduCourseUnit;
import com.ruoyi.edu.mapper.EduCourseUnitMapper;
import com.ruoyi.edu.service.IEduCourseUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EduCourseUnitServiceImpl implements IEduCourseUnitService {

    private static final Logger log = LoggerFactory.getLogger(EduCourseUnitServiceImpl.class);

    @Autowired
    private EduCourseUnitMapper eduCourseUnitMapper;

    @Override
    public EduCourseUnit selectEduCourseUnitById(Long unitId) {
        if (unitId == null) {
            throw new ServiceException("单元ID不能为空");
        }
        return eduCourseUnitMapper.selectEduCourseUnitById(unitId);
    }

    @Override
    public List<EduCourseUnit> selectEduCourseUnitList(EduCourseUnit eduCourseUnit) {
        return eduCourseUnitMapper.selectEduCourseUnitList(eduCourseUnit);
    }

    @Override
    public List<EduCourseUnit> selectUnitsByCourseId(Long courseId) {
        if (courseId == null) {
            throw new ServiceException("课程ID不能为空");
        }
        return eduCourseUnitMapper.selectUnitsByCourseId(courseId);
    }

    @Override
    public int insertEduCourseUnit(EduCourseUnit eduCourseUnit) {
        if (StringUtils.isEmpty(eduCourseUnit.getUnitName())) {
            throw new ServiceException("单元名称不能为空");
        }
        if (eduCourseUnit.getCourseId() == null) {
            throw new ServiceException("所属课程不能为空");
        }
        return eduCourseUnitMapper.insertEduCourseUnit(eduCourseUnit);
    }

    @Override
    public int updateEduCourseUnit(EduCourseUnit eduCourseUnit) {
        if (eduCourseUnit.getUnitId() == null) {
            throw new ServiceException("单元ID不能为空");
        }
        return eduCourseUnitMapper.updateEduCourseUnit(eduCourseUnit);
    }

    @Override
    public int deleteEduCourseUnitById(Long unitId) {
        if (unitId == null) {
            throw new ServiceException("单元ID不能为空");
        }
        return eduCourseUnitMapper.deleteEduCourseUnitById(unitId);
    }

    @Override
    public int deleteEduCourseUnitByIds(Long[] unitIds) {
        if (unitIds == null || unitIds.length == 0) {
            throw new ServiceException("请选择要删除的单元");
        }
        return eduCourseUnitMapper.deleteEduCourseUnitByIds(unitIds);
    }
}