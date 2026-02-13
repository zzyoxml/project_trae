-- ============================================
-- 多语种学习平台 - 教育模块表结构
-- 若依框架 edu 模块建表脚本
-- ============================================

USE ryvue;

-- -------------------------------------------
-- 1. 课程表 edu_course
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS `edu_course` (
  `course_id`          bigint(20)   NOT NULL AUTO_INCREMENT  COMMENT '课程ID',
  `course_name`        varchar(100) NOT NULL                  COMMENT '课程名称',
  `course_code`        varchar(50)  DEFAULT ''                COMMENT '课程编码',
  `category_id`        bigint(20)   DEFAULT NULL              COMMENT '分类ID',
  `language`           varchar(10)  DEFAULT 'en'              COMMENT '课程语言：en,ja,zh',
  `level`              varchar(20)  DEFAULT 'beginner'        COMMENT '课程等级：beginner,elementary,intermediate,advanced',
  `course_type`        varchar(30)  DEFAULT 'general'         COMMENT '课程类型：general,conversation,grammar,vocabulary,listening,speaking',
  `description`        text                                    COMMENT '课程描述',
  `cover_image`        varchar(500) DEFAULT ''                COMMENT '封面图URL',
  `trailer_video`      varchar(500) DEFAULT ''                COMMENT '预告视频URL',
  `teacher_id`         bigint(20)   DEFAULT NULL              COMMENT '授课教师ID',
  `total_duration`     int(11)      DEFAULT 0                 COMMENT '总时长（分钟）',
  `total_lessons`      int(11)      DEFAULT 0                 COMMENT '总课时数',
  `total_students`     int(11)      DEFAULT 0                 COMMENT '学习人数',
  `rating`             decimal(3,2) DEFAULT 0.00              COMMENT '平均评分',
  `rating_count`       int(11)      DEFAULT 0                 COMMENT '评分次数',
  `is_free`            char(1)      DEFAULT '1'               COMMENT '是否免费（0否 1是）',
  `price`              decimal(10,2) DEFAULT 0.00             COMMENT '课程价格',
  `is_published`       char(1)      DEFAULT '0'               COMMENT '是否发布（0否 1是）',
  `is_featured`        char(1)      DEFAULT '0'               COMMENT '是否精选（0否 1是）',
  `prerequisites`      text                                    COMMENT '先修课程要求',
  `learning_objectives` text                                   COMMENT '学习目标',
  `tags`               varchar(500) DEFAULT ''                COMMENT '标签（逗号分隔）',
  `difficulty_score`   int(11)      DEFAULT 3                 COMMENT '难度系数（1-5）',
  `popularity_score`   int(11)      DEFAULT 0                 COMMENT '热度评分',
  `del_flag`           char(1)      DEFAULT '0'               COMMENT '删除标志（0存在 2删除）',
  `create_dept`        bigint(20)   DEFAULT NULL              COMMENT '创建部门',
  `create_by`          varchar(64)  DEFAULT ''                COMMENT '创建者',
  `create_time`        datetime                               COMMENT '创建时间',
  `update_by`          varchar(64)  DEFAULT ''                COMMENT '更新者',
  `update_time`        datetime                               COMMENT '更新时间',
  PRIMARY KEY (`course_id`),
  KEY `idx_category` (`category_id`),
  KEY `idx_language` (`language`),
  KEY `idx_published` (`is_published`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- -------------------------------------------
-- 2. 课程章节表 edu_course_chapter
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS `edu_course_chapter` (
  `chapter_id`         bigint(20)   NOT NULL AUTO_INCREMENT  COMMENT '章节ID',
  `course_id`          bigint(20)   NOT NULL                  COMMENT '所属课程ID',
  `chapter_name`       varchar(100) NOT NULL                  COMMENT '章节名称',
  `chapter_order`      int(11)      DEFAULT 1                 COMMENT '章节排序',
  `description`        text                                    COMMENT '章节描述',
  `total_units`        int(11)      DEFAULT 0                 COMMENT '单元数量',
  `total_lessons`      int(11)      DEFAULT 0                 COMMENT '课时总数',
  `del_flag`           char(1)      DEFAULT '0'               COMMENT '删除标志（0存在 2删除）',
  `create_by`          varchar(64)  DEFAULT ''                COMMENT '创建者',
  `create_time`        datetime                               COMMENT '创建时间',
  `update_by`          varchar(64)  DEFAULT ''                COMMENT '更新者',
  `update_time`        datetime                               COMMENT '更新时间',
  PRIMARY KEY (`chapter_id`),
  KEY `idx_course_id` (`course_id`),
  CONSTRAINT `fk_chapter_course` FOREIGN KEY (`course_id`) REFERENCES `edu_course` (`course_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='课程章节表';

-- -------------------------------------------
-- 3. 课程单元表 edu_course_unit
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS `edu_course_unit` (
  `unit_id`            bigint(20)   NOT NULL AUTO_INCREMENT  COMMENT '单元ID',
  `course_id`          bigint(20)   NOT NULL                  COMMENT '所属课程ID',
  `chapter_id`         bigint(20)   DEFAULT NULL              COMMENT '所属章节ID',
  `unit_name`          varchar(100) NOT NULL                  COMMENT '单元名称',
  `unit_order`         int(11)      DEFAULT 1                 COMMENT '单元排序',
  `description`        text                                    COMMENT '单元描述',
  `total_lessons`      int(11)      DEFAULT 0                 COMMENT '课时数',
  `total_duration`     int(11)      DEFAULT 0                 COMMENT '总时长（分钟）',
  `is_locked`          tinyint(1)   DEFAULT 0                 COMMENT '是否锁定（0否 1是）',
  `passing_score`      int(11)      DEFAULT 60                COMMENT '及格分数',
  `experience_reward`  int(11)      DEFAULT 100               COMMENT '经验奖励',
  `del_flag`           char(1)      DEFAULT '0'               COMMENT '删除标志（0存在 2删除）',
  `create_by`          varchar(64)  DEFAULT ''                COMMENT '创建者',
  `create_time`        datetime                               COMMENT '创建时间',
  `update_by`          varchar(64)  DEFAULT ''                COMMENT '更新者',
  `update_time`        datetime                               COMMENT '更新时间',
  PRIMARY KEY (`unit_id`),
  KEY `idx_course_id` (`course_id`),
  KEY `idx_chapter_id` (`chapter_id`),
  CONSTRAINT `fk_unit_course` FOREIGN KEY (`course_id`) REFERENCES `edu_course` (`course_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_unit_chapter` FOREIGN KEY (`chapter_id`) REFERENCES `edu_course_chapter` (`chapter_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='课程单元表';

-- -------------------------------------------
-- 4. 课程课时表 edu_course_lesson
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS `edu_course_lesson` (
  `lesson_id`          bigint(20)   NOT NULL AUTO_INCREMENT  COMMENT '课时ID',
  `unit_id`            bigint(20)   NOT NULL                  COMMENT '所属单元ID',
  `lesson_name`        varchar(100) NOT NULL                  COMMENT '课时名称',
  `lesson_order`       int(11)      DEFAULT 1                 COMMENT '课时排序',
  `lesson_type`        varchar(20)  DEFAULT 'vocabulary'      COMMENT '课时类型：vocabulary,grammar,listening,speaking,quiz,video',
  `content`            longtext                                COMMENT '学习内容(JSON)',
  `duration`           int(11)      DEFAULT 0                 COMMENT '时长（分钟）',
  `is_free`            char(1)      DEFAULT '1'               COMMENT '是否免费试看（0否 1是）',
  `del_flag`           char(1)      DEFAULT '0'               COMMENT '删除标志（0存在 2删除）',
  `create_by`          varchar(64)  DEFAULT ''                COMMENT '创建者',
  `create_time`        datetime                               COMMENT '创建时间',
  `update_by`          varchar(64)  DEFAULT ''                COMMENT '更新者',
  `update_time`        datetime                               COMMENT '更新时间',
  PRIMARY KEY (`lesson_id`),
  KEY `idx_unit_id` (`unit_id`),
  CONSTRAINT `fk_lesson_unit` FOREIGN KEY (`unit_id`) REFERENCES `edu_course_unit` (`unit_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='课程课时表';
