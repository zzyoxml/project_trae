package com.ruoyi.edu.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.edu.mapper.EduPostMapper;
import com.ruoyi.edu.domain.EduPost;
import com.ruoyi.edu.service.IEduPostService;
import com.ruoyi.common.core.text.Convert;

/**
 * 社区帖子Service业务层处理
 * 
 * @author LingLearn
 */
@Service
public class EduPostServiceImpl implements IEduPostService {
    @Autowired
    private EduPostMapper eduPostMapper;

    /**
     * 查询帖子
     * 
     * @param postId 帖子ID
     * @return 帖子
     */
    @Override
    public EduPost selectEduPostById(Long postId) {
        return eduPostMapper.selectEduPostById(postId);
    }

    /**
     * 查询帖子列表
     * 
     * @param eduPost 帖子
     * @return 帖子集合
     */
    @Override
    public List<EduPost> selectEduPostList(EduPost eduPost) {
        return eduPostMapper.selectEduPostList(eduPost);
    }

    /**
     * 新增帖子
     * 
     * @param eduPost 帖子
     * @return 结果
     */
    @Override
    public int insertEduPost(EduPost eduPost) {
        return eduPostMapper.insertEduPost(eduPost);
    }

    /**
     * 修改帖子
     * 
     * @param eduPost 帖子
     * @return 结果
     */
    @Override
    public int updateEduPost(EduPost eduPost) {
        return eduPostMapper.updateEduPost(eduPost);
    }

    /**
     * 批量删除帖子
     * 
     * @param postIds 需要删除的帖子ID
     * @return 结果
     */
    @Override
    public int deleteEduPostByIds(Long[] postIds) {
        return eduPostMapper.deleteEduPostByIds(postIds);
    }

    /**
     * 删除帖子信息
     * 
     * @param postId 帖子ID
     * @return 结果
     */
    @Override
    public int deleteEduPostById(Long postId) {
        return eduPostMapper.deleteEduPostById(postId);
    }

    /**
     * 审核帖子
     * 
     * @param postId 帖子ID
     * @param status 状态
     * @return 结果
     */
    @Override
    public int auditPost(Long postId, String status) {
        EduPost post = new EduPost();
        post.setPostId(postId);
        post.setStatus(status);
        return eduPostMapper.updateEduPost(post);
    }
}