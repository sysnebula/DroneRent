package com.xxq.dronerent.controller;

import com.xxq.dronerent.common.Result;
import com.xxq.dronerent.common.UserRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色管理控制器
 * 提供角色查询和管理接口
 *
 * @author xxq
 * @since 2024-01-01
 */
@Tag(name = "角色管理", description = "系统角色的查询和管理接口")
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    /**
     * 获取所有角色列表
     *
     * @return 角色列表
     */
    @Operation(summary = "获取所有角色", description = "获取系统中所有的角色列表")
    @GetMapping
    public Result<List<Map<String, String>>> getAllRoles() {
        List<Map<String, String>> roles = Arrays.stream(UserRole.values())
                .map(role -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("code", role.getCode());
                    map.put("name", role.getName());
                    return map;
                })
                .collect(Collectors.toList());
        
        return Result.success(roles);
    }

    /**
     * 获取角色的权限列表
     *
     * @param roleCode 角色代码
     * @return 权限列表
     */
    @Operation(summary = "获取角色权限", description = "获取指定角色的权限列表")
    @GetMapping("/{roleCode}/permissions")
    @PreAuthorize("hasRole('ADMIN')") // 需要管理员权限
    public Result<List<String>> getRolePermissions(@PathVariable String roleCode) {
        UserRole role = UserRole.fromCode(roleCode);
        
        // TODO: 从数据库或配置中获取角色的权限列表
        // 这里只是示例，实际项目中应该根据业务需求定义权限
        
        List<String> permissions;
        if (role == UserRole.ADMIN) {
            // 管理员拥有所有权限
            permissions = Arrays.asList(
                "user:create", "user:update", "user:delete", "user:view",
                "drone:create", "drone:update", "drone:delete", "drone:view",
                "order:create", "order:update", "order:delete", "order:view",
                "customer:create", "customer:update", "customer:delete", "customer:view",
                "finance:view", "report:view"
            );
        } else {
            // 员工只有查看和基本操作权限
            permissions = Arrays.asList(
                "user:view",
                "drone:view",
                "order:create", "order:view",
                "customer:create", "customer:view"
            );
        }
        
        return Result.success(permissions);
    }

    /**
     * 更新角色权限
     *
     * @param roleCode    角色代码
     * @param permissions 权限列表
     * @return 操作结果
     */
    @Operation(summary = "更新角色权限", description = "更新指定角色的权限列表")
    @PutMapping("/{roleCode}/permissions")
    @PreAuthorize("hasRole('ADMIN')") // 需要管理员权限
    public Result<Void> updateRolePermissions(
            @PathVariable String roleCode,
            @RequestBody List<String> permissions) {
        
        // TODO: 实现权限更新逻辑
        // 这里只是示例，实际项目中应该保存到数据库
        
        UserRole role = UserRole.fromCode(roleCode);
        
        // 示例：打印更新的权限
        System.out.println("更新角色 " + role.getName() + " 的权限:");
        permissions.forEach(permission -> System.out.println("  - " + permission));
        
        return Result.success("权限更新成功", null);
    }
}
