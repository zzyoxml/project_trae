-- ============================================
-- 教育平台应用用户表 edu_app_user
-- 用于存储前端应用（http://localhost:3000/）的用户信息
-- 与管理后台用户（sys_user）分离，互不干扰
-- ============================================

USE ryvue;

-- -------------------------------------------
-- 教育平台应用用户表 edu_app_user
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS `edu_app_user` (
  `user_id`            bigint(20)   NOT NULL AUTO_INCREMENT  COMMENT '用户ID',
  `username`           varchar(30)  NOT NULL                  COMMENT '用户名',
  `password`           varchar(100) NOT NULL                  COMMENT '密码',
  `nick_name`          varchar(30)  DEFAULT ''                COMMENT '昵称',
  `email`              varchar(50)  DEFAULT ''                COMMENT '邮箱',
  `phonenumber`        varchar(11)  DEFAULT ''                COMMENT '手机号码',
  `avatar`             varchar(100) DEFAULT ''                COMMENT '头像路径',
  `status`             char(1)      DEFAULT '0'               COMMENT '帐号状态（0正常 1停用）',
  `del_flag`           char(1)      DEFAULT '0'               COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by`          varchar(64)  DEFAULT ''                COMMENT '创建者',
  `create_time`        datetime                               COMMENT '创建时间',
  `update_by`          varchar(64)  DEFAULT ''                COMMENT '更新者',
  `update_time`        datetime                               COMMENT '更新时间',
  `remark`             varchar(500) DEFAULT NULL              COMMENT '备注',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='教育平台应用用户表';

-- -------------------------------------------
-- 数据迁移：将现有非admin用户从sys_user迁移到edu_app_user
-- -------------------------------------------
-- 注意：执行此迁移前请备份数据
-- INSERT INTO edu_app_user (user_id, username, password, nick_name, email, phonenumber, avatar, status, del_flag, create_by, create_time, update_by, update_time, remark)
-- SELECT user_id, user_name, password, nick_name, email, phonenumber, avatar, status, del_flag, create_by, create_time, update_by, update_time, remark
-- FROM sys_user
-- WHERE user_name != 'admin';
