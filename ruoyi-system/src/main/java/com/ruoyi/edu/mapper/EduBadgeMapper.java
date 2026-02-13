package com.ruoyi.edu.mapper;

import com.ruoyi.edu.domain.EduBadge;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EduBadgeMapper {

    public EduBadge selectEduBadgeById(Long badgeId);

    public List<EduBadge> selectEduBadgeList(EduBadge eduBadge);

    public int insertEduBadge(EduBadge eduBadge);

    public int updateEduBadge(EduBadge eduBadge);

    public int deleteEduBadgeById(Long badgeId);
}
