package com.ruoyi.edu.controller;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.edu.domain.EduPost;
import com.ruoyi.edu.domain.EduPostComment;
import com.ruoyi.edu.service.IEduCommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 社区Controller
 *
 * @author LingLearn
 */
@RestController
@RequestMapping("/edu/community")
public class EduCommunityController extends BaseController {

    @Autowired
    private IEduCommunityService communityService;

    /**
     * 查询帖子列表
     */
    @Anonymous
    @GetMapping("/post/list")
    public TableDataInfo postList(EduPost eduPost) {
        startPage();
        List<EduPost> list = communityService.selectEduPostList(eduPost);
        return getDataTable(list);
    }

    /**
     * 获取热门帖子
     */
    @Anonymous
    @GetMapping("/post/hot")
    public AjaxResult getHotPosts(@RequestParam(defaultValue = "10") Integer limit) {
        List<EduPost> posts = communityService.selectHotPosts(limit);
        return success(posts);
    }

    /**
     * 根据语言获取帖子
     */
    @Anonymous
    @GetMapping("/post/language/{language}")
    public TableDataInfo getPostsByLanguage(@PathVariable String language,
                                            @RequestParam(defaultValue = "20") Integer limit) {
        List<EduPost> list = communityService.selectEduPostByLanguage(language, limit);
        return getDataTable(list);
    }

    /**
     * 获取帖子详情
     */
    @Anonymous
    @GetMapping("/post/{postId}")
    public AjaxResult getPostDetails(@PathVariable Long postId) {
        EduPost post = communityService.selectEduPostById(postId);
        return success(post);
    }

    /**
     * 发布帖子
     */
    @PostMapping("/post")
    public AjaxResult publishPost(@RequestBody Map<String, String> params) {
        Long userId = SecurityUtils.getUserId();
        String postType = params.get("postType");
        String language = params.get("language");
        String title = params.get("title");
        String content = params.get("content");
        String tags = params.get("tags");

        Long postId = communityService.publishPost(userId, postType, language, title, content, tags);
        return success().put("postId", postId);
    }

    /**
     * 修改帖子
     */
    @PutMapping("/post")
    public AjaxResult updatePost(@RequestBody EduPost eduPost) {
        return toAjax(communityService.updateEduPost(eduPost));
    }

    /**
     * 删除帖子
     */
    @DeleteMapping("/post/{postId}")
    public AjaxResult deletePost(@PathVariable Long postId) {
        Long userId = SecurityUtils.getUserId();
        return toAjax(communityService.deleteEduPostById(postId, userId));
    }

    /**
     * 点赞帖子
     */
    @PostMapping("/post/{postId}/like")
    public AjaxResult likePost(@PathVariable Long postId) {
        Long userId = SecurityUtils.getUserId();
        boolean result = communityService.likePost(userId, postId);
        return success(result);
    }

    /**
     * 取消点赞帖子
     */
    @DeleteMapping("/post/{postId}/like")
    public AjaxResult unlikePost(@PathVariable Long postId) {
        Long userId = SecurityUtils.getUserId();
        boolean result = communityService.unlikePost(userId, postId);
        return success(result);
    }

    /**
     * 获取帖子评论
     */
    @Anonymous
    @GetMapping("/post/{postId}/comments")
    public AjaxResult getPostComments(@PathVariable Long postId) {
        Long userId = null;
        try {
            userId = SecurityUtils.getUserId();
        } catch (Exception e) {
            // 未登录用户
        }
        List<EduPostComment> comments = communityService.getPostComments(postId, userId);
        return success(comments);
    }

    /**
     * 添加评论
     */
    @PostMapping("/comment")
    public AjaxResult addComment(@RequestBody Map<String, Object> params) {
        Long userId = SecurityUtils.getUserId();
        Long postId = Long.parseLong(params.get("postId").toString());
        String content = params.get("content").toString();
        Long parentCommentId = params.containsKey("parentCommentId") ?
                Long.parseLong(params.get("parentCommentId").toString()) : null;

        Long commentId = communityService.addComment(postId, userId, content, parentCommentId);
        return success().put("commentId", commentId);
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/comment/{commentId}")
    public AjaxResult deleteComment(@PathVariable Long commentId) {
        Long userId = SecurityUtils.getUserId();
        return toAjax(communityService.deleteComment(commentId, userId));
    }

    /**
     * 点赞评论
     */
    @PostMapping("/comment/{commentId}/like")
    public AjaxResult likeComment(@PathVariable Long commentId) {
        Long userId = SecurityUtils.getUserId();
        boolean result = communityService.likeComment(userId, commentId);
        return success(result);
    }

    /**
     * 获取我的帖子
     */
    @GetMapping("/post/my")
    public TableDataInfo getMyPosts() {
        Long userId = SecurityUtils.getUserId();
        List<EduPost> list = communityService.selectEduPostByUserId(userId);
        return getDataTable(list);
    }

    /**
     * 获取用户帖子统计
     */
    @GetMapping("/post/stats")
    public AjaxResult getPostStats() {
        Long userId = SecurityUtils.getUserId();
        Map<String, Object> stats = communityService.getUserPostStats(userId);
        return success(stats);
    }
}
