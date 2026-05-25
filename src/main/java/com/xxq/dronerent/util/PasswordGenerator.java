package com.xxq.dronerent.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码生成工具
 * 用于生成 BCrypt 加密的密码
 *
 * @author xxq
 * @since 2024-01-01
 */
public class PasswordGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 生成 admin123 的 BCrypt 密码
        String password = "admin123";
        String encodedPassword = encoder.encode(password);
        
        System.out.println("========================================");
        System.out.println("明文密码: " + password);
        System.out.println("BCrypt加密: " + encodedPassword);
        System.out.println("========================================");
        
        // 验证密码
        boolean matches = encoder.matches(password, encodedPassword);
        System.out.println("验证结果: " + (matches ? "✓ 成功" : "✗ 失败"));
        
        // 生成 employee123 的密码
        String empPassword = "employee123";
        String encodedEmpPassword = encoder.encode(empPassword);
        System.out.println("\n========================================");
        System.out.println("明文密码: " + empPassword);
        System.out.println("BCrypt加密: " + encodedEmpPassword);
        System.out.println("========================================");
        
        // 输出 SQL 更新语句
        System.out.println("\nSQL 更新语句：");
        System.out.println("UPDATE sys_user SET password = '" + encodedPassword + "' WHERE username = 'admin';");
        System.out.println("UPDATE sys_user SET password = '" + encodedEmpPassword + "' WHERE username = 'employee001';");
    }
}
