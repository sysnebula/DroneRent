package com.xxq.dronerent.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 临时密码生成控制器（仅用于调试，生产环境应删除）
 */
@RestController
@RequestMapping("/api/test")
public class PasswordTestController {

    @GetMapping("/generate-password")
    public String generatePassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        String adminPassword = "admin123";
        String encodedAdmin = encoder.encode(adminPassword);
        
        String empPassword = "employee123";
        String encodedEmp = encoder.encode(empPassword);
        
        StringBuilder result = new StringBuilder();
        result.append("=== 密码生成结果 ===\n\n");
        result.append("明文: ").append(adminPassword).append("\n");
        result.append("加密: ").append(encodedAdmin).append("\n\n");
        result.append("明文: ").append(empPassword).append("\n");
        result.append("加密: ").append(encodedEmp).append("\n\n");
        result.append("=== SQL 更新语句 ===\n\n");
        result.append("UPDATE sys_user SET password = '").append(encodedAdmin).append("' WHERE username = 'admin';\n");
        result.append("UPDATE sys_user SET password = '").append(encodedEmp).append("' WHERE username = 'employee001';\n");
        
        return result.toString();
    }
}
