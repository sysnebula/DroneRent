-- ============================================
-- 诊断脚本 - 检查 admin 用户状态
-- ============================================

USE drone_rental;

-- 1. 检查数据库是否存在
SELECT DATABASE() AS current_database;

-- 2. 检查 sys_user 表是否存在
SHOW TABLES LIKE 'sys_user';

-- 3. 查看 admin 用户的完整信息
SELECT 
    id,
    username,
    password,
    LENGTH(password) AS password_length,
    LEFT(password, 7) AS password_prefix,
    real_name,
    role,
    status,
    deleted,
    create_time
FROM sys_user 
WHERE username = 'admin';

-- 4. 检查是否有其他用户
SELECT COUNT(*) AS total_users FROM sys_user WHERE deleted = 0;

-- 5. 验证密码格式是否正确（应该以 $2a$10$ 开头）
SELECT 
    username,
    CASE 
        WHEN password LIKE '$2a$10$%' THEN '✓ BCrypt格式正确'
        ELSE '✗ BCrypt格式错误'
    END AS password_format_check
FROM sys_user 
WHERE username = 'admin';

-- 6. 如果密码格式错误，显示正确的密码哈希值
SELECT 
    '正确的BCrypt密码哈希值（对应明文 admin123）:' AS note,
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi' AS correct_hash;
