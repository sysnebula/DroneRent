-- ============================================
-- 强制重置 admin 用户（终极解决方案）
-- ============================================

USE drone_rental;

-- 1. 删除所有 admin 用户记录
DELETE FROM sys_user WHERE username = 'admin';

-- 2. 重新插入完全正确的 admin 用户
INSERT INTO sys_user (
    username, 
    password, 
    real_name, 
    phone, 
    email, 
    role, 
    status,
    deleted
) VALUES (
    'admin', 
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 
    '系统管理员', 
    '13800138000', 
    'admin@dronerent.com', 
    'ADMIN', 
    1,
    0
);

-- 3. 验证插入结果
SELECT 
    id,
    username,
    password,
    real_name,
    role,
    status,
    deleted,
    CASE 
        WHEN password = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi' THEN '✓ 密码正确'
        ELSE '✗ 密码错误'
    END AS verification
FROM sys_user 
WHERE username = 'admin';

-- 4. 显示最终结果
SELECT '✓ admin 用户已重置，用户名: admin, 密码: admin123' AS result;
