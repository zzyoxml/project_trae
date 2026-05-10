package com.ruoyi.edu.mapper;

import com.ruoyi.edu.domain.EduPostComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 帖子评论Mapper接口
 *
 * @author LingLearn
 */
@Mapper
public interface EduPostCommentMapper {

    /**
     * 查询评论
     *
     * @param commentId 评论ID
     * @return 评论
     */
    public EduPostComment selectEduPostCommentById(Long commentId);

    /**
     * 查询帖子的评论
     *
     * @param postId 帖子ID
     * @return 评论列表
     */
    public List<EduPostComment> selectCommentsByPostId(Long postId);

    /**
     * 查询回复
     *
     * @param parentCommentId 父评论ID
     * @return 回复列表
     */
    public List<EduPostComment> selectReplies(@Param("parentCommentId") Long parentCommentId);

    /**
     * 新增评论
     *
     * @param eduPostComment 评论
     * @return 结果
     */
    public int insertEduPostComment(EduPostComment eduPostComment);

    /**
     * 修改评论
     *
     * @param eduPostComment 评论
     * @return 结果
     */
    public int updateEduPostComment(EduPostComment eduPostComment);

    /**
     * 删除评论
     *
     * @param commentId 评论ID
     * @return 结果
     */
    public int deleteEduPostCommentById(Long commentId);

    /**
     * 增加点赞次数
     *
     * @param commentId 评论ID
     * @return 结果
     */
    public int incrementLikeCount(Long commentId);
}
