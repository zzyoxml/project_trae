package com.ruoyi.edu.service;

import java.util.List;
import com.ruoyi.edu.domain.EduPost;

/**
 * 社区帖子Service接口
 * 
 * @author LingLearn
 */
public interface IEduPostService {
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
     * @return 帖子集合
     */
    public List<EduPost> selectEduPostList(EduPost eduPost);

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
     * 批量删除帖子
     * 
     * @param postIds 需要删除的帖子ID
     * @return 结果
     */
    public int deleteEduPostByIds(Long[] postIds);

    /**
     * 删除帖子信息
     * 
     * @param postId 帖子ID
     * @return 结果
     */
    public int deleteEduPostById(Long postId);

    /**
     * 审核帖子
     * 
     * @param postId 帖子ID
     * @param status 状态
     * @return 结果
     */
    public int auditPost(Long postId, String status);
}