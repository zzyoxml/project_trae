-- ============================================
-- 多语种在线教育平台数据库初始化脚本
-- 版本: 1.0.0
-- 创建日期: 2024-01-15
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS lingua_learn DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE lingua_learn;

-- ============================================
-- 1. 用户扩展信息表
-- ============================================
CREATE TABLE IF NOT EXISTS edu_user_profile (
    user_id BIGINT(20) NOT NULL COMMENT '用户ID',
    native_language VARCHAR(10) DEFAULT 'zh' COMMENT '母语',
    learning_languages VARCHAR(100) DEFAULT '' COMMENT '学习语言',
    proficiency_level VARCHAR(20) DEFAULT 'beginner' COMMENT '当前等级',
    total_study_time BIGINT(20) DEFAULT 0 COMMENT '总学习时间（分钟）',
    current_streak INT(11) DEFAULT 0 COMMENT '连续学习天数',
    longest_streak INT(11) DEFAULT 0 COMMENT '最长连续学习天数',
    last_study_date DATE COMMENT '最后学习日期',
    daily_goal INT(11) DEFAULT 30 COMMENT '每日目标（分钟）',
    preferred_learning_time VARCHAR(20) DEFAULT 'morning' COMMENT '最佳学习时段',
    voice_enabled TINYINT(1) DEFAULT 1 COMMENT '是否启用语音功能',
    avatar_url VARCHAR(255) COMMENT '头像URL',
    bio VARCHAR(500) COMMENT '个人简介',
    total_points INT(11) DEFAULT 0 COMMENT '总积分',
    level INT(11) DEFAULT 1 COMMENT '用户等级',
    experience_points INT(11) DEFAULT 0 COMMENT '经验值',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户扩展信息表';

-- ============================================
-- 2. 课程分类表
-- ============================================
CREATE TABLE IF NOT EXISTS edu_course_category (
    category_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    parent_id BIGINT(20) DEFAULT 0 COMMENT '父分类ID',
    category_name VARCHAR(50) NOT NULL COMMENT '分类名称',
    category_code VARCHAR(50) COMMENT '分类编码',
    display_order INT(11) DEFAULT 0 COMMENT '显示顺序',
    icon VARCHAR(100) COMMENT '分类图标',
    description VARCHAR(500) COMMENT '分类描述',
    status CHAR(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志',
    create_dept BIGINT(20) DEFAULT NULL,
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程分类表';

-- ============================================
-- 3. 课程表
-- ============================================
CREATE TABLE IF NOT EXISTS edu_course (
    course_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '课程ID',
    course_name VARCHAR(100) NOT NULL COMMENT '课程名称',
    course_code VARCHAR(50) COMMENT '课程编码',
    category_id BIGINT(20) COMMENT '分类ID',
    language VARCHAR(10) NOT NULL COMMENT '课程语言：en,ja,zh',
    level VARCHAR(20) NOT NULL COMMENT '课程等级',
    course_type VARCHAR(20) DEFAULT 'general' COMMENT '课程类型',
    description TEXT COMMENT '课程描述',
    cover_image VARCHAR(255) COMMENT '课程封面图',
    trailer_video VARCHAR(255) COMMENT '预告视频URL',
    teacher_id BIGINT(20) COMMENT '授课教师ID',
    total_duration INT(11) DEFAULT 0 COMMENT '总时长（分钟）',
    total_lessons INT(11) DEFAULT 0 COMMENT '总课时数',
    total_students INT(11) DEFAULT 0 COMMENT '学习人数',
    rating DECIMAL(3,2) DEFAULT 5.00 COMMENT '平均评分',
    rating_count INT(11) DEFAULT 0 COMMENT '评分次数',
    is_free TINYINT(1) DEFAULT 0 COMMENT '是否免费',
    price DECIMAL(10,2) DEFAULT 0.00 COMMENT '课程价格',
    is_published TINYINT(1) DEFAULT 0 COMMENT '是否发布',
    is_featured TINYINT(1) DEFAULT 0 COMMENT '是否精选',
    prerequisites VARCHAR(500) COMMENT '先修课程要求',
    learning_objectives TEXT COMMENT '学习目标',
    tags VARCHAR(500) COMMENT '标签',
    difficulty_score INT(11) DEFAULT 1 COMMENT '难度系数',
    popularity_score INT(11) DEFAULT 0 COMMENT '热度评分',
    create_dept BIGINT(20) DEFAULT NULL,
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志',
    PRIMARY KEY (course_id),
    INDEX idx_course_language (language),
    INDEX idx_course_level (level),
    INDEX idx_course_category (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- ============================================
-- 4. 课程单元表
-- ============================================
CREATE TABLE IF NOT EXISTS edu_course_unit (
    unit_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '单元ID',
    course_id BIGINT(20) NOT NULL COMMENT '课程ID',
    unit_name VARCHAR(100) NOT NULL COMMENT '单元名称',
    unit_order INT(11) DEFAULT 0 COMMENT '单元顺序',
    description TEXT COMMENT '单元描述',
    total_lessons INT(11) DEFAULT 0 COMMENT '课时数',
    total_duration INT(11) DEFAULT 0 COMMENT '总时长（分钟）',
    is_locked TINYINT(1) DEFAULT 0 COMMENT '是否锁定',
    passing_score INT(11) DEFAULT 60 COMMENT '及格分数',
    experience_reward INT(11) DEFAULT 50 COMMENT '经验奖励',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志',
    PRIMARY KEY (unit_id),
    INDEX idx_unit_course (course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程单元表';

-- ============================================
-- 5. 课时表
-- ============================================
CREATE TABLE IF NOT EXISTS edu_course_lesson (
    lesson_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '课时ID',
    unit_id BIGINT(20) NOT NULL COMMENT '单元ID',
    lesson_name VARCHAR(100) NOT NULL COMMENT '课时名称',
    lesson_order INT(11) DEFAULT 0 COMMENT '课时顺序',
    lesson_type VARCHAR(20) NOT NULL COMMENT '课时类型',
    content TEXT COMMENT '课时内容',
    duration INT(11) DEFAULT 0 COMMENT '时长（分钟）',
    is_free TINYINT(1) DEFAULT 0 COMMENT '是否免费试看',
    is_preview TINYINT(1) DEFAULT 0 COMMENT '是否预览',
    experience_reward INT(11) DEFAULT 10 COMMENT '经验奖励',
    coin_reward INT(11) DEFAULT 5 COMMENT '金币奖励',
    passing_score INT(11) DEFAULT 60 COMMENT '及格分数',
    max_attempts INT(11) DEFAULT 3 COMMENT '最大尝试次数',
    resources JSON COMMENT '课时资源',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志',
    PRIMARY KEY (lesson_id),
    INDEX idx_lesson_unit (unit_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课时表';

-- ============================================
-- 6. 单词表
-- ============================================
CREATE TABLE IF NOT EXISTS edu_vocabulary (
    vocab_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '单词ID',
    word VARCHAR(100) NOT NULL COMMENT '单词',
    language VARCHAR(10) NOT NULL COMMENT '语言',
    pronunciation VARCHAR(200) COMMENT '发音/音标',
    audio_url VARCHAR(255) COMMENT '音频URL',
    part_of_speech VARCHAR(20) COMMENT '词性',
    level VARCHAR(20) DEFAULT 'beginner' COMMENT '等级',
    definition TEXT COMMENT '释义',
    example_sentences JSON COMMENT '例句',
    synonyms JSON COMMENT '同义词',
    antonyms JSON COMMENT '反义词',
    word_family JSON COMMENT '词族',
    difficulty INT(11) DEFAULT 1 COMMENT '难度系数',
    frequency INT(11) DEFAULT 0 COMMENT '使用频率',
    tags VARCHAR(500) COMMENT '标签',
    image_url VARCHAR(255) COMMENT '配图URL',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志',
    PRIMARY KEY (vocab_id),
    UNIQUE KEY uk_word_language (word, language),
    INDEX idx_vocab_language (language),
    INDEX idx_vocab_level (level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='单词表';

-- ============================================
-- 7. 语法规则表
-- ============================================
CREATE TABLE IF NOT EXISTS edu_grammar (
    grammar_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '语法ID',
    grammar_name VARCHAR(100) NOT NULL COMMENT '语法名称',
    language VARCHAR(10) NOT NULL COMMENT '语言',
    level VARCHAR(20) DEFAULT 'beginner' COMMENT '等级',
    category VARCHAR(50) COMMENT '分类',
    description TEXT COMMENT '规则描述',
    rule_formula VARCHAR(500) COMMENT '规则公式',
    examples JSON COMMENT '例句',
    usage_notes TEXT COMMENT '使用注意事项',
    common_mistakes TEXT COMMENT '常见错误',
    related_grammar VARCHAR(500) COMMENT '相关语法',
    difficulty INT(11) DEFAULT 1 COMMENT '难度系数',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志',
    PRIMARY KEY (grammar_id),
    INDEX idx_grammar_language (language),
    INDEX idx_grammar_level (level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='语法规则表';

-- ============================================
-- 8. 学习记录表
-- ============================================
CREATE TABLE IF NOT EXISTS edu_learning_record (
    record_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    user_id BIGINT(20) NOT NULL COMMENT '用户ID',
    lesson_id BIGINT(20) COMMENT '课时ID',
    course_id BIGINT(20) COMMENT '课程ID',
    activity_type VARCHAR(30) NOT NULL COMMENT '活动类型',
    language VARCHAR(10) COMMENT '学习语言',
    duration INT(11) DEFAULT 0 COMMENT '学习时长',
    score INT(11) COMMENT '得分',
    max_score INT(11) COMMENT '最高得分',
    accuracy DECIMAL(5,2) COMMENT '正确率',
    xp_earned INT(11) DEFAULT 0 COMMENT '获得经验',
    coins_earned INT(11) DEFAULT 0 COMMENT '获得金币',
    completed TINYINT(1) DEFAULT 0 COMMENT '是否完成',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (record_id),
    INDEX idx_record_user (user_id),
    INDEX idx_record_lesson (lesson_id),
    INDEX idx_record_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习记录表';

-- ============================================
-- 9. 学习进度表
-- ============================================
CREATE TABLE IF NOT EXISTS edu_learning_progress (
    progress_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '进度ID',
    user_id BIGINT(20) NOT NULL COMMENT '用户ID',
    course_id BIGINT(20) NOT NULL COMMENT '课程ID',
    unit_id BIGINT(20) COMMENT '单元ID',
    lesson_id BIGINT(20) COMMENT '课时ID',
    status VARCHAR(20) DEFAULT 'not_started' COMMENT '状态',
    progress_percent INT(11) DEFAULT 0 COMMENT '完成进度百分比',
    best_score INT(11) DEFAULT 0 COMMENT '最高得分',
    attempt_count INT(11) DEFAULT 0 COMMENT '尝试次数',
    time_spent INT(11) DEFAULT 0 COMMENT '花费时间',
    last_study_time DATETIME COMMENT '最后学习时间',
    completed_time DATETIME COMMENT '完成时间',
    next_review_time DATETIME COMMENT '下次复习时间',
    mastery_level INT(11) DEFAULT 0 COMMENT '掌握程度',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (progress_id),
    UNIQUE KEY uk_user_lesson (user_id, lesson_id),
    INDEX idx_progress_user (user_id),
    INDEX idx_progress_course (course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习进度表';

-- ============================================
-- 10. 用户课程表
-- ============================================
CREATE TABLE IF NOT EXISTS edu_user_course (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT(20) NOT NULL COMMENT '用户ID',
    course_id BIGINT(20) NOT NULL COMMENT '课程ID',
    enrollment_date DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '报名日期',
    progress_percent INT(11) DEFAULT 0 COMMENT '课程完成百分比',
    status VARCHAR(20) DEFAULT 'learning' COMMENT '状态',
    current_lesson_id BIGINT(20) COMMENT '当前学习课时ID',
    start_date DATETIME COMMENT '开始学习日期',
    completion_date DATETIME COMMENT '完成日期',
    certificate_url VARCHAR(255) COMMENT '证书URL',
    is_favorite TINYINT(1) DEFAULT 0 COMMENT '是否收藏',
    rating INT(11) COMMENT '用户评分',
    review TEXT COMMENT '用户评价',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_course (user_id, course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户课程表';

-- ============================================
-- 11. 闯关天梯表
-- ============================================
CREATE TABLE IF NOT EXISTS edu_ladder_challenge (
    challenge_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '关卡ID',
    stage_id INT(11) NOT NULL COMMENT '关卡阶段ID',
    stage_name VARCHAR(50) NOT NULL COMMENT '关卡名称',
    stage_level INT(11) NOT NULL COMMENT '关卡等级',
    language VARCHAR(10) NOT NULL COMMENT '语言',
    challenge_type VARCHAR(30) NOT NULL COMMENT '挑战类型',
    difficulty INT(11) DEFAULT 1 COMMENT '难度',
    questions JSON COMMENT '题目',
    time_limit INT(11) DEFAULT 300 COMMENT '时间限制（秒）',
    passing_score INT(11) DEFAULT 60 COMMENT '及格分数',
    xp_reward INT(11) DEFAULT 100 COMMENT '经验奖励',
    coin_reward INT(11) DEFAULT 50 COMMENT '金币奖励',
    stars_3_score INT(11) DEFAULT 90 COMMENT '3星通关分数',
    stars_2_score INT(11) DEFAULT 75 COMMENT '2星通关分数',
    stars_1_score INT(11) DEFAULT 60 COMMENT '1星通关分数',
    is_unlocked TINYINT(1) DEFAULT 0 COMMENT '是否解锁',
    prerequisite_challenge_id BIGINT(20) COMMENT '前置关卡ID',
    avatar_url VARCHAR(255) COMMENT '关卡头像',
    description VARCHAR(500) COMMENT '关卡描述',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志',
    PRIMARY KEY (challenge_id),
    INDEX idx_ladder_language (language),
    INDEX idx_ladder_level (stage_level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='闯关天梯表';

-- ============================================
-- 12. 用户闯关记录表
-- ============================================
CREATE TABLE IF NOT EXISTS edu_user_challenge (
    record_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    user_id BIGINT(20) NOT NULL COMMENT '用户ID',
    challenge_id BIGINT(20) NOT NULL COMMENT '关卡ID',
    stage_level INT(11) NOT NULL COMMENT '关卡等级',
    language VARCHAR(10) NOT NULL COMMENT '语言',
    attempts INT(11) DEFAULT 0 COMMENT '尝试次数',
    best_score INT(11) DEFAULT 0 COMMENT '最高得分',
    best_stars INT(11) DEFAULT 0 COMMENT '最高星级',
    best_time INT(11) COMMENT '最佳用时（秒）',
    status VARCHAR(20) DEFAULT 'not_started' COMMENT '状态',
    last_attempt_time DATETIME COMMENT '最后尝试时间',
    completed_time DATETIME COMMENT '通关时间',
    xp_earned INT(11) DEFAULT 0 COMMENT '获得经验',
    coins_earned INT(11) DEFAULT 0 COMMENT '获得金币',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (record_id),
    UNIQUE KEY uk_user_challenge (user_id, challenge_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户闯关记录表';

-- ============================================
-- 13. 成就表
-- ============================================
CREATE TABLE IF NOT EXISTS edu_achievement (
    achievement_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '成就ID',
    achievement_code VARCHAR(50) NOT NULL COMMENT '成就代码',
    achievement_name VARCHAR(100) NOT NULL COMMENT '成就名称',
    achievement_type VARCHAR(30) NOT NULL COMMENT '成就类型',
    language VARCHAR(10) DEFAULT 'all' COMMENT '适用语言',
    description TEXT COMMENT '成就描述',
    icon_url VARCHAR(255) COMMENT '图标URL',
    tier VARCHAR(20) DEFAULT 'bronze' COMMENT '等级',
    requirement_type VARCHAR(30) COMMENT '需求类型',
    requirement_value INT(11) COMMENT '需求数值',
    xp_reward INT(11) DEFAULT 0 COMMENT '经验奖励',
    coin_reward INT(11) DEFAULT 0 COMMENT '金币奖励',
    badge_id BIGINT(20) COMMENT '关联徽章ID',
    is_active TINYINT(1) DEFAULT 1 COMMENT '是否启用',
    display_order INT(11) DEFAULT 0 COMMENT '显示顺序',
    rarity INT(11) DEFAULT 1 COMMENT '稀有度',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志',
    PRIMARY KEY (achievement_id),
    UNIQUE KEY uk_achievement_code (achievement_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成就表';

-- ============================================
-- 14. 用户成就表
-- ============================================
CREATE TABLE IF NOT EXISTS edu_user_achievement (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT(20) NOT NULL COMMENT '用户ID',
    achievement_id BIGINT(20) NOT NULL COMMENT '成就ID',
    progress INT(11) DEFAULT 0 COMMENT '当前进度',
    target_value INT(11) COMMENT '目标值',
    is_completed TINYINT(1) DEFAULT 0 COMMENT '是否完成',
    completed_time DATETIME COMMENT '完成时间',
    notification_sent TINYINT(1) DEFAULT 0 COMMENT '是否已发送通知',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_achievement (user_id, achievement_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户成就表';

-- ============================================
-- 15. 徽章表
-- ============================================
CREATE TABLE IF NOT EXISTS edu_badge (
    badge_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '徽章ID',
    badge_name VARCHAR(100) NOT NULL COMMENT '徽章名称',
    badge_type VARCHAR(30) NOT NULL COMMENT '徽章类型',
    description TEXT COMMENT '徽章描述',
    icon_url VARCHAR(255) COMMENT '图标URL',
    tier VARCHAR(20) DEFAULT 'bronze' COMMENT '等级',
    requirement_type VARCHAR(30) COMMENT '获取条件类型',
    requirement_value INT(11) COMMENT '获取条件值',
    xp_reward INT(11) DEFAULT 0 COMMENT '经验奖励',
    coin_reward INT(11) DEFAULT 0 COMMENT '金币奖励',
    is_active TINYINT(1) DEFAULT 1 COMMENT '是否启用',
    display_order INT(11) DEFAULT 0 COMMENT '显示顺序',
    rarity INT(11) DEFAULT 1 COMMENT '稀有度',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志',
    PRIMARY KEY (badge_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='徽章表';

-- ============================================
-- 16. 用户徽章表
-- ============================================
CREATE TABLE IF NOT EXISTS edu_user_badge (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT(20) NOT NULL COMMENT '用户ID',
    badge_id BIGINT(20) NOT NULL COMMENT '徽章ID',
    earned_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '获得时间',
    is_displayed TINYINT(1) DEFAULT 1 COMMENT '是否展示',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_badge (user_id, badge_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户徽章表';

-- ============================================
-- 17. 社区帖子表
-- ============================================
CREATE TABLE IF NOT EXISTS edu_post (
    post_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '帖子ID',
    user_id BIGINT(20) NOT NULL COMMENT '用户ID',
    post_type VARCHAR(30) NOT NULL COMMENT '帖子类型',
    language VARCHAR(10) DEFAULT 'all' COMMENT '相关语言',
    title VARCHAR(200) NOT NULL COMMENT '帖子标题',
    content TEXT NOT NULL COMMENT '帖子内容',
    images JSON COMMENT '图片URL列表',
    tags VARCHAR(500) COMMENT '标签',
    view_count INT(11) DEFAULT 0 COMMENT '浏览次数',
    like_count INT(11) DEFAULT 0 COMMENT '点赞次数',
    comment_count INT(11) DEFAULT 0 COMMENT '评论次数',
    is_pinned TINYINT(1) DEFAULT 0 COMMENT '是否置顶',
    is_featured TINYINT(1) DEFAULT 0 COMMENT '是否精选',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除',
    status VARCHAR(20) DEFAULT 'published' COMMENT '状态',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (post_id),
    INDEX idx_post_user (user_id),
    INDEX idx_post_language (language),
    INDEX idx_post_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社区帖子表';

-- ============================================
-- 18. 帖子评论表
-- ============================================
CREATE TABLE IF NOT EXISTS edu_post_comment (
    comment_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    post_id BIGINT(20) NOT NULL COMMENT '帖子ID',
    user_id BIGINT(20) NOT NULL COMMENT '用户ID',
    parent_comment_id BIGINT(20) COMMENT '父评论ID',
    content TEXT NOT NULL COMMENT '评论内容',
    like_count INT(11) DEFAULT 0 COMMENT '点赞次数',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (comment_id),
    INDEX idx_comment_post (post_id),
    INDEX idx_comment_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子评论表';

-- ============================================
-- 19. 用户点赞表
-- ============================================
CREATE TABLE IF NOT EXISTS edu_user_like (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT(20) NOT NULL COMMENT '用户ID',
    like_type VARCHAR(20) NOT NULL COMMENT '点赞类型',
    target_id BIGINT(20) NOT NULL COMMENT '目标ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_like (user_id, like_type, target_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户点赞表';

-- ============================================
-- 20. 学习路径推荐表
-- ============================================
CREATE TABLE IF NOT EXISTS edu_learning_path (
    path_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '路径ID',
    path_name VARCHAR(100) NOT NULL COMMENT '路径名称',
    language VARCHAR(10) NOT NULL COMMENT '语言',
    target_level VARCHAR(20) NOT NULL COMMENT '目标等级',
    start_level VARCHAR(20) NOT NULL COMMENT '起始等级',
    description TEXT COMMENT '路径描述',
    course_sequence JSON NOT NULL COMMENT '课程顺序',
    estimated_duration INT(11) COMMENT '预计时长（天）',
    prerequisites VARCHAR(500) COMMENT '前置要求',
    target_skills JSON COMMENT '目标技能',
    is_recommended TINYINT(1) DEFAULT 0 COMMENT '是否推荐',
    success_rate DECIMAL(5,2) COMMENT '成功率',
    enrollment_count INT(11) DEFAULT 0 COMMENT '报名人数',
    rating DECIMAL(3,2) DEFAULT 5.00 COMMENT '评分',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志',
    PRIMARY KEY (path_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习路径推荐表';

-- ============================================
-- 21. 用户学习路径表
-- ============================================
CREATE TABLE IF NOT EXISTS edu_user_learning_path (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT(20) NOT NULL COMMENT '用户ID',
    path_id BIGINT(20) NOT NULL COMMENT '路径ID',
    progress_percent INT(11) DEFAULT 0 COMMENT '完成进度',
    current_step INT(11) DEFAULT 1 COMMENT '当前步骤',
    status VARCHAR(20) DEFAULT 'in_progress' COMMENT '状态',
    start_time DATETIME COMMENT '开始时间',
    completion_time DATETIME COMMENT '完成时间',
    estimated_completion DATE COMMENT '预计完成日期',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_path (user_id, path_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户学习路径表';

-- ============================================
-- 22. 听力资源表
-- ============================================
CREATE TABLE IF NOT EXISTS edu_listening_resource (
    resource_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '资源ID',
    resource_name VARCHAR(100) NOT NULL COMMENT '资源名称',
    language VARCHAR(10) NOT NULL COMMENT '语言',
    level VARCHAR(20) NOT NULL COMMENT '等级',
    category VARCHAR(50) COMMENT '分类',
    audio_url VARCHAR(255) NOT NULL COMMENT '音频URL',
    duration INT(11) DEFAULT 0 COMMENT '时长（秒）',
    transcript TEXT COMMENT '原文文本',
    translation TEXT COMMENT '中文翻译',
    vocabulary_list JSON COMMENT '相关词汇列表',
    questions JSON COMMENT '练习题目',
    difficulty INT(11) DEFAULT 1 COMMENT '难度',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志',
    PRIMARY KEY (resource_id),
    INDEX idx_listening_language (language),
    INDEX idx_listening_level (level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='听力资源表';

-- ============================================
-- 23. 口语练习表
-- ============================================
CREATE TABLE IF NOT EXISTS edu_speaking_practice (
    practice_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '练习ID',
    practice_name VARCHAR(100) NOT NULL COMMENT '练习名称',
    language VARCHAR(10) NOT NULL COMMENT '语言',
    level VARCHAR(20) NOT NULL COMMENT '等级',
    practice_type VARCHAR(30) NOT NULL COMMENT '练习类型',
    prompt_text TEXT NOT NULL COMMENT '提示文本',
    prompt_audio VARCHAR(255) COMMENT '提示音频',
    reference_audio VARCHAR(255) COMMENT '参考答案音频',
    reference_transcript TEXT COMMENT '参考答案文本',
    evaluation_criteria JSON COMMENT '评分标准',
    difficulty INT(11) DEFAULT 1 COMMENT '难度',
    tips TEXT COMMENT '练习提示',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志',
    PRIMARY KEY (practice_id),
    INDEX idx_speaking_language (language),
    INDEX idx_speaking_level (level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='口语练习表';

-- ============================================
-- 初始化数据
-- ============================================

-- 插入课程分类
INSERT INTO edu_course_category (category_name, category_code, description, display_order) VALUES
('英语课程', 'en', '英语学习相关课程', 1),
('日语课程', 'ja', '日语学习相关课程', 2),
('汉语课程', 'zh', '汉语学习相关课程', 3);

-- 插入示例课程数据
INSERT INTO edu_course (course_name, language, level, description, cover_image, student_count, rating, is_featured, is_published, create_time) VALUES
('英语基础入门', 'en', 1, '适合零基础学习者，从字母、发音开始，逐步建立英语基础', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=English%20beginner%20course%20cover%20with%20ABC%20and%20book&image_size=square', 1258, 4.9, 1, 1, NOW()),
('英语日常会话', 'en', 2, '学习日常生活场景对话，提高口语表达能力', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=English%20conversation%20course%20cover%20with%20people%20talking&image_size=square', 986, 4.8, 1, 1, NOW()),
('日语五十音与基础对话', 'ja', 1, '从零开始学习日语发音和基础会话', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Japanese%20hiragana%20katakana%20course%20cover%20with%20sushi%20and%20cherry%20blossom&image_size=square', 1542, 4.9, 1, 1, NOW()),
('日语N3进阶课程', 'ja', 2, '系统学习日语N3级别语法和词汇', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Japanese%20N3%20course%20cover%20with%20Tokyo%20Tower&image_size=square', 623, 4.7, 1, 1, NOW()),
('汉语初级课程', 'zh', 1, '适合外籍学习者，从拼音开始学习中文', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Chinese%20beginner%20course%20cover%20with%20pinyin%20and%20Chinese%20characters&image_size=square', 854, 4.8, 1, 1, NOW()),
('HSK三级备考课程', 'zh', 2, 'HSK三级考试针对性辅导课程', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=HSK%20exam%20preparation%20course%20cover%20with%20certificate&image_size=square', 432, 4.6, 1, 1, NOW());

-- 插入示例用户数据（与系统用户关联）
INSERT INTO edu_user_profile (user_id, native_language, learning_languages, total_study_time, current_streak, total_points, level, experience_points, create_time) VALUES
(1, 'zh', 'en,ja', 360, 15, 2500, 5, 4500, NOW()),
(2, 'en', 'zh,ja', 180, 7, 1200, 3, 2200, NOW()),
(3, 'ja', 'en,zh', 120, 3, 800, 2, 1500, NOW());

-- 插入示例用户课程数据
INSERT INTO edu_user_course (user_id, course_id, progress_percent, status, enrollment_date) VALUES
(1, 1, 75, 'learning', DATE_SUB(NOW(), INTERVAL 30 DAY)),
(1, 3, 40, 'learning', DATE_SUB(NOW(), INTERVAL 20 DAY)),
(2, 5, 100, 'completed', DATE_SUB(NOW(), INTERVAL 60 DAY)),
(2, 2, 60, 'learning', DATE_SUB(NOW(), INTERVAL 15 DAY)),
(3, 1, 30, 'learning', DATE_SUB(NOW(), INTERVAL 10 DAY));

-- 插入示例用户成就数据
INSERT INTO edu_user_achievement (user_id, achievement_id, is_completed, completed_time) VALUES
(1, 1, 1, DATE_SUB(NOW(), INTERVAL 10 DAY)),
(1, 4, 1, DATE_SUB(NOW(), INTERVAL 8 DAY)),
(1, 6, 1, DATE_SUB(NOW(), INTERVAL 5 DAY)),
(2, 1, 1, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(3, 4, 1, DATE_SUB(NOW(), INTERVAL 2 DAY));

-- 插入示例社区帖子数据
INSERT INTO edu_post (user_id, post_type, language, title, content, view_count, like_count, comment_count, create_time) VALUES
(1, 'share', 'en', '我的英语学习心得分享', '学习英语已经半年了，想和大家分享一下我的学习方法。首先，每天坚持背30个单词，然后利用碎片化时间听英语新闻...', 325, 45, 12, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(2, 'question', 'zh', '请问中文的四声怎么练习？', '作为英语母语者，我觉得中文的四声很难掌握，有什么好的练习方法吗？求各位大佬指点！', 218, 32, 25, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(3, 'share', 'ja', '日语N3备考经验', '刚刚通过了日语N3考试，分享一下我的备考经验。重点是语法和词汇的积累...', 412, 68, 18, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(1, 'question', 'ja', '日语敬语太难了怎么办？', '敬语真的好复杂，有什么学习技巧吗？', 187, 25, 14, NOW());

-- 插入示例成就数据
INSERT INTO edu_achievement (achievement_code, achievement_name, achievement_type, description, tier, requirement_type, requirement_value, display_order) VALUES
('streak_7', '7天连续学习', 'streak', '连续学习7天', 'bronze', 'streak_days', 7, 1),
('streak_30', '30天连续学习', 'streak', '连续学习30天', 'silver', 'streak_days', 30, 2),
('streak_100', '100天连续学习', 'streak', '连续学习100天', 'gold', 'streak_days', 100, 3),
('study_time_60', '学习达人', 'study', '累计学习60分钟', 'bronze', 'total_study_time', 60, 4),
('study_time_600', '学习高手', 'study', '累计学习10小时', 'silver', 'total_study_time', 600, 5),
('vocab_100', '词汇初学者', 'study', '掌握100个单词', 'bronze', 'vocabulary_count', 100, 6),
('vocab_500', '词汇进阶者', 'study', '掌握500个单词', 'silver', 'vocabulary_count', 500, 7),
('vocab_1000', '词汇达人', 'study', '掌握1000个单词', 'gold', 'vocabulary_count', 1000, 8);

-- 插入示例徽章数据
INSERT INTO edu_badge (badge_name, badge_type, description, tier, requirement_type, requirement_value, display_order) VALUES
('初学者', 'master', '完成第一课', 'bronze', 'lessons_completed', 1, 1),
('学习新星', 'streak', '连续学习7天', 'bronze', 'streak_days', 7, 2),
('学习达人', 'score', '总分达到1000', 'silver', 'total_points', 1000, 3),
('闯关达人', 'score', '通关10个关卡', 'silver', 'challenges_passed', 10, 4),
('社区之星', 'social', '发帖被点赞100次', 'gold', 'total_likes_received', 100, 5);

SELECT 'Database initialization completed!' AS status;
