package com.xxq.dronerent.controller;

import com.xxq.dronerent.common.Result;
import com.xxq.dronerent.common.UserRole;
import com.xxq.dronerent.service.SysRolePermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 角色管理控制器
 *
 * @author xxq
 * @since 2024-01-01
 */
@Tag(name = "角色管理", description = "系统角色的查询和管理接口")
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final SysRolePermissionService sysRolePermissionService;

    private static final List<String> DEFAULT_ADMIN_PERMISSIONS = Arrays.asList(
            "user:create", "user:update", "user:delete", "user:view",
            "drone:create", "drone:update", "drone:delete", "drone:view",
            "order:create", "order:update", "order:delete", "order:view",
            "customer:create", "customer:update", "customer:delete", "customer:view",
            "finance:view", "report:view"
    );

    private static final List<String> DEFAULT_EMPLOYEE_PERMISSIONS = Arrays.asList(
            "user:view",
            "drone:view",
            "order:create", "order:view",
            "customer:create", "customer:view"
    );

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
                .collect(java.util.stream.Collectors.toList());

        return Result.success(roles);
    }

    @Operation(summary = "获取角色权限", description = "获取指定角色的权限列表")
    @GetMapping("/{roleCode}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<String>> getRolePermissions(@PathVariable String roleCode) {
        UserRole.fromCode(roleCode); // 验证角色代码合法性

        List<String> permissions = sysRolePermissionService.getPermissionsByRoleCode(roleCode);

        // 如果数据库中没有权限记录，返回默认权限并初始化
        if (permissions.isEmpty()) {
            permissions = roleCode.equals("ADMIN") ? DEFAULT_ADMIN_PERMISSIONS : DEFAULT_EMPLOYEE_PERMISSIONS;
            sysRolePermissionService.updateRolePermissions(roleCode, permissions);
        }

        return Result.success(permissions);
    }

    @Operation(summary = "更新角色权限", description = "更新指定角色的权限列表")
    @PutMapping("/{roleCode}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> updateRolePermissions(
            @PathVariable String roleCode,
            @RequestBody List<String> permissions) {

        UserRole.fromCode(roleCode); // 验证角色代码合法性

        sysRolePermissionService.updateRolePermissions(roleCode, permissions);

        return Result.success("权限更新成功", null);
    }
}
