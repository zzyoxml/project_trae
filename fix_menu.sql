SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

UPDATE sys_menu SET menu_name = '教育管理' WHERE menu_id = 2000;
UPDATE sys_menu SET menu_name = '课程管理' WHERE menu_id = 2001;
UPDATE sys_menu SET menu_name = '学习数据' WHERE menu_id = 2002;
UPDATE sys_menu SET menu_name = '社区管理' WHERE menu_id = 2003;
UPDATE sys_menu SET menu_name = '成就管理' WHERE menu_id = 2004;
UPDATE sys_menu SET menu_name = '单元管理' WHERE menu_id = 2005;
UPDATE sys_menu SET menu_name = '课时管理' WHERE menu_id = 2006;

SELECT '菜单名称修复完成' AS result;