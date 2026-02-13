package com.ruoyi.edu.mapper;

import com.ruoyi.edu.domain.EduUserBadge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EduUserBadgeMapper {

    public EduUserBadge selectUserBadge(@Param("userId") Long userId, @Param("badgeId") Long badgeId);

    public List<EduUserBadge> selectUserBadgeList(Long userId);

    public int insertEduUserBadge(EduUserBadge userBadge);

    public int updateEduUserBadge(EduUserBadge userBadge);

    public int deleteUserBadge(@Param("userId") Long userId, @Param("badgeId") Long badgeId);
}
