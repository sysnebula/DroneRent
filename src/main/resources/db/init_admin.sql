-- ============================================
-- 快速初始化/修复管理员账号
-- 如果数据库已存在，执行此脚本即可
-- ============================================

USE drone_rental;

-- 删除现有的 admin 用户（如果存在）
DELETE FROM sys_user WHERE username = 'admin';

-- 重新插入管理员账号
-- 用户名: admin
-- 密码: admin123 (BCrypt加密后的值)
INSERT INTO sys_user (username, password, real_name, phone, email, role, status) 
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '系统管理员', '13800138000', 'admin@dronerent.com', 'ADMIN', 1);

-- 验证插入结果
SELECT id, username, real_name, role, status FROM sys_user WHERE username = 'admin';

-- 如果需要重置其他测试账号
DELETE FROM sys_user WHERE username = 'employee001';

INSERT INTO sys_user (username, password, real_name, phone, email, role, status) 
VALUES ('employee001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '张三', '13800138001', 'zhangsan@dronerent.com', 'EMPLOYEE', 1);

-- 查看所有用户
SELECT * FROM sys_user;
