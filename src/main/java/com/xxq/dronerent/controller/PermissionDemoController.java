package com.xxq.dronerent.controller;

import com.xxq.dronerent.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 权限控制示例控制器
 * 演示如何使用 @PreAuthorize 进行权限控制
 *
 * @author xxq
 * @since 2024-01-01
 */
@Tag(name = "权限控制示例", description = "演示基于角色的权限控制")
@RestController
@RequestMapping("/api/demo")
public class PermissionDemoController {

    /**
     * 所有登录用户都可以访问
     */
    @Operation(summary = "公开接口", description = "所有登录用户都可以访问")
    @GetMapping("/public")
    public Result<String> publicEndpoint() {
        return Result.success("这是公开接口，所有登录用户都可以访问");
    }

    /**
     * 只有管理员可以访问
     */
    @Operation(summary = "管理员接口", description = "只有管理员角色可以访问")
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<String> adminEndpoint() {
        return Result.success("这是管理员接口，只有 ADMIN 角色可以访问");
    }

    /**
     * 管理员或员工都可以访问
     */
    @Operation(summary = "多角色接口", description = "管理员或员工角色都可以访问")
    @GetMapping("/staff")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public Result<String> staffEndpoint() {
        return Result.success("这是员工接口，ADMIN 和 EMPLOYEE 角色都可以访问");
    }

    /**
     * 基于权限表达式的控制
     */
    @Operation(summary = "自定义权限接口", description = "使用自定义权限表达式")
    @GetMapping("/custom")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Result<String> customPermissionEndpoint() {
        return Result.success("这是自定义权限接口");
    }

    /**
     * 复杂权限表达式示例
     */
    @Operation(summary = "复杂权限接口", description = "演示复杂的权限表达式")
    @PostMapping("/complex")
    @PreAuthorize("hasRole('ADMIN') and #userId != null")
    public Result<String> complexPermissionEndpoint(@RequestParam Long userId) {
        return Result.success("复杂权限验证通过，操作用户ID: " + userId);
    }
}
