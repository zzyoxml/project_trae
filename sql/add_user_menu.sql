-- ============================================
-- 添加学员管理菜单
-- ============================================

-- 插入学员管理菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2009, '学员管理', 2000, 7, 'user', 'edu/user/index', '1', '0', '0', '0', 'edu:user:list', 'peoples', 'admin', NOW(), NULL, NULL, '学员用户管理菜单');

-- 为超级管理员角色添加学员管理菜单权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2009);

SELECT '学员管理菜单添加成功！' AS message;