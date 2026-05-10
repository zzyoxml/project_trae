package com.ruoyi.edu.mapper;

import com.ruoyi.edu.domain.EduUserLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户点赞Mapper接口
 *
 * @author LingLearn
 */
@Mapper
public interface EduUserLikeMapper {

    /**
     * 查询用户点赞
     *
     * @param userId    用户ID
     * @param likeType  点赞类型
     * @param targetId  目标ID
     * @return 用户点赞
     */
    public EduUserLike selectUserLike(
            @Param("userId") Long userId,
            @Param("likeType") String likeType,
            @Param("targetId") Long targetId);

    /**
     * 新增点赞
     *
     * @param eduUserLike 点赞
     * @return 结果
     */
    public int insertEduUserLike(EduUserLike eduUserLike);

    /**
     * 删除点赞
     *
     * @param userId    用户ID
     * @param likeType  点赞类型
     * @param targetId  目标ID
     * @return 结果
     */
    public int deleteEduUserLike(
            @Param("userId") Long userId,
            @Param("likeType") String likeType,
            @Param("targetId") Long targetId);

    /**
     * 查询用户点赞列表
     *
     * @param userId   用户ID
     * @param likeType 点赞类型
     * @return 点赞列表
     */
    public java.util.List<EduUserLike> selectUserLikes(
            @Param("userId") Long userId,
            @Param("likeType") String likeType);
}
