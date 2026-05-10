package com.ruoyi.edu.service;

import com.ruoyi.edu.domain.EduPost;
import com.ruoyi.edu.domain.EduPostComment;

import java.util.List;
import java.util.Map;

/**
 * 社区服务Service接口
 *
 * @author LingLearn
 */
public interface IEduCommunityService {

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
    public List<EduPost> selectEduPostByLanguage(String language, Integer limit);

    /**
     * 查询用户的帖子
     *
     * @param userId 用户ID
     * @return 帖子列表
     */
    public List<EduPost> selectEduPostByUserId(Long userId);

    /**
     * 获取热门帖子
     *
     * @param limit 数量限制
     * @return 热门帖子列表
     */
    public List<EduPost> selectHotPosts(Integer limit);

    /**
     * 发布帖子
     *
     * @param userId    用户ID
     * @param postType  帖子类型
     * @param language  语言
     * @param title     标题
     * @param content   内容
     * @param tags      标签
     * @return 帖子ID
     */
    public Long publishPost(Long userId, String postType, String language,
                           String title, String content, String tags);

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
     * @param userId 用户ID（用于验证）
     * @return 结果
     */
    public int deleteEduPostById(Long postId, Long userId);

    /**
     * 点赞帖子
     *
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 结果
     */
    public boolean likePost(Long userId, Long postId);

    /**
     * 取消点赞帖子
     *
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 结果
     */
    public boolean unlikePost(Long userId, Long postId);

    /**
     * 获取帖子评论
     *
     * @param postId 帖子ID
     * @param userId 用户ID（可选）
     * @return 评论列表
     */
    public List<EduPostComment> getPostComments(Long postId, Long userId);

    /**
     * 添加评论
     *
     * @param postId          帖子ID
     * @param userId          用户ID
     * @param content         评论内容
     * @param parentCommentId 父评论ID（可选）
     * @return 评论ID
     */
    public Long addComment(Long postId, Long userId, String content, Long parentCommentId);

    /**
     * 删除评论
     *
     * @param commentId 评论ID
     * @param userId   用户ID（用于验证）
     * @return 结果
     */
    public int deleteComment(Long commentId, Long userId);

    /**
     * 点赞评论
     *
     * @param userId    用户ID
     * @param commentId 评论ID
     * @return 结果
     */
    public boolean likeComment(Long userId, Long commentId);

    /**
     * 获取用户帖子统计
     *
     * @param userId 用户ID
     * @return 统计数据
     */
    public Map<String, Object> getUserPostStats(Long userId);
}
