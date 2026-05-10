package com.ruoyi.edu.enums;

/**
 * 教育平台响应码枚举
 *
 * @author LingLearn
 */
public enum EduResultCode {

    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 失败
     */
    FAIL(500, "操作失败"),

    /**
     * 参数错误
     */
    PARAM_ERROR(400, "参数错误"),

    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权"),

    /**
     * 禁止访问
     */
    FORBIDDEN(403, "禁止访问"),

    /**
     * 资源不存在
     */
    NOT_FOUND(404, "资源不存在"),

    /**
     * 服务器内部错误
     */
    INTERNAL_ERROR(500, "服务器内部错误"),

    // ==================== 业务错误码 ====================

    /**
     * 用户不存在
     */
    USER_NOT_FOUND(1001, "用户不存在"),

    /**
     * 用户名已存在
     */
    USERNAME_EXISTS(1002, "用户名已存在"),

    /**
     * 邮箱已被注册
     */
    EMAIL_EXISTS(1003, "邮箱已被注册"),

    /**
     * 密码错误
     */
    PASSWORD_ERROR(1004, "密码错误"),

    /**
     * 用户已被停用
     */
    USER_DISABLED(1005, "用户已被停用"),

    /**
     * 课程不存在
     */
    COURSE_NOT_FOUND(2001, "课程不存在"),

    /**
     * 已报名该课程
     */
    COURSE_ALREADY_ENROLLED(2002, "您已报名该课程"),

    /**
     * 课时不存在
     */
    LESSON_NOT_FOUND(2003, "课时不存在"),

    /**
     * 课时未解锁
     */
    LESSON_LOCKED(2004, "课时未解锁，请先完成前面的课程"),

    /**
     * 学习记录不存在
     */
    RECORD_NOT_FOUND(3001, "学习记录不存在"),

    /**
     * 成绩不符合要求
     */
    SCORE_NOT_QUALIFIED(3002, "成绩不符合要求"),

    /**
     * 帖子不存在
     */
    POST_NOT_FOUND(4001, "帖子不存在"),

    /**
     * 无权删除此帖子
     */
    NO_PERMISSION_DELETE_POST(4002, "无权删除此帖子"),

    /**
     * 评论不存在
     */
    COMMENT_NOT_FOUND(4003, "评论不存在"),

    /**
     * 无权删除此评论
     */
    NO_PERMISSION_DELETE_COMMENT(4004, "无权删除此评论"),

    /**
     * 已经点赞过
     */
    ALREADY_LIKED(4005, "已经点赞过"),

    /**
     * 未点赞
     */
    NOT_LIKED(4006, "未点赞"),

    /**
     * 成就不存在
     */
    ACHIEVEMENT_NOT_FOUND(5001, "成就不存在"),

    /**
     * 成就已完成
     */
    ACHIEVEMENT_COMPLETED(5002, "成就已完成"),

    /**
     * 闯关不存在
     */
    CHALLENGE_NOT_FOUND(6001, "闯关不存在"),

    /**
     * 关卡未解锁
     */
    CHALLENGE_LOCKED(6002, "关卡未解锁"),

    /**
     * 闯关时间已过期
     */
    CHALLENGE_EXPIRED(6003, "闯关时间已过期");

    private final int code;
    private final String message;

    EduResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
