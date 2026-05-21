-- ============================================
-- 开启后台登录验证码功能
-- 执行日期：2026-05-21
-- ============================================

-- 开启验证码功能（true开启，false关闭）
UPDATE `sys_config` SET `config_value` = 'true' WHERE `config_key` = 'sys.account.captchaEnabled';

-- 关闭用户注册功能（后台管理不需要开放注册）
UPDATE `sys_config` SET `config_value` = 'false' WHERE `config_key` = 'sys.account.registerUser';
