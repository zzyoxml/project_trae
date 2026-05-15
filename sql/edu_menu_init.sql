-- ============================================
-- 多语种学习平台后台管理菜单初始化脚本
-- ============================================

-- 注意: 执行此脚本前请备份数据
-- 执行方式: 在MySQL中执行此SQL文件

-- 1. 清理旧的教育管理菜单（可选，如果需要重新初始化）
-- DELETE FROM sys_menu WHERE menu_name IN ('教育管理', '课程管理', '学习数据', '社区管理', '成就管理');

-- 2. 插入教育管理主菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2000, '教育管理', 0, 2, 'edu', NULL, '1', '0', '0', '0', '', 'education', 'admin', NOW(), NULL, NULL, '教育平台管理菜单');

-- 3. 插入课程管理菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2001, '课程管理', 2000, 1, 'course', 'edu/course/index', '1', '0', '0', '0', 'edu:course:list', 'education', 'admin', NOW(), NULL, NULL, '课程管理菜单');

-- 4. 插入学习数据菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2002, '学习数据', 2000, 2, 'learning', 'edu/learning/index', '1', '0', '0', '0', 'edu:learning:list', 'chart', 'admin', NOW(), NULL, NULL, '用户学习数据管理菜单');

-- 5. 插入社区管理菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2003, '社区管理', 2000, 3, 'community', 'edu/community/index', '1', '0', '0', '0', 'edu:community:list', 'message', 'admin', NOW(), NULL, NULL, '社区内容管理菜单');

-- 6. 插入成就管理菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2004, '成就管理', 2000, 4, 'achievement', 'edu/achievement/index', '1', '0', '0', '0', 'edu:achievement:list', 'star', 'admin', NOW(), NULL, NULL, '成就系统管理菜单');

-- 7. 插入章节管理菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2005, '章节管理', 2000, 5, 'unit', 'edu/unit/index', '1', '0', '0', '0', 'edu:unit:list', 'tree-table', 'admin', NOW(), NULL, NULL, '课程章节管理菜单');

-- 8. 插入课时管理菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2006, '课时管理', 2000, 6, 'lesson', 'edu/lesson/index', '1', '0', '0', '0', 'edu:lesson:list', 'list', 'admin', NOW(), NULL, NULL, '课时内容管理菜单');

-- 9. 插入词汇管理菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2007, '词汇管理', 2000, 7, 'vocabulary', 'edu/vocabulary/index', '1', '0', '0', '0', 'edu:vocabulary:list', 'language', 'admin', NOW(), NULL, NULL, '词汇内容管理菜单');

-- 10. 插入导入导出菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2008, '导入导出', 2000, 8, 'importexport', 'edu/import-export/index', '1', '0', '0', '0', 'edu:importexport:list', 'upload', 'admin', NOW(), NULL, NULL, '数据导入导出菜单');

-- 11. 为超级管理员角色添加所有教育管理菜单权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu WHERE menu_id >= 2000;

-- 12. 输出提示信息
SELECT '教育平台菜单初始化完成！' AS message;