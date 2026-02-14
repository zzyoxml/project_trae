INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2007, 'Import/Export', 2000, 7, 'import-export', 'edu/import-export/index', '1', '0', '0', '0', 'edu:importexport:list', 'upload', 'admin', NOW(), NULL, NULL, 'Course content import export management');

INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2007);

SELECT 'Import/Export menu added successfully!' AS message;