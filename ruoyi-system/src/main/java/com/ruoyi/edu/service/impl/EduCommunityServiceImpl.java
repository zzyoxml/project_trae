package com.ruoyi.edu.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.edu.domain.EduPost;
import com.ruoyi.edu.domain.EduPostComment;
import com.ruoyi.edu.domain.EduUserLike;
import com.ruoyi.edu.mapper.EduPostMapper;
import com.ruoyi.edu.mapper.EduPostCommentMapper;
import com.ruoyi.edu.mapper.EduUserLikeMapper;
import com.ruoyi.edu.service.IEduCommunityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 社区服务实现
 *
 * @author LingLearn
 */
@Service
public class EduCommunityServiceImpl implements IEduCommunityService {

    private static final Logger log = LoggerFactory.getLogger(EduCommunityServiceImpl.class);

    @Autowired
    private EduPostMapper postMapper;

    @Autowired
    private EduPostCommentMapper commentMapper;

    @Autowired
    private EduUserLikeMapper likeMapper;

    /**
     * 查询帖子
     *
     * @param postId 帖子ID
     * @return 帖子
     */
    @Override
    public EduPost selectEduPostById(Long postId) {
        EduPost post = postMapper.selectEduPostById(postId);
        if (post != null) {
            // 增加浏览次数
            postMapper.incrementViewCount(postId);
        }
        return post;
    }

    /**
     * 查询帖子列表
     *
     * @param eduPost 帖子
     * @return 帖子列表
     */
    @Override
    public List<EduPost> selectEduPostList(EduPost eduPost) {
        return postMapper.selectEduPostList(eduPost);
    }

    /**
     * 根据语言查询帖子
     *
     * @param language 语言
     * @param limit    数量限制
     * @return 帖子列表
     */
    @Override
    public List<EduPost> selectEduPostByLanguage(String language, Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 20;
        }
        return postMapper.selectEduPostByLanguage(language, limit);
    }

    /**
     * 查询用户的帖子
     *
     * @param userId 用户ID
     * @return 帖子列表
     */
    @Override
    public List<EduPost> selectEduPostByUserId(Long userId) {
        return postMapper.selectEduPostByUserId(userId);
    }

    /**
     * 获取热门帖子
     *
     * @param limit 数量限制
     * @return 热门帖子列表
     */
    @Override
    public List<EduPost> selectHotPosts(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        return postMapper.selectHotPosts(limit);
    }

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
    @Override
    @Transactional
    public Long publishPost(Long userId, String postType, String language,
                           String title, String content, String tags) {
        if (StringUtils.isEmpty(title) || StringUtils.isEmpty(content)) {
            throw new ServiceException("标题和内容不能为空");
        }

        EduPost post = new EduPost();
        post.setUserId(userId);
        post.setPostType(postType != null ? postType : "discussion");
        post.setLanguage(language != null ? language : "all");
        post.setTitle(title);
        post.setContent(content);
        post.setTags(tags);
        post.setViewCount(0);
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setIsPinned(false);
        post.setIsFeatured(false);
        post.setIsDeleted(false);
        post.setStatus("published");

        postMapper.insertEduPost(post);

        log.info("发布帖子: userId={}, postId={}", userId, post.getPostId());
        return post.getPostId();
    }

    /**
     * 修改帖子
     *
     * @param eduPost 帖子
     * @return 结果
     */
    @Override
    public int updateEduPost(EduPost eduPost) {
        return postMapper.updateEduPost(eduPost);
    }

    /**
     * 删除帖子
     *
     * @param postId 帖子ID
     * @param userId 用户ID（用于验证）
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteEduPostById(Long postId, Long userId) {
        EduPost post = postMapper.selectEduPostById(postId);
        if (post == null) {
            throw new ServiceException("帖子不存在");
        }

        if (!post.getUserId().equals(userId)) {
            throw new ServiceException("无权删除此帖子");
        }

        post.setIsDeleted(true);
        return postMapper.updateEduPost(post);
    }

    /**
     * 点赞帖子
     *
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 结果
     */
    @Override
    @Transactional
    public boolean likePost(Long userId, Long postId) {
        // 检查是否已点赞
        EduUserLike existLike = likeMapper.selectUserLike(userId, "post", postId);
        if (existLike != null) {
            throw new ServiceException("已经点赞过");
        }

        // 创建点赞记录
        EduUserLike like = new EduUserLike();
        like.setUserId(userId);
        like.setLikeType("post");
        like.setTargetId(postId);

        likeMapper.insertEduUserLike(like);

        // 增加帖子点赞数
        postMapper.incrementLikeCount(postId);

        log.info("点赞帖子: userId={}, postId={}", userId, postId);
        return true;
    }

    /**
     * 取消点赞帖子
     *
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 结果
     */
    @Override
    @Transactional
    public boolean unlikePost(Long userId, Long postId) {
        // 检查是否存在点赞记录
        EduUserLike existLike = likeMapper.selectUserLike(userId, "post", postId);
        if (existLike == null) {
            throw new ServiceException("未点赞");
        }

        // 删除点赞记录
        likeMapper.deleteEduUserLike(userId, "post", postId);

        // 减少帖子点赞数
        postMapper.decrementLikeCount(postId);

        log.info("取消点赞帖子: userId={}, postId={}", userId, postId);
        return true;
    }

    /**
     * 获取帖子评论
     *
     * @param postId 帖子ID
     * @param userId 用户ID（可选）
     * @return 评论列表
     */
    @Override
    public List<EduPostComment> getPostComments(Long postId, Long userId) {
        List<EduPostComment> comments = commentMapper.selectCommentsByPostId(postId);

        // 标记用户已点赞的评论
        for (EduPostComment comment : comments) {
            if (userId != null) {
                EduUserLike like = likeMapper.selectUserLike(userId, "comment", comment.getCommentId());
                comment.setIsLiked(like != null);
            }

            // 获取子评论
            List<EduPostComment> replies = commentMapper.selectReplies(comment.getCommentId());
            comment.setReplies(replies);
            comment.setReplyCount(replies != null ? replies.size() : 0);
        }

        return comments;
    }

    /**
     * 添加评论
     *
     * @param postId          帖子ID
     * @param userId          用户ID
     * @param content         评论内容
     * @param parentCommentId 父评论ID（可选）
     * @return 评论ID
     */
    @Override
    @Transactional
    public Long addComment(Long postId, Long userId, String content, Long parentCommentId) {
        if (StringUtils.isEmpty(content)) {
            throw new ServiceException("评论内容不能为空");
        }

        EduPostComment comment = new EduPostComment();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setParentCommentId(parentCommentId);
        comment.setLikeCount(0);
        comment.setIsDeleted(false);

        commentMapper.insertEduPostComment(comment);

        // 增加帖子评论数
        postMapper.incrementCommentCount(postId);

        log.info("添加评论: userId={}, postId={}, commentId={}", userId, postId, comment.getCommentId());
        return comment.getCommentId();
    }

    /**
     * 删除评论
     *
     * @param commentId 评论ID
     * @param userId   用户ID（用于验证）
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteComment(Long commentId, Long userId) {
        EduPostComment comment = commentMapper.selectEduPostCommentById(commentId);
        if (comment == null) {
            throw new ServiceException("评论不存在");
        }

        if (!comment.getUserId().equals(userId)) {
            throw new ServiceException("无权删除此评论");
        }

        comment.setIsDeleted(true);
        return commentMapper.updateEduPostComment(comment);
    }

    /**
     * 点赞评论
     *
     * @param userId    用户ID
     * @param commentId 评论ID
     * @return 结果
     */
    @Override
    @Transactional
    public boolean likeComment(Long userId, Long commentId) {
        // 检查是否已点赞
        EduUserLike existLike = likeMapper.selectUserLike(userId, "comment", commentId);
        if (existLike != null) {
            throw new ServiceException("已经点赞过");
        }

        // 创建点赞记录
        EduUserLike like = new EduUserLike();
        like.setUserId(userId);
        like.setLikeType("comment");
        like.setTargetId(commentId);

        likeMapper.insertEduUserLike(like);

        // 增加评论点赞数
        commentMapper.incrementLikeCount(commentId);

        log.info("点赞评论: userId={}, commentId={}", userId, commentId);
        return true;
    }

    /**
     * 获取用户帖子统计
     *
     * @param userId 用户ID
     * @return 统计数据
     */
    @Override
    public Map<String, Object> getUserPostStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();

        List<EduPost> posts = postMapper.selectEduPostByUserId(userId);
        stats.put("totalPosts", posts.size());

        int totalViews = 0;
        int totalLikes = 0;
        int totalComments = 0;

        for (EduPost post : posts) {
            totalViews += post.getViewCount() != null ? post.getViewCount() : 0;
            totalLikes += post.getLikeCount() != null ? post.getLikeCount() : 0;
            totalComments += post.getCommentCount() != null ? post.getCommentCount() : 0;
        }

        stats.put("totalViews", totalViews);
        stats.put("totalLikes", totalLikes);
        stats.put("totalComments", totalComments);

        return stats;
    }
}
