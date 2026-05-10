package com.ruoyi.edu.mapper;

import com.ruoyi.edu.domain.EduPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 社区帖子Mapper接口
 *
 * @author LingLearn
 */
@Mapper
public interface EduPostMapper {

    /**
     * 查询帖子
     *
     * @param postId 帖子ID
     * @return 帖子
     */
    public EduPost selectEduPostById(Long postId);

    /**
     * 查询帖子列表
     *
     * @param eduPost 帖子
     * @return 帖子列表
     */
    public List<EduPost> selectEduPostList(EduPost eduPost);

    /**
     * 根据语言查询帖子
     *
     * @param language 语言
     * @param limit    数量限制
     * @return 帖子列表
     */
    public List<EduPost> selectEduPostByLanguage(@Param("language") String language, @Param("limit") Integer limit);

    /**
     * 查询用户的帖子
     *
     * @param userId 用户ID
     * @return 帖子列表
     */
    public List<EduPost> selectEduPostByUserId(Long userId);

    /**
     * 查询热门帖子
     *
     * @param limit 数量限制
     * @return 热门帖子列表
     */
    public List<EduPost> selectHotPosts(@Param("limit") Integer limit);

    /**
     * 新增帖子
     *
     * @param eduPost 帖子
     * @return 结果
     */
    public int insertEduPost(EduPost eduPost);

    /**
     * 修改帖子
     *
     * @param eduPost 帖子
     * @return 结果
     */
    public int updateEduPost(EduPost eduPost);

    /**
     * 删除帖子
     *
     * @param postId 帖子ID
     * @return 结果
     */
    public int deleteEduPostById(Long postId);

    /**
     * 批量删除帖子
     *
     * @param postIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteEduPostByIds(Long[] postIds);

    /**
     * 增加浏览次数
     *
     * @param postId 帖子ID
     * @return 结果
     */
    public int incrementViewCount(Long postId);

    /**
     * 增加点赞次数
     *
     * @param postId 帖子ID
     * @return 结果
     */
    public int incrementLikeCount(Long postId);

    /**
     * 减少点赞次数
     *
     * @param postId 帖子ID
     * @return 结果
     */
    public int decrementLikeCount(Long postId);

    /**
     * 增加评论次数
     *
     * @param postId 帖子ID
     * @return 结果
     */
    public int incrementCommentCount(Long postId);
}
