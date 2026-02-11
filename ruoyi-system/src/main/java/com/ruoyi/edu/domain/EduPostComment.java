package com.ruoyi.edu.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 帖子评论表 edu_post_comment
 *
 * @author LingLearn
 */
public class EduPostComment extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 评论ID
     */
    private Long commentId;

    /**
     * 帖子ID
     */
    private Long postId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 父评论ID（用于回复）
     */
    private Long parentCommentId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 点赞次数
     */
    private Integer likeCount;

    /**
     * 是否删除
     */
    private Boolean isDeleted;

    /**
     * 用户名（非数据库字段）
     */
    private String username;

    /**
     * 用户头像（非数据库字段）
     */
    private String userAvatar;

    /**
     * 子评论数量（非数据库字段）
     */
    private Integer replyCount;

    /**
     * 是否已点赞（非数据库字段）
     */
    private Boolean isLiked;

    /**
     * 子评论列表（非数据库字段）
     */
    private java.util.List<EduPostComment> replies;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public Integer getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }

    public Boolean getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(Boolean isLiked) {
        this.isLiked = isLiked;
    }

    public java.util.List<EduPostComment> getReplies() {
        return replies;
    }

    public void setReplies(java.util.List<EduPostComment> replies) {
        this.replies = replies;
    }

    @Override
    public String toString() {
        return "EduPostComment{" +
                "commentId=" + commentId +
                ", postId=" + postId +
                ", userId=" + userId +
                ", parentCommentId=" + parentCommentId +
                ", content='" + content + '\'' +
                ", likeCount=" + likeCount +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
