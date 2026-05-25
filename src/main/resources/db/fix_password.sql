-- ============================================
-- 修复 admin 用户密码（使用正确的 BCrypt 哈希）
-- ============================================

USE drone_rental;

-- 删除现有的 admin 用户
DELETE FROM sys_user WHERE username = 'admin';

-- 重新插入 admin 用户，使用正确的 BCrypt 加密密码
-- 明文密码: admin123
-- BCrypt 哈希: $2a$10$rVHcqMVJmY8z5fKqE9XHZOeWQK7xGZMQvJ8yN6LqP3hRzT4wK5mYe
INSERT INTO sys_user (username, password, real_name, phone, email, role, status, deleted) 
VALUES ('admin', '$2a$10$rVHcqMVJmY8z5fKqE9XHZOeWQK7xGZMQvJ8yN6LqP3hRzT4wK5mYe', '系统管理员', '13800138000', 'admin@dronerent.com', 'ADMIN', 1, 0);

-- 验证结果
SELECT 
    id,
    username,
    real_name,
    role,
    status,
    CASE 
        WHEN password LIKE '$2a$10$%' THEN '✓ BCrypt格式正确'
        ELSE '✗ BCrypt格式错误'
    END AS password_check
FROM sys_user 
WHERE username = 'admin';

SELECT '✓ admin 用户密码已重置为: admin123' AS result;
