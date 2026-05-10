# 多语种在线教育平台数据库设计

## 1. 数据库整体设计

### 1.1 核心模块
- **用户认证模块**：基于若依框架的用户表扩展
- **课程管理模块**：课程、单元、课时结构
- **学习资源模块**：单词、语法、听力、口语等资源
- **学习进度模块**：用户学习记录和进度追踪
- **社区交流模块**：帖子、评论、点赞
- **成就系统模块**：成就、徽章、排行榜
- **推荐系统模块**：学习路径推荐

### 1.2 支持语言
- 英语 (en)
- 日语 (ja)
- 汉语 (zh)

---

## 2. 数据库表结构

### 2.1 用户扩展表 (edu_user_profile)

```sql
-- 用户扩展信息表
CREATE TABLE edu_user_profile (
    user_id BIGINT(20) NOT NULL COMMENT '用户ID',
    native_language VARCHAR(10) DEFAULT 'zh' COMMENT '母语',
    learning_languages VARCHAR(100) DEFAULT '' COMMENT '学习语言，多个用逗号分隔',
    proficiency_level VARCHAR(10) DEFAULT 'beginner' COMMENT '当前等级：beginner,elementary,intermediate,advanced',
    total_study_time BIGINT(20) DEFAULT 0 COMMENT '总学习时间（分钟）',
    current_streak INT(11) DEFAULT 0 COMMENT '连续学习天数',
    longest_streak INT(11) DEFAULT 0 COMMENT '最长连续学习天数',
    last_study_date DATE COMMENT '最后学习日期',
    daily_goal INT(11) DEFAULT 30 COMMENT '每日目标（分钟）',
    preferred_learning_time VARCHAR(10) DEFAULT 'morning' COMMENT '最佳学习时段：morning,afternoon,evening',
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
```

### 2.2 课程分类表 (edu_course_category)

```sql
-- 课程分类表
CREATE TABLE edu_course_category (
    category_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    parent_id BIGINT(20) DEFAULT 0 COMMENT '父分类ID',
    category_name VARCHAR(50) NOT NULL COMMENT '分类名称',
    category_code VARCHAR(50) COMMENT '分类编码',
    display_order INT(11) DEFAULT 0 COMMENT '显示顺序',
    icon VARCHAR(100) COMMENT '分类图标',
    description VARCHAR(500) COMMENT '分类描述',
    status CHAR(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
    create_dept BIGINT(20) DEFAULT NULL,
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程分类表';
```

### 2.3 课程表 (edu_course)

```sql
-- 课程表
CREATE TABLE edu_course (
    course_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '课程ID',
    course_name VARCHAR(100) NOT NULL COMMENT '课程名称',
    course_code VARCHAR(50) COMMENT '课程编码',
    category_id BIGINT(20) COMMENT '分类ID',
    language VARCHAR(10) NOT NULL COMMENT '课程语言：en,ja,zh',
    level VARCHAR(20) NOT NULL COMMENT '课程等级：beginner,elementary,intermediate,advanced',
    course_type VARCHAR(20) DEFAULT 'general' COMMENT '课程类型：general,conversation,grammar,vocabulary,listening,speaking',
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
    tags VARCHAR(500) COMMENT '标签，多个用逗号分隔',
    difficulty_score INT(11) DEFAULT 1 COMMENT '难度系数（1-5）',
    popularity_score INT(11) DEFAULT 0 COMMENT '热度评分',
    create_dept BIGINT(20) DEFAULT NULL,
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- 添加索引
CREATE INDEX idx_course_language ON edu_course(language);
CREATE INDEX idx_course_level ON edu_course(level);
CREATE INDEX idx_course_category ON edu_course(category_id);
```

### 2.4 课程单元表 (edu_course_unit)

```sql
-- 课程单元表
CREATE TABLE edu_course_unit (
    unit_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '单元ID',
    course_id BIGINT(20) NOT NULL COMMENT '课程ID',
    unit_name VARCHAR(100) NOT NULL COMMENT '单元名称',
    unit_order INT(11) DEFAULT 0 COMMENT '单元顺序',
    description TEXT COMMENT '单元描述',
    total_lessons INT(11) DEFAULT 0 COMMENT '课时数',
    total_duration INT(11) DEFAULT 0 COMMENT '总时长（分钟）',
    is_locked TINYINT(1) DEFAULT 0 COMMENT '是否锁定（需完成前一单元）',
    passing_score INT(11) DEFAULT 60 COMMENT '及格分数',
    experience_reward INT(11) DEFAULT 50 COMMENT '经验奖励',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (unit_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程单元表';

CREATE INDEX idx_unit_course ON edu_course_unit(course_id);
```

### 2.5 课时表 (edu_course_lesson)

```sql
-- 课时表
CREATE TABLE edu_course_lesson (
    lesson_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '课时ID',
    unit_id BIGINT(20) NOT NULL COMMENT '单元ID',
    lesson_name VARCHAR(100) NOT NULL COMMENT '课时名称',
    lesson_order INT(11) DEFAULT 0 COMMENT '课时顺序',
    lesson_type VARCHAR(20) NOT NULL COMMENT '课时类型：vocabulary,grammar,listening,speaking,quiz,video',
    content TEXT COMMENT '课时内容（JSON格式）',
    duration INT(11) DEFAULT 0 COMMENT '时长（分钟）',
    is_free TINYINT(1) DEFAULT 0 COMMENT '是否免费试看',
    is_preview TINYINT(1) DEFAULT 0 COMMENT '是否预览',
    experience_reward INT(11) DEFAULT 10 COMMENT '经验奖励',
    coin_reward INT(11) DEFAULT 5 COMMENT '金币奖励',
    passing_score INT(11) DEFAULT 60 COMMENT '及格分数',
    max_attempts INT(11) DEFAULT 3 COMMENT '最大尝试次数',
    resources JSON COMMENT '课时资源（音频、视频、图片等）',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (lesson_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课时表';

CREATE INDEX idx_lesson_unit ON edu_course_lesson(unit_id);
```

### 2.6 单词表 (edu_vocabulary)

```sql
-- 单词表
CREATE TABLE edu_vocabulary (
    vocab_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '单词ID',
    word VARCHAR(100) NOT NULL COMMENT '单词',
    language VARCHAR(10) NOT NULL COMMENT '语言：en,ja,zh',
    pronunciation VARCHAR(200) COMMENT '发音/音标',
    audio_url VARCHAR(255) COMMENT '音频URL',
    part_of_speech VARCHAR(20) COMMENT '词性：noun,verb,adj,adv等',
    level VARCHAR(20) DEFAULT 'beginner' COMMENT '等级',
    definition TEXT COMMENT '释义',
    example_sentences JSON COMMENT '例句（数组格式）',
    synonyms JSON COMMENT '同义词',
    antonyms JSON COMMENT '反义词',
    word_family JSON COMMENT '词族（名词、动词、形容词等变形）',
    difficulty INT(11) DEFAULT 1 COMMENT '难度系数（1-5）',
    frequency INT(11) DEFAULT 0 COMMENT '使用频率',
    tags VARCHAR(500) COMMENT '标签',
    image_url VARCHAR(255) COMMENT '配图URL',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (vocab_id),
    UNIQUE KEY uk_word_language (word, language)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='单词表';

CREATE INDEX idx_vocab_language ON edu_vocabulary(language);
CREATE INDEX idx_vocab_level ON edu_vocabulary(level);
```

### 2.7 语法规则表 (edu_grammar)

```sql
-- 语法规则表
CREATE TABLE edu_grammar (
    grammar_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '语法ID',
    grammar_name VARCHAR(100) NOT NULL COMMENT '语法名称',
    language VARCHAR(10) NOT NULL COMMENT '语言：en,ja,zh',
    level VARCHAR(20) DEFAULT 'beginner' COMMENT '等级',
    category VARCHAR(50) COMMENT '分类：tense,structure,sentence等',
    description TEXT COMMENT '规则描述',
    rule_formula VARCHAR(500) COMMENT '规则公式',
    examples JSON COMMENT '例句（数组格式）',
    usage_notes TEXT COMMENT '使用注意事项',
    common_mistakes TEXT COMMENT '常见错误',
    related_grammar VARCHAR(500) COMMENT '相关语法',
    difficulty INT(11) DEFAULT 1 COMMENT '难度系数（1-5）',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (grammar_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='语法规则表';

CREATE INDEX idx_grammar_language ON edu_grammar(language);
CREATE INDEX idx_grammar_level ON edu_grammar(level);
```

### 2.8 学习记录表 (edu_learning_record)

```sql
-- 学习记录表
CREATE TABLE edu_learning_record (
    record_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    user_id BIGINT(20) NOT NULL COMMENT '用户ID',
    lesson_id BIGINT(20) COMMENT '课时ID',
    course_id BIGINT(20) COMMENT '课程ID',
    activity_type VARCHAR(30) NOT NULL COMMENT '活动类型：vocabulary,grammar,listening,speaking,quiz,practice',
    language VARCHAR(10) COMMENT '学习语言',
    duration INT(11) DEFAULT 0 COMMENT '学习时长（分钟）',
    score INT(11) COMMENT '得分',
    max_score INT(11) COMMENT '最高得分',
    accuracy DECIMAL(5,2) COMMENT '正确率',
    xp_earned INT(11) DEFAULT 0 COMMENT '获得经验',
    coins_earned INT(11) DEFAULT 0 COMMENT '获得金币',
    completed TINYINT(1) DEFAULT 0 COMMENT '是否完成',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (record_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习记录表';

CREATE INDEX idx_record_user ON edu_learning_record(user_id);
CREATE INDEX idx_record_lesson ON edu_learning_record(lesson_id);
CREATE INDEX idx_record_time ON edu_learning_record(create_time);
```

### 2.9 学习进度表 (edu_learning_progress)

```sql
-- 学习进度表
CREATE TABLE edu_learning_progress (
    progress_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '进度ID',
    user_id BIGINT(20) NOT NULL COMMENT '用户ID',
    course_id BIGINT(20) NOT NULL COMMENT '课程ID',
    unit_id BIGINT(20) COMMENT '单元ID',
    lesson_id BIGINT(20) COMMENT '课时ID',
    status VARCHAR(20) DEFAULT 'not_started' COMMENT '状态：not_started,in_progress,completed,locked',
    progress_percent INT(11) DEFAULT 0 COMMENT '完成进度百分比',
    best_score INT(11) DEFAULT 0 COMMENT '最高得分',
    attempt_count INT(11) DEFAULT 0 COMMENT '尝试次数',
    time_spent INT(11) DEFAULT 0 COMMENT '花费时间（分钟）',
    last_study_time DATETIME COMMENT '最后学习时间',
    completed_time DATETIME COMMENT '完成时间',
    next_review_time DATETIME COMMENT '下次复习时间',
    mastery_level INT(11) DEFAULT 0 COMMENT '掌握程度（0-100）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (progress_id),
    UNIQUE KEY uk_user_lesson (user_id, lesson_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习进度表';

CREATE INDEX idx_progress_user ON edu_learning_progress(user_id);
CREATE INDEX idx_progress_course ON edu_learning_progress(course_id);
```

### 2.10 用户课程表 (edu_user_course)

```sql
-- 用户课程表
CREATE TABLE edu_user_course (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT(20) NOT NULL COMMENT '用户ID',
    course_id BIGINT(20) NOT NULL COMMENT '课程ID',
    enrollment_date DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '报名日期',
    progress_percent INT(11) DEFAULT 0 COMMENT '课程完成百分比',
    status VARCHAR(20) DEFAULT 'learning' COMMENT '状态：enrolled,learning,completed,dropped',
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

CREATE INDEX idx_user_course_user ON edu_user_course(user_id);
CREATE INDEX idx_user_course_course ON edu_user_course(course_id);
```

### 2.11 闯关天梯表 (edu_ladder_challenge)

```sql
-- 闯关天梯表
CREATE TABLE edu_ladder_challenge (
    challenge_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '关卡ID',
    stage_id INT(11) NOT NULL COMMENT '关卡阶段ID',
    stage_name VARCHAR(50) NOT NULL COMMENT '关卡名称',
    stage_level INT(11) NOT NULL COMMENT '关卡等级（1-100）',
    language VARCHAR(10) NOT NULL COMMENT '语言',
    challenge_type VARCHAR(30) NOT NULL COMMENT '挑战类型：vocabulary,grammar,listening,speaking,mixed',
    difficulty INT(11) DEFAULT 1 COMMENT '难度（1-5）',
    questions JSON COMMENT '题目（JSON数组）',
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
    PRIMARY KEY (challenge_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='闯关天梯表';

CREATE INDEX idx_ladder_language ON edu_ladder_challenge(language);
CREATE INDEX idx_ladder_level ON edu_ladder_challenge(stage_level);
```

### 2.12 用户闯关记录表 (edu_user_challenge)

```sql
-- 用户闯关记录表
CREATE TABLE edu_user_challenge (
    record_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    user_id BIGINT(20) NOT NULL COMMENT '用户ID',
    challenge_id BIGINT(20) NOT NULL COMMENT '关卡ID',
    stage_level INT(11) NOT NULL COMMENT '关卡等级',
    language VARCHAR(10) NOT NULL COMMENT '语言',
    attempts INT(11) DEFAULT 0 COMMENT '尝试次数',
    best_score INT(11) DEFAULT 0 COMMENT '最高得分',
    best_stars INT(11) DEFAULT 0 COMMENT '最高星级（1-3）',
    best_time INT(11) COMMENT '最佳用时（秒）',
    status VARCHAR(20) DEFAULT 'not_started' COMMENT '状态：not_started,in_progress,passed,failed',
    last_attempt_time DATETIME COMMENT '最后尝试时间',
    completed_time DATETIME COMMENT '通关时间',
    xp_earned INT(11) DEFAULT 0 COMMENT '获得经验',
    coins_earned INT(11) DEFAULT 0 COMMENT '获得金币',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (record_id),
    UNIQUE KEY uk_user_challenge (user_id, challenge_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户闯关记录表';

CREATE INDEX idx_user_challenge_user ON edu_user_challenge(user_id);
CREATE INDEX idx_user_challenge_lang ON edu_user_challenge(language);
```

### 2.13 成就表 (edu_achievement)

```sql
-- 成就表
CREATE TABLE edu_achievement (
    achievement_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '成就ID',
    achievement_code VARCHAR(50) NOT NULL COMMENT '成就代码',
    achievement_name VARCHAR(100) NOT NULL COMMENT '成就名称',
    achievement_type VARCHAR(30) NOT NULL COMMENT '成就类型：study,streak,score,social,collection',
    language VARCHAR(10) DEFAULT 'all' COMMENT '适用语言',
    description TEXT COMMENT '成就描述',
    icon_url VARCHAR(255) COMMENT '图标URL',
    tier VARCHAR(20) DEFAULT 'bronze' COMMENT '等级：bronze,silver,gold,diamond,legendary',
    requirement_type VARCHAR(30) COMMENT '需求类型',
    requirement_value INT(11) COMMENT '需求数值',
    xp_reward INT(11) DEFAULT 0 COMMENT '经验奖励',
    coin_reward INT(11) DEFAULT 0 COMMENT '金币奖励',
    badge_id BIGINT(20) COMMENT '关联徽章ID',
    is_active TINYINT(1) DEFAULT 1 COMMENT '是否启用',
    display_order INT(11) DEFAULT 0 COMMENT '显示顺序',
    rarity INT(11) DEFAULT 1 COMMENT '稀有度（1-5）',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志',
    PRIMARY KEY (achievement_id),
    UNIQUE KEY uk_achievement_code (achievement_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成就表';
```

### 2.14 用户成就表 (edu_user_achievement)

```sql
-- 用户成就表
CREATE TABLE edu_user_achievement (
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

CREATE INDEX idx_user_achievement_user ON edu_user_achievement(user_id);
```

### 2.15 徽章表 (edu_badge)

```sql
-- 徽章表
CREATE TABLE edu_badge (
    badge_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '徽章ID',
    badge_name VARCHAR(100) NOT NULL COMMENT '徽章名称',
    badge_type VARCHAR(30) NOT NULL COMMENT '徽章类型：streak,score,social,master,rare',
    description TEXT COMMENT '徽章描述',
    icon_url VARCHAR(255) COMMENT '图标URL',
    tier VARCHAR(20) DEFAULT 'bronze' COMMENT '等级',
    requirement_type VARCHAR(30) COMMENT '获取条件类型',
    requirement_value INT(11) COMMENT '获取条件值',
    xp_reward INT(11) DEFAULT 0 COMMENT '经验奖励',
    coin_reward INT(11) DEFAULT 0 COMMENT '金币奖励',
    is_active TINYINT(1) DEFAULT 1 COMMENT '是否启用',
    display_order INT(11) DEFAULT 0 COMMENT '显示顺序',
    rarity INT(11) DEFAULT 1 COMMENT '稀有度（1-5）',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志',
    PRIMARY KEY (badge_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='徽章表';
```

### 2.16 用户徽章表 (edu_user_badge)

```sql
-- 用户徽章表
CREATE TABLE edu_user_badge (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT(20) NOT NULL COMMENT '用户ID',
    badge_id BIGINT(20) NOT NULL COMMENT '徽章ID',
    earned_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '获得时间',
    is_displayed TINYINT(1) DEFAULT 1 COMMENT '是否展示',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_badge (user_id, badge_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户徽章表';

CREATE INDEX idx_user_badge_user ON edu_user_badge(user_id);
```

### 2.17 社区帖子表 (edu_post)

```sql
-- 社区帖子表
CREATE TABLE edu_post (
    post_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '帖子ID',
    user_id BIGINT(20) NOT NULL COMMENT '用户ID',
    post_type VARCHAR(30) NOT NULL COMMENT '帖子类型：discussion,question,tip,share,achievement',
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
    status VARCHAR(20) DEFAULT 'published' COMMENT '状态：draft,published,hidden',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (post_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社区帖子表';

CREATE INDEX idx_post_user ON edu_post(user_id);
CREATE INDEX idx_post_language ON edu_post(language);
CREATE INDEX idx_post_create_time ON edu_post(create_time);
```

### 2.18 帖子评论表 (edu_post_comment)

```sql
-- 帖子评论表
CREATE TABLE edu_post_comment (
    comment_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    post_id BIGINT(20) NOT NULL COMMENT '帖子ID',
    user_id BIGINT(20) NOT NULL COMMENT '用户ID',
    parent_comment_id BIGINT(20) COMMENT '父评论ID（用于回复）',
    content TEXT NOT NULL COMMENT '评论内容',
    like_count INT(11) DEFAULT 0 COMMENT '点赞次数',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (comment_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子评论表';

CREATE INDEX idx_comment_post ON edu_post_comment(post_id);
CREATE INDEX idx_comment_user ON edu_post_comment(user_id);
```

### 2.19 用户点赞表 (edu_user_like)

```sql
-- 用户点赞表
CREATE TABLE edu_user_like (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT(20) NOT NULL COMMENT '用户ID',
    like_type VARCHAR(20) NOT NULL COMMENT '点赞类型：post,comment',
    target_id BIGINT(20) NOT NULL COMMENT '目标ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_like (user_id, like_type, target_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户点赞表';

CREATE INDEX idx_like_user ON edu_user_like(user_id);
```

### 2.20 听力资源表 (edu_listening_resource)

```sql
-- 听力资源表
CREATE TABLE edu_listening_resource (
    resource_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '资源ID',
    resource_name VARCHAR(100) NOT NULL COMMENT '资源名称',
    language VARCHAR(10) NOT NULL COMMENT '语言',
    level VARCHAR(20) NOT NULL COMMENT '等级',
    category VARCHAR(50) COMMENT '分类：dialogue,story,news,podcast',
    audio_url VARCHAR(255) NOT NULL COMMENT '音频URL',
    duration INT(11) DEFAULT 0 COMMENT '时长（秒）',
    transcript TEXT COMMENT '原文文本',
    translation TEXT COMMENT '中文翻译',
    vocabulary_list JSON COMMENT '相关词汇列表',
    questions JSON COMMENT '练习题目',
    difficulty INT(11) DEFAULT 1 COMMENT '难度（1-5）',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志',
    PRIMARY KEY (resource_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='听力资源表';

CREATE INDEX idx_listening_language ON edu_listening_resource(language);
CREATE INDEX idx_listening_level ON edu_listening_resource(level);
```

### 2.21 口语练习表 (edu_speaking_practice)

```sql
-- 口语练习表
CREATE TABLE edu_speaking_practice (
    practice_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '练习ID',
    practice_name VARCHAR(100) NOT NULL COMMENT '练习名称',
    language VARCHAR(10) NOT NULL COMMENT '语言',
    level VARCHAR(20) NOT NULL COMMENT '等级',
    practice_type VARCHAR(30) NOT NULL COMMENT '练习类型：pronunciation,intonation,dialogue,monologue',
    prompt_text TEXT NOT NULL COMMENT '提示文本',
    prompt_audio VARCHAR(255) COMMENT '提示音频',
    reference_audio VARCHAR(255) COMMENT '参考答案音频',
    reference_transcript TEXT COMMENT '参考答案文本',
    evaluation_criteria JSON COMMENT '评分标准',
    difficulty INT(11) DEFAULT 1 COMMENT '难度（1-5）',
    tips TEXT COMMENT '练习提示',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志',
    PRIMARY KEY (practice_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='口语练习表';

CREATE INDEX idx_speaking_language ON edu_speaking_practice(language);
CREATE INDEX idx_speaking_level ON edu_speaking_practice(level);
```

### 2.22 学习路径推荐表 (edu_learning_path)

```sql
-- 学习路径推荐表
CREATE TABLE edu_learning_path (
    path_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '路径ID',
    path_name VARCHAR(100) NOT NULL COMMENT '路径名称',
    language VARCHAR(10) NOT NULL COMMENT '语言',
    target_level VARCHAR(20) NOT NULL COMMENT '目标等级',
    start_level VARCHAR(20) NOT NULL COMMENT '起始等级',
    description TEXT COMMENT '路径描述',
    course_sequence JSON NOT NULL COMMENT '课程顺序（课程ID数组）',
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

CREATE INDEX idx_path_language ON edu_learning_path(language);
```

### 2.23 用户学习路径表 (edu_user_learning_path)

```sql
-- 用户学习路径表
CREATE TABLE edu_user_learning_path (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT(20) NOT NULL COMMENT '用户ID',
    path_id BIGINT(20) NOT NULL COMMENT '路径ID',
    progress_percent INT(11) DEFAULT 0 COMMENT '完成进度',
    current_step INT(11) DEFAULT 1 COMMENT '当前步骤',
    status VARCHAR(20) DEFAULT 'in_progress' COMMENT '状态：not_started,in_progress,completed',
    start_time DATETIME COMMENT '开始时间',
    completion_time DATETIME COMMENT '完成时间',
    estimated_completion DATE COMMENT '预计完成日期',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_path (user_id, path_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户学习路径表';

CREATE INDEX idx_user_path_user ON edu_user_learning_path(user_id);
```

---

## 3. 索引策略

### 3.1 常用查询索引
- 用户学习进度：`idx_progress_user(user_id)`
- 课程查询：`idx_course_language(language)`, `idx_course_level(level)`
- 闯关记录：`idx_user_challenge_lang(language)`
- 社区帖子：`idx_post_language(language)`

### 3.2 复合索引
- 用户课程进度：`idx_user_course(user_id, course_id)`
- 用户成就：`idx_user_achievement_user(user_id)`

---

## 4. 数据初始化

### 4.1 默认成就数据
```sql
-- 学习连续天数成就
INSERT INTO edu_achievement (achievement_code, achievement_name, achievement_type, description, tier, requirement_type, requirement_value) VALUES
('streak_7', '7天连续学习', 'streak', '连续学习7天', 'bronze', 'streak_days', 7),
('streak_30', '30天连续学习', 'streak', '连续学习30天', 'silver', 'streak_days', 30),
('streak_100', '100天连续学习', 'streak', '连续学习100天', 'gold', 'streak_days', 100),
('streak_365', '365天连续学习', 'streak', '连续学习365天', 'legendary', 'streak_days', 365);

-- 学习时长成就
INSERT INTO edu_achievement (achievement_code, achievement_name, achievement_type, description, tier, requirement_type, requirement_value) VALUES
('study_time_60', '学习达人', 'study', '累计学习60分钟', 'bronze', 'total_study_time', 60),
('study_time_600', '学习高手', 'study', '累计学习10小时', 'silver', 'total_study_time', 600),
('study_time_3600', '学习大师', 'study', '累计学习60小时', 'gold', 'total_study_time', 3600);

-- 词汇量成就
INSERT INTO edu_achievement (achievement_code, achievement_name, achievement_type, description, tier, requirement_type, requirement_value) VALUES
('vocab_100', '词汇初学者', 'study', '掌握100个单词', 'bronze', 'vocabulary_count', 100),
('vocab_500', '词汇进阶者', 'study', '掌握500个单词', 'silver', 'vocabulary_count', 500),
('vocab_1000', '词汇达人', 'study', '掌握1000个单词', 'gold', 'vocabulary_count', 1000),
('vocab_5000', '词汇专家', 'study', '掌握5000个单词', 'diamond', 'vocabulary_count', 5000);
```

### 4.2 徽章数据
```sql
INSERT INTO edu_badge (badge_name, badge_type, description, tier, requirement_type, requirement_value) VALUES
('初学者', 'master', '完成第一课', 'bronze', 'lessons_completed', 1),
('学习新星', 'streak', '连续学习7天', 'bronze', 'streak_days', 7),
('学习达人', 'score', '总分达到1000', 'silver', 'total_points', 1000),
('闯关达人', 'score', '通关10个关卡', 'silver', 'challenges_passed', 10),
('社区之星', 'social', '发帖被点赞100次', 'gold', 'total_likes_received', 100),
('全能学霸', 'master', '所有类型练习都完成过', 'legendary', 'all_practice_types', 1);
```

---

## 5. 性能优化建议

1. **读写分离**：将学习记录、排行榜等读多写少的数据读写分离
2. **分表策略**：
   - `edu_learning_record` 按月份分表
   - `edu_post_comment` 按帖子ID分表
3. **缓存策略**：
   - 用户进度、成就使用Redis缓存
   - 课程信息、排行榜使用本地缓存
4. **异步处理**：
   - 积分计算、成就检查异步执行
   - 社区帖子浏览数异步更新

---

## 6. 数据库维护

### 6.1 定期清理
- 学习记录保留最近6个月
- 登录日志保留30天
- 定时任务执行日志保留7天

### 6.2 数据备份
- 每日凌晨2点全量备份
- 每小时增量备份
- 保留最近30天备份
