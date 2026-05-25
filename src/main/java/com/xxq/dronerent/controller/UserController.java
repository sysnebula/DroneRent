package com.xxq.dronerent.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxq.dronerent.common.BusinessException;
import com.xxq.dronerent.common.PageResult;
import com.xxq.dronerent.common.Result;
import com.xxq.dronerent.dto.UserDTO;
import com.xxq.dronerent.entity.SysUser;
import com.xxq.dronerent.service.SysUserService;
import com.xxq.dronerent.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户管理控制器
 * 提供用户的增删改查接口
 *
 * @author xxq
 * @since 2024-01-01
 */
@Slf4j
@Tag(name = "用户管理", description = "系统用户的增删改查接口")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final SysUserService sysUserService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 分页查询用户列表
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @param username 用户名（可选）
     * @param role     角色（可选）
     * @return 分页结果
     */
    @Operation(summary = "分页查询用户列表", description = "支持按用户名和角色筛选")
    @GetMapping("/page")
    @PreAuthorize("hasRole('ADMIN')") // 需要管理员权限
    public Result<PageResult<UserVO>> pageUsers(
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页条数", example = "10") @RequestParam(defaultValue = "10") Long pageSize,
            @Parameter(description = "用户名") @RequestParam(required = false) String username,
            @Parameter(description = "角色") @RequestParam(required = false) String role) {
        
        log.info("分页查询用户: pageNum={}, pageSize={}, username={}, role={}", pageNum, pageSize, username, role);
        
        // 构建查询条件
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(username != null, SysUser::getUsername, username)
               .eq(role != null, SysUser::getRole, role)
               .orderByDesc(SysUser::getCreateTime);
        
        // 分页查询
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        Page<SysUser> userPage = sysUserService.page(page, wrapper);
        
        // 转换为 VO
        List<UserVO> userVOList = userPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        PageResult<UserVO> result = PageResult.build(
                userPage.getTotal(),
                userPage.getCurrent(),
                userPage.getSize(),
                userVOList
        );
        
        return Result.success(result);
    }

    /**
     * 根据ID查询用户详情
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @Operation(summary = "查询用户详情", description = "根据用户ID查询详细信息")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // 需要管理员权限
    public Result<UserVO> getUserById(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        
        SysUser user = sysUserService.getById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        return Result.success(convertToVO(user));
    }

    /**
     * 创建用户
     *
     * @param userDTO 用户信息
     * @return 操作结果
     */
    @Operation(summary = "创建用户", description = "新增一个系统用户")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // 需要管理员权限
    public Result<Void> createUser(@Valid @RequestBody UserDTO userDTO) {
        log.info("创建用户: {}", userDTO.getUsername());
        
        // 检查用户名是否已存在
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, userDTO.getUsername());
        if (sysUserService.count(wrapper) > 0) {
            throw new BusinessException("用户名已存在");
        }
        
        // 转换为实体
        SysUser user = new SysUser();
        BeanUtils.copyProperties(userDTO, user);
        
        // 加密密码
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        } else {
            throw new BusinessException("密码不能为空");
        }
        
        // 设置默认状态
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        
        // 保存
        sysUserService.save(user);
        
        return Result.success("创建成功", null);
    }

    /**
     * 更新用户
     *
     * @param id      用户ID
     * @param userDTO 用户信息
     * @return 操作结果
     */
    @Operation(summary = "更新用户", description = "修改用户信息")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // 需要管理员权限
    public Result<Void> updateUser(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Valid @RequestBody UserDTO userDTO) {
        
        log.info("更新用户: id={}", id);
        
        // 检查用户是否存在
        SysUser existUser = sysUserService.getById(id);
        if (existUser == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 检查用户名是否被其他用户使用
        if (!existUser.getUsername().equals(userDTO.getUsername())) {
            LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysUser::getUsername, userDTO.getUsername());
            if (sysUserService.count(wrapper) > 0) {
                throw new BusinessException("用户名已存在");
            }
        }
        
        // 更新字段
        BeanUtils.copyProperties(userDTO, existUser);
        
        // 如果提供了新密码，则更新密码
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        
        // 保存
        sysUserService.updateById(existUser);
        
        return Result.success("更新成功", null);
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 操作结果
     */
    @Operation(summary = "删除用户", description = "根据ID删除用户（逻辑删除）")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // 需要管理员权限
    public Result<Void> deleteUser(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        
        log.info("删除用户: id={}", id);
        
        SysUser user = sysUserService.getById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 不允许删除自己
        // TODO: 从 SecurityContext 获取当前用户ID进行比较
        
        // 删除（逻辑删除）
        sysUserService.removeById(id);
        
        return Result.success("删除成功", null);
    }

    /**
     * 启用/禁用用户
     *
     * @param id     用户ID
     * @param status 状态（0-禁用，1-启用）
     * @return 操作结果
     */
    @Operation(summary = "启用/禁用用户", description = "修改用户的启用状态")
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')") // 需要管理员权限
    public Result<Void> updateUserStatus(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "状态（0-禁用，1-启用）") @RequestParam Integer status) {
        
        log.info("修改用户状态: id={}, status={}", id, status);
        
        SysUser user = sysUserService.getById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        user.setStatus(status);
        sysUserService.updateById(user);
        
        String message = status == 1 ? "启用成功" : "禁用成功";
        return Result.success(message, null);
    }

    /**
     * 重置用户密码
     *
     * @param id       用户ID
     * @param newPassword 新密码
     * @return 操作结果
     */
    @Operation(summary = "重置用户密码", description = "管理员重置用户密码")
    @PatchMapping("/{id}/password")
    @PreAuthorize("hasRole('ADMIN')") // 需要管理员权限
    public Result<Void> resetPassword(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "新密码") @RequestParam String newPassword) {
        
        log.info("重置用户密码: id={}", id);
        
        SysUser user = sysUserService.getById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 加密新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        sysUserService.updateById(user);
        
        return Result.success("密码重置成功", null);
    }

    /**
     * 将 SysUser 转换为 UserVO
     */
    private UserVO convertToVO(SysUser user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}
