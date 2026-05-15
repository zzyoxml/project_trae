INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2000, '教育管理', 0, 2, 'edu', NULL, '1', '0', '0', '0', '', 'education', 'admin', NOW(), NULL, NULL, '教育平台管理菜单'),
(2001, '课程管理', 2000, 1, 'course', 'edu/course/index', '1', '0', '0', '0', 'edu:course:list', 'education', 'admin', NOW(), NULL, NULL, '课程管理菜单'),
(2002, '学习数据', 2000, 2, 'learning', 'edu/learning/index', '1', '0', '0', '0', 'edu:learning:list', 'chart', 'admin', NOW(), NULL, NULL, '用户学习数据管理菜单'),
(2003, '社区管理', 2000, 3, 'community', 'edu/community/index', '1', '0', '0', '0', 'edu:community:list', 'message', 'admin', NOW(), NULL, NULL, '社区内容管理菜单'),
(2004, '成就管理', 2000, 4, 'achievement', 'edu/achievement/index', '1', '0', '0', '0', 'edu:achievement:list', 'star', 'admin', NOW(), NULL, NULL, '成就系统管理菜单'),
(2005, '章节管理', 2000, 5, 'unit', 'edu/unit/index', '1', '0', '0', '0', 'edu:unit:list', 'tree-table', 'admin', NOW(), NULL, NULL, '课程章节管理菜单'),
(2006, '课时管理', 2000, 6, 'lesson', 'edu/lesson/index', '1', '0', '0', '0', 'edu:lesson:list', 'list', 'admin', NOW(), NULL, NULL, '课时内容管理菜单'),
(2007, '词汇管理', 2000, 7, 'vocabulary', 'edu/vocabulary/index', '1', '0', '0', '0', 'edu:vocabulary:list', 'language', 'admin', NOW(), NULL, NULL, '词汇内容管理菜单'),
(2008, '导入导出', 2000, 8, 'importexport', 'edu/import-export/index', '1', '0', '0', '0', 'edu:importexport:list', 'upload', 'admin', NOW(), NULL, NULL, '数据导入导出菜单');
INSERT INTO sys_role_menu (role_id, menu_id) SELECT 1, menu_id FROM sys_menu WHERE menu_id >= 2000;
